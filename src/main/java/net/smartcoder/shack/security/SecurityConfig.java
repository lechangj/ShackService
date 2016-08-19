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
import javax.sql.DataSource;

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
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
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
	private static final String REMEMBER_ME_KEY = "shack-key";
	private static final String REMEMBER_ME_PARAMETER = "remember-me";
	private static final String LOGOUT = "/api/logout";

	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

    @Autowired
    private ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    @Autowired
    private ApiAuthenticationFailureHandler apiAuthenticationFailureHandler;

    @Autowired
    private ApiLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;
    
    @Autowired
    DataSource dataSource;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);
		
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder());
		auth.authenticationProvider(daoProvider);		

        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        auth.authenticationProvider(preAuthProvider);
		
		RememberMeAuthenticationProvider rememberMeProvider = 
				new RememberMeAuthenticationProvider(REMEMBER_ME_KEY);
		auth.authenticationProvider(rememberMeProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
//        	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        		.antMatchers(API_CAR_LIST_URL).permitAll()
				.anyRequest().authenticated()
                .antMatchers(API_USER_URL).hasRole("USER");
//                .antMatchers(API_LOGIN_URL).permitAll();
		
		http.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationEntryPoint)
				.accessDeniedHandler(restAccessDeniedHandler);	
		
		http.csrf()
			.csrfTokenRepository(csrfTokenRepository());
		
		http.addFilterAfter(authFilter(), ApiRequestHeaderAuthenticationFilter.class)
			.addFilter(preAuthFilter())
			.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
			.addFilterBefore(getRememberMeAuthenticationFilter(), RememberMeAuthenticationFilter.class);
		
		http.logout().logoutUrl(LOGOUT)
			.logoutSuccessHandler(logoutSuccessHandler)
			.logoutSuccessUrl(API_CAR_LIST_URL);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.GET, "/resources/*")
						.antMatchers(HttpMethod.OPTIONS, "/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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
        filter.setAuthenticationManager(authenticationManager());
        filter.setRememberMeServices(getPersistentTokenBasedRememberMeServices());
        filter.setAuthenticationSuccessHandler(apiAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(apiAuthenticationFailureHandler);

        return filter;
    }

    @Bean(name = "preAuthFilter")
    public Filter preAuthFilter() throws Exception {
        log.info("Creating preAuthFilter...");
        ApiRequestHeaderAuthenticationFilter filter = new ApiRequestHeaderAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
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
						log.info("ContextPath: " + request.getContextPath());
						cookie.setPath(request.getContextPath()+"/");
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
	
	//Remember-me Related
	
	@Bean
	public RememberMeAuthenticationFilter getRememberMeAuthenticationFilter() throws Exception {
		RememberMeAuthenticationFilter rememberMeAuthenticationFilter = 
				new RememberMeAuthenticationFilter(
						authenticationManager(), getPersistentTokenBasedRememberMeServices());		
		return rememberMeAuthenticationFilter;
	}

	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {		

        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);
        
		PersistentTokenBasedRememberMeServices tokenBasedService = new PersistentTokenBasedRememberMeServices(
				REMEMBER_ME_KEY, userDetailsService, tokenRepositoryImpl);
		tokenBasedService.setParameter(REMEMBER_ME_PARAMETER);
		tokenBasedService.setTokenValiditySeconds(60 * 60 * 24);
		return tokenBasedService;
	}

}
