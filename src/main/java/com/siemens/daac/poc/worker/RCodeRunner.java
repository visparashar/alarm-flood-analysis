package com.siemens.daac.poc.worker;



import java.io.File;

import org.apache.log4j.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.utility.CommonUtils;
import com.siemens.daac.poc.utility.RConnector;


@Component
public class RCodeRunner {

	@Value("${r-prediction-folder-location}")
	private String predictionDefaultLocation;

	@Value("${r-prediction-output-folder-location}")
	private String defaultPredictionOutputLocation;
	private static final Logger logger = Logger.getLogger(RCodeRunner.class);

	//	TODO : To run this program you need to adde REngine and Rserve in your classpath
	public void handleRCalls(RInput rinput){

		//		RConnection rConnection = null;
		try {
			if(rinput.getInputFilePath()!=null && !rinput.getInputFilePath().isEmpty() 
					&& rinput.getAlgorithmType()!=null && !rinput.getAlgorithmType().isEmpty()){
				if(logger.isDebugEnabled())
					logger.debug("[RCodeRunner] Creating RConnection ");
				String outputFilePath =defaultPredictionOutputLocation;
				RConnection connector = RConnector.getConnection("localhost", 6311);
				if(rinput.getOutputFilePath()!=null && rinput.getOutputFilePath().isEmpty())
					outputFilePath =rinput.getOutputFilePath();
				outputFilePath.replace(File.separator, "/");
				if(connector.isConnected()){
					if(logger.isDebugEnabled())
						logger.debug("---Successfully created RConnection ----");
					System.out.println(connector.isConnected());
					System.out.println(connector.needLogin());
					String inputFilePath =rinput.getInputFilePath();
					String rWorkSpacePath=CommonUtils.getRWorkspacePath();
					String predictionAlgoPath = rWorkSpacePath+"/prediction/NaiveBayes.R";
					predictionAlgoPath =predictionAlgoPath.replace(File.separator, "/");
					if(logger.isDebugEnabled()){
						logger.debug("--inputFilePath : "+inputFilePath);
						logger.debug("predication Algo Path "+predictionAlgoPath);
					}
					if(logger.isDebugEnabled())
						logger.info("Calling the Source function of R ");

					String sourceRStatement ="source('"+predictionAlgoPath+"')";
					//					calling the source using try eval
					REXP rResponseObject = connector.parseAndEval(
							"try(eval("+sourceRStatement+"),silent=TRUE)");
					if (rResponseObject.inherits("try-error")) {
						logger.error("R Serve Eval Exception : "+rResponseObject.asString());
					}

					//					rConnection.eval("source('C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction/NaiveBayes.R')");
					//					String inputFilePath ="C:/Workspace_alarmflood/alarm-food-analysis/merged_file";

					String name ="CustomNaiveBayesFunc('"+inputFilePath+"','"+predictionDefaultLocation+"','"+outputFilePath+"')";
					System.out.println(name);
					REXP rFuncCallResponse = connector.parseAndEval(
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
