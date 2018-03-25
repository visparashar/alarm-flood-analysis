package com.siemens.daac.poc.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;

@Component
public class RListener {
	
	@JmsListener(destination =ProjectConstants.R_QUEUE)
	@SendTo(ProjectConstants.R_RESPONSE_QUEUE)
	public String handleRCalls(final RInput rInput){
//	syso
		
//		TODO: Need to call the R worker from here and nee dto check the flow as well
		System.out.println("recieved message "+rInput);
		
		return rInput.toString();
		
		
	}

}
