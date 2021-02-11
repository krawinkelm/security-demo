package de.ordix.securitydemo.http.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class DemoController {

	@GetMapping(value = "/without-authentication", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> withoutAuthentication() {
		return new ResponseEntity<String>("Without Authentication", HttpStatus.OK);
	}
	
	@GetMapping(value = "/basic-authentication", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> basicAuthentication() {
		return new ResponseEntity<String>("Basic Authentication", HttpStatus.OK);
	}
	
	@GetMapping(value = "/filter-authentication", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> filterAuthentication() {
		return new ResponseEntity<String>("Filter Authentication", HttpStatus.OK);
	}

}
