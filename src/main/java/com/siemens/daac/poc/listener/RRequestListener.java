package com.siemens.daac.poc.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.model.ROutput;
import com.siemens.daac.poc.worker.RCodeRunner;

@Component
public class RRequestListener {
	
	
	@Autowired
	RCodeRunner rCodeRunner;
	
	@JmsListener(destination =ProjectConstants.R_QUEUE)
	@SendTo(ProjectConstants.R_RESPONSE_QUEUE)
	public ROutput handleRCalls(final RInput rInput){
//	syso
//		TODO: Need to call the R worker from here and nee dto check the flow as well
		System.out.println("recieved message at R Listener for  "+rInput.getAlgorithmType());
//		TODO: Need to create one ROutput  POJO	
		
		ROutput rOutput = rCodeRunner.handleRCalls(rInput);
		return rOutput;
		
	}

}
