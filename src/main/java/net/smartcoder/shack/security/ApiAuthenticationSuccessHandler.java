package net.smartcoder.shack.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import net.smartcoder.shack.model.User;
import net.smartcoder.shack.service.UserService;

@Component
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		User user = userService.findByUsername(authentication.getName());
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, user, userDetails);
		
	}

}
