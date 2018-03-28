package com.siemens.daac.poc.worker;



import java.io.File;

import org.apache.log4j.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.model.ROutput;
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
	public ROutput handleRCalls(RInput rinput){
		ROutput rOutput = new ROutput();
		try {
			if(rinput.getInputFilePath()!=null && !rinput.getInputFilePath().isEmpty() 
					&& rinput.getAlgorithmType()!=null && !rinput.getAlgorithmType().isEmpty()){
//				 TODO: added condtition for output path
				if(logger.isDebugEnabled())
					logger.debug("[RCodeRunner] Creating RConnection ");
				rOutput.setTransactionId(rinput.getTransactionId());
				String outputFilePath =defaultPredictionOutputLocation;
				RConnection connector = RConnector.getConnection("localhost", 6311);
				if(rinput.getOutputFilePath()!=null && !rinput.getOutputFilePath().isEmpty())
					outputFilePath =rinput.getOutputFilePath();
				outputFilePath.replaceAll(File.separator, "/");
				if(connector.isConnected()){
					if(logger.isDebugEnabled())
						logger.debug("---Successfully created RConnection ----");
					System.out.println(connector.isConnected());
					System.out.println(connector.needLogin());
					String inputFilePath =rinput.getInputFilePath();
					String rWorkSpacePath=CommonUtils.getRWorkspacePath();
					String predictionAlgoPath = rWorkSpacePath+"/prediction/NaiveBayes.R";
					predictionAlgoPath =predictionAlgoPath.replaceAll(File.separator, "/");
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
					String name ="CustomNaiveBayesFunc('"+inputFilePath+"','"+predictionDefaultLocation+"','"+outputFilePath+"')";
					REXP rFuncCallResponse = connector.parseAndEval(
							"try(eval("+name+"),silent=TRUE)");
					if (rFuncCallResponse.inherits("try-error")) {
						logger.error("Exception occurred after calling CustomNaiveBayesFunc : "+rResponseObject.asString());
						rOutput.setErrorMsg(rResponseObject.asString());
						rOutput.setStatus(ProjectConstants.FALSE);
					}
					int[] rResponseArr =rFuncCallResponse.asIntegers();
					if(rResponseArr.length>0) {
						rOutput.setTrueFloodCount(rResponseArr[0]);
						rOutput.setFalseFloodCount(rResponseArr[1]);
					}
					rOutput.setStatus(ProjectConstants.TRUE);
					return rOutput;
				}else {
					logger.error("Not able to create Connection with Rserve");
					rOutput.setErrorMsg("Connection not created with Rserve");
					rOutput.setStatus(ProjectConstants.FALSE);
					return rOutput;
				}
			}else{
				logger.error("Required InputFilePath and Algorithm Type is not defined ");
				rOutput.setStatus(ProjectConstants.FALSE);
				rOutput.setErrorMsg("Required Input Arguments are not found");
				return rOutput;
			}
		}catch(REXPMismatchException ex) {
			logger.error("REXPMismatchException :::"+ex.getMessage());
			rOutput.setStatus(ProjectConstants.FALSE);
			rOutput.setErrorMsg("REXPMismatchException occure occured :: "+ex.getMessage());
		}catch(Exception e) {
			logger.error("Exception :::"+e.getMessage());
			rOutput.setStatus(ProjectConstants.FALSE);
			rOutput.setErrorMsg("Exception occure occured    "+e.getMessage());
		}
		return rOutput;
	}

}
