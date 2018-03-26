package com.siemens.daac.poc.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.service.RManager;

@RestController
@RequestMapping("/rest")
public class CreateMessageRestController {

	@Autowired
	RManager manager;
	
	
//	Just for creating the sample input and make sample output for r testing
	@PostMapping
	public Boolean createDumbRequest(@RequestBody RInput input
			){
		
		return manager.createDumpData(input);
	}
}
