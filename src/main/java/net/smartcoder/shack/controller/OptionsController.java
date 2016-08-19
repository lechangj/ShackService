package net.smartcoder.shack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {
	static final Logger log = LoggerFactory.getLogger(OptionsController.class);
	
	@RequestMapping(method = RequestMethod.OPTIONS, value="/*")
	public ResponseEntity handle(){
		log.info("Options request is called.");
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

}
