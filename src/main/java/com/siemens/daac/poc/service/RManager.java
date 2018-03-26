package com.siemens.daac.poc.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.worker.RCaller;

@Service
public class RManager {
	
	@Autowired
	RCaller rCaller ;
	
	
	public  boolean sendToQueueForRExecution(RInput input) {
		
//		String inputFilePath = "C:/Workspace_alarmflood/alarm-food-analysis/merged_file";
		
		String transactionId = UUID.randomUUID().toString();
		input.setTransactionId(transactionId);
//		RInput rInput = new RInput(transactionId, inputFilePath, ProjectConstants.R_PREDICTION_ALGO, "","true");
		return rCaller.sendToR(input);
	}
	

}
