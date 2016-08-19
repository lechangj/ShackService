package net.smartcoder.shack.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
	
	static final Logger log = LoggerFactory.getLogger(CORSFilter.class);

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
        log.info("Filtering on CORSFilter");
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-auth-token, x-requested-with, Accept, Content-Type, Origin");	//"x-auth-token, x-requested-with, Accept, Content-Type, Origin, Access-Control-Request-Method, Access-Control-Request-Headers"
        
        HttpServletRequest request = (HttpServletRequest) req;
        if(request.getMethod().equals("OPTIONS")) {
        	log.info("CORS Filter: OPTIONS");
//        	response.setStatus(HttpStatus.NO_CONTENT.value());
//        	response.getWriter().flush();
        } else {
        	chain.doFilter(req, res);
        }

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
