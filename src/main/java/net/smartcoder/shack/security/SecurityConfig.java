package net.smartcoder.shack.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import net.smartcoder.shack.ServerConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String API_CAR_URL = "/api/car/**";
    private static final String API_CAR_LIST_URL = "/api/car/list";
    private static final String API_USER_URL = "/api/**";
	private static final String API_LOGIN_URL = "/api/authenticate";
	private static final String REMEMBER_ME_KEY = "remember-me";

	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

    @Autowired
    private ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    @Autowired
    private ApiAuthenticationFailureHandler apiAuthenticationFailureHandler;

	PersistentTokenRepository tokenRepository;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

    @Bean(name="myAuthenticationManager")
	@Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
        		.antMatchers(API_CAR_LIST_URL).permitAll()
				.anyRequest().authenticated()
                .antMatchers(API_USER_URL).hasRole("USER");
//                .antMatchers(API_LOGIN_URL).anonymous();
		
		http.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationEntryPoint)
				.accessDeniedHandler(restAccessDeniedHandler);	
		
		http.csrf()
			.csrfTokenRepository(csrfTokenRepository()).and()
			.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		
		http.addFilterAfter(authFilter(), ApiRequestHeaderAuthenticationFilter.class)
			.addFilter(preAuthFilter());

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.GET, "/resources/*");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices tokenBasedService = new PersistentTokenBasedRememberMeServices(
				REMEMBER_ME_KEY, userDetailsService, tokenRepository);
		return tokenBasedService;
	}
	
	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver(){
		return new AuthenticationTrustResolverImpl();
	}

    @Bean(name = "authFilter")
    public Filter authFilter() throws Exception {
        log.info("Creating authFilter...");

        RequestMatcher antReqMatch = new AntPathRequestMatcher(API_LOGIN_URL);

        List<RequestMatcher> reqMatches = new ArrayList<>();
        reqMatches.add(antReqMatch);
        RequestMatcher reqMatch = new AndRequestMatcher(reqMatches);

        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setPostOnly(true);
        filter.setUsernameParameter(USERNAME);
        filter.setPasswordParameter(PASSWORD);
        filter.setRequiresAuthenticationRequestMatcher(reqMatch);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(apiAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(apiAuthenticationFailureHandler);

        return filter;
    }

    @Bean(name = "preAuthFilter")
    public Filter preAuthFilter() {
        log.info("Creating preAuthFilter...");
        ApiRequestHeaderAuthenticationFilter filter = new ApiRequestHeaderAuthenticationFilter();
        filter.setAuthenticationManager(preAuthAuthenticationManager());
        return filter;
    }

    @Bean(name = "preAuthAuthenticationManager")
    public AuthenticationManager preAuthAuthenticationManager() {
        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();

        List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
        providers.add(preAuthProvider);

        return new ProviderManager(providers);
    }

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				log.debug("csrf: " + csrf);
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, ServerConstants.XSRF_TOKEN);
					String token = csrf.getToken();
					log.info("token: " + token);
					if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
						cookie = new Cookie(ServerConstants.XSRF_TOKEN, token);
						cookie.setPath(request.getContextPath());
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(ServerConstants.X_XSRF_TOKEN);
		return repository;
	}
	
	@Bean(name = "authenticationTokenProcessingFilter")
	public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter(){
		return new AuthenticationTokenProcessingFilter(userDetailsService);
	}

}
