package com.siemens.daac.poc.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;
@Component
public class RResponseListener {

	
	@JmsListener(destination =ProjectConstants.R_RESPONSE_QUEUE)
	public void handleRResponse(final RInput rInput){
//	syso
		
//		TODO: Need to call the R worker from here and nee dto check the flow as well
//		System.out.println("recieved message "+rInput);
//		rCodeRunner.handleRCalls(rInput);
//		return rInput.toString();
		System.out.println(rInput);
//		return rInput.toString();
		
	}
}
