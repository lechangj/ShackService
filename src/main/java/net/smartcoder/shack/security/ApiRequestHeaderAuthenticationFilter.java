package net.smartcoder.shack.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import net.smartcoder.shack.ServerConstants;

public class ApiRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {
	
	static final Logger log = LoggerFactory.getLogger(ApiRequestHeaderAuthenticationFilter.class);

	public ApiRequestHeaderAuthenticationFilter() {		
		super.setPrincipalRequestHeader(ServerConstants.X_AUTH_TOKEN);
		super.setExceptionIfHeaderMissing(false);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Authenticating for url: " + ((HttpServletRequest)request).getRequestURL().toString());
		super.doFilter(request, response, chain);
	}

}
