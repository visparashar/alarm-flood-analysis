package com.siemens.daac.poc.listener;

import java.io.File;
import java.io.IOException;

import javax.jms.JMSException;

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
	public void handleRResponse(final ROutput rOutput) throws JMSException{
		//	syso
		System.out.println("Got Response of the R Algorithm with Transaction Id "+rOutput.getTransactionId()
		+" and Status "+rOutput.getStatus());
		if(rOutput.getStatus()!= null &&rOutput.getStatus().equalsIgnoreCase(ProjectConstants.TRUE)){
			if(rOutput.getAlgoType().equals(ProjectConstants.R_PREDICTION_ALGO)) {
			CSVReaderUtil.trueCount.addAndGet(rOutput.getTrueFloodCount());
			CSVReaderUtil.falseCount.addAndGet(rOutput.getFalseFloodCount());
			//		TODO: Need to call the R worker from here and nee dto check the flow as well
			System.out.println(CSVReaderUtil.trueCount);
			System.out.println(CSVReaderUtil.falseCount);
			System.out.println("recieved message "+rOutput);
			File f = new File("Check_For_R_Call_Get_Completed.txt");
			try {
				f.createNewFile();
			}catch(IOException e) {
				System.out.println("problem occured while creating file for prediction");
				e.printStackTrace();
			}
			}else if(rOutput.getAlgoType().equals(ProjectConstants.CONST_MSW_CLUSTER_ALSO)) {
				File f = new File("check_for_mswcluster_complete.txt");
				try {
					f.createNewFile();
					
				}catch(IOException e) {
					System.out.println("problem occured while creating file for mswcluster");
					e.printStackTrace();
				}
				
			}else {
				System.out.println("got message for prefilter");
			}
		}

	}
}
