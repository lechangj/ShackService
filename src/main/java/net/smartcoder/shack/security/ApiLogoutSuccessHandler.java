package net.smartcoder.shack.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import antlr.StringUtils;
import net.smartcoder.shack.ServerConstants;

@Component
public class ApiLogoutSuccessHandler implements LogoutSuccessHandler {
	
	static final Logger log = LoggerFactory.getLogger(ApiLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(authentication != null){
			log.info(authentication.getName());
		}
		Cookie[] cookies = request.getCookies();
		log.info("No of cookies"+ cookies.length);
		if(cookies != null){
			for (Cookie c : cookies){
				c.setValue(null);
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
		response.setStatus(HttpStatus.OK.value());
        response.setHeader(ServerConstants.X_AUTH_TOKEN, "");

	}

}
