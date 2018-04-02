package com.siemens.daac.poc.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;

@Component
public class RCaller  {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public boolean sendToR(RInput rInput) {
		
		jmsTemplate.convertAndSend(ProjectConstants.R_QUEUE, rInput);
		return true;
		
	}
	
}
