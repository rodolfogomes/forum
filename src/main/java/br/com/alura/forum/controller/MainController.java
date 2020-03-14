package br.com.alura.forum.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {
	
	@GetMapping
	@Cacheable(value = "listaTopicos")
	public String hello(){
		
		return "Hello world!";
	}
	
	

}
