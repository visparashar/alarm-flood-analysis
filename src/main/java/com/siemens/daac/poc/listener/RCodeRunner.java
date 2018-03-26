package com.siemens.daac.poc.listener;



import org.apache.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Value;

import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.utility.CommonUtils;



public class RCodeRunner {
	
	@Value("${default-prediction-path}")
	private String defaultPredicationPath;

	private static final Logger logger = Logger.getLogger(RCodeRunner.class);

	//	TODO : To run this program you need to adde REngine and Rserve in your classpath
	public void handleRCalls(RInput rinput){

		RConnection rConnection = null;
		try {
			if(rinput.getInputFilePath()!=null && rinput.getInputFilePath().isEmpty() 
					&& rinput.getAlgorithmType()!=null && rinput.getAlgorithmType().isEmpty()){
				if(logger.isDebugEnabled())
					logger.debug("[RCodeRunner] Creating RConnection ");
				rConnection = new RConnection("localhost",6311);

				if(rConnection.isConnected()){
					if(logger.isDebugEnabled())
						logger.debug("---Successfully created RConnection ----");
					System.out.println(rConnection.isConnected());
					System.out.println(rConnection.needLogin());
					String inputFilePath =rinput.getInputFilePath();
					String rWorkSpacePath=CommonUtils.getRWorkspacePath();
					String predictionAlgoPath = rWorkSpacePath+"/prediction/NaiveBayes.R";
					if(logger.isDebugEnabled()){
						logger.debug("--inputFilePath : "+inputFilePath);
						logger.debug("predication Algo Path "+predictionAlgoPath);
					}
					if(logger.isDebugEnabled())
						logger.info("Calling the Source function of R ");
					
					String sourceRStatement ="source('"+predictionAlgoPath+"')";
//					calling the source using try eval
					REXP rResponseObject = rConnection.parseAndEval(
							 "try(eval("+sourceRStatement+"),silent=TRUE)");
					if (rResponseObject.inherits("try-error")) {
						logger.error("R Serve Eval Exception : "+rResponseObject.asString());
					}
					
//					rConnection.eval("source('C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction/NaiveBayes.R')");
//					String inputFilePath ="C:/Workspace_alarmflood/alarm-food-analysis/merged_file";
					
					String name ="CustomNaiveBayesFunc('"+inputFilePath+"',defaultPredicationPath)";
					System.out.println(name);
					REXP rFuncCallResponse = rConnection.parseAndEval(
							 "try(eval("+name+"),silent=TRUE)");
					
					if (rFuncCallResponse.inherits("try-error")) {
						logger.error("Exception occurred after calling CustomNaiveBayesFunc : "+rResponseObject.asString());
					}
//					rConnection.eval("CustomNaiveBayesFunc('"+inputFilePath+"','C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction')");
					//		System.out.println(result);
				}
			}else{

			}


		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
