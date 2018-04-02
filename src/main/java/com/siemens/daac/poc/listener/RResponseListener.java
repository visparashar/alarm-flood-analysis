package com.siemens.daac.poc.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.ROutput;
import com.siemens.daac.poc.utility.CSVReaderUtil;
@Component
public class RResponseListener {


	@Value("${mergedFilePath}")
	String trainingSetLocation;

	@JmsListener(destination =ProjectConstants.R_RESPONSE_QUEUE)
	public void handleRResponse(final ROutput rOutput){
		//	syso
		CSVReaderUtil.trueCount.addAndGet(rOutput.getTrueFloodCount());
		CSVReaderUtil.falseCount.addAndGet(rOutput.getFalseFloodCount());
		//		TODO: Need to call the R worker from here and nee dto check the flow as well
		System.out.println("recieved message "+rOutput);
		//		rCodeRunner.handleRCalls(rInput);
		//		return rInput.toString();
		//		if(rOutput!=null && rOutput.getStatus().equalsIgnoreCase(ProjectConstants.TRUE))
		//		{
		//			String path =
		//			CSVReaderUtil.processInputFile(inputFilePath)
		//		}
		//		return rOutput.toString();

	}
}
