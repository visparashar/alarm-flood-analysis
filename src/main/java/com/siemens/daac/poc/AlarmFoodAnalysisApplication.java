package com.siemens.daac.poc;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.siemens.daac.poc.service.RManager;
import com.siemens.daac.poc.service.StorageService;

@SpringBootApplication
@EnableJms
public class AlarmFoodAnalysisApplication implements CommandLineRunner {

	@Autowired
	RManager manager;

	@Resource
	StorageService storeService;


	public static void main(String[] args) {
		SpringApplication.run(AlarmFoodAnalysisApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		storeService.archievAllFiles();
//		storeService.deleteAllFiles();
//		storeService.deleteAll(null);
		storeService.deleteOldPreRequesFiles();
		storeService.init();



	}




}
