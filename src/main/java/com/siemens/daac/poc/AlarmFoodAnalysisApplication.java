package com.siemens.daac.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.EnableJms;

import com.siemens.daac.poc.service.RManager;

@SpringBootApplication
@EnableJms
public class AlarmFoodAnalysisApplication {

	@Autowired
	RManager manager;
	
	public static void main(String[] args) {
		SpringApplication.run(AlarmFoodAnalysisApplication.class, args);
	}
	
//	TODO: Need to delete it as added only for testing
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
//	    manager.createDumpData();
	}
}
