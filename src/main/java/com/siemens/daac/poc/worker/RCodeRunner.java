package com.siemens.daac.poc.worker;



import org.apache.logging.log4j.LogManager;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.model.ROutput;
import com.siemens.daac.poc.utility.RConnector;


@Component
public class RCodeRunner {

	@Value("${r-prediction-output-folder-location}")
	private String defaultPredictionOutputLocation;

	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();
	//	TODO : To run this program you need to adde REngine and Rserve in your classpath
	public ROutput handleRCalls(RInput rinput){
		ROutput rOutput = new ROutput();
		try {
			if(checkInput(rinput)) {
				if(logger.isDebugEnabled())
					logger.debug("[RCodeRunner] Creating RConnection ");

				String outputFilePath =defaultPredictionOutputLocation;
				RConnection connector = RConnector.getConnection("localhost", 6311);
				if(rinput.getOutputFilePath()!=null && !rinput.getOutputFilePath().isEmpty())
					outputFilePath =rinput.getOutputFilePath();
				outputFilePath=outputFilePath.replaceAll("\\\\", "/");
				if(connector.isConnected()){
					if(logger.isDebugEnabled())
						logger.debug("---Successfully created RConnection ----");
					rOutput= runRCall(rinput);
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
		}catch(Exception e) {
			logger.error("Exception :::"+e.getMessage());
			rOutput.setStatus(ProjectConstants.FALSE);
			rOutput.setErrorMsg("Exception occure occured    "+e.getMessage());
		}
		System.out.println("--------------"+rOutput+"-----------------");
		return rOutput;
	}

	public ROutput runRCall(RInput rInput) {
		ROutput rOutput = new ROutput();
		try {
			RConnection connector = RConnector.getConnection("localhost", 6311);
			rOutput.setTransactionId(rInput.getTransactionId());
			String generalRCodeRunner = rInput.getrWorkSpacePath()+"/Rcode.R";
			generalRCodeRunner =generalRCodeRunner.replaceAll("\\\\", "/");
			if(logger.isDebugEnabled()){
				logger.debug("--inputFilePath : "+rInput.getInputFilePath());
				logger.debug("Algo Path "+generalRCodeRunner);
			}
			if(logger.isDebugEnabled())
				logger.info("Calling the Source function of R ");
			String sourceRStatement ="source('"+generalRCodeRunner+"')";
			logger.info("Running Source Command as "+sourceRStatement);
			//					calling the source using try eval
			REXP rResponseObject = connector.parseAndEval(
					"try(eval("+sourceRStatement+"),silent=TRUE)");
			if (rResponseObject.inherits("try-error")) {
				logger.error("R Serve Eval Exception : "+rResponseObject);
			}
			String algoPath =getAlgoPath(rInput.getAlgorithmType());
			logger.info("Running Algo ----------- "+algoPath);
			if(algoPath.equals("notFound")) {
				rOutput.setStatus(ProjectConstants.FALSE);
				rOutput.setErrorMsg("The Algotype defined is invalid");
				return rOutput;
			}
			rOutput.setAlgoType(rInput.getAlgorithmType());
			//			if(rInput.getAlgorithmType().equals(ProjectConstants.CONST_PREFILTER_ALGO) && rInput.getAlgorithmType() &&) 
			String name ="callRFunction('"+rInput.getInputFilePath()+"','"+rInput.getOutputFilePath()+"','"+rInput.getrWorkSpacePath()+algoPath
					+ "','"+rInput.getAlgorithmType()+"','"+rInput.isRunForTraining()+"','"+rInput.getrWorkSpacePath()+"','"+rInput.getSecondInputFilePath()+
					"','"+rInput.getPatternMiningFilePath()+"','"+rInput.getClusterResultFilePath()+"','"+rInput.getMergedResultFilePath()+"','"+rInput.getTestRcaFilePath()+
					"','"+rInput.getFrequencyCountFilePath()+"','"+rInput.getOtherRequiredPath()+"' )";
			if(logger.isDebugEnabled())
				logger.debug("The Calling function is for Algo is "+name);
			REXP rFuncCallResponse = connector.parseAndEval(
					"try(eval("+name+"),silent=TRUE)");
			if (rFuncCallResponse.inherits("try-error")) {
				logger.error("Exception occurred after calling callRFunction : "+rResponseObject);
				rOutput.setErrorMsg(rResponseObject.asString());
				rOutput.setStatus(ProjectConstants.FALSE);
			}
			rOutput.setRunForTraining(rInput.isRunForTraining());
			if(rInput.getAlgorithmType().equalsIgnoreCase(ProjectConstants.R_PREDICTION_ALGO) && !rInput.isRunForTraining())
			{
				int[] rResponseArr =rFuncCallResponse.asIntegers();
				if(rResponseArr.length>0) {
					rOutput.setTrueFloodCount(rResponseArr[0]);
					rOutput.setFalseFloodCount(rResponseArr[1]);
				}
				rOutput.setStatus(ProjectConstants.TRUE);
				return rOutput;
			}
			if(rInput.getAlgorithmType().equalsIgnoreCase(ProjectConstants.R_PREDICTION_ALGO) && rInput.isRunForTraining()) {
				rOutput.setStatus(ProjectConstants.FALSE);
			}else {
				rOutput.setStatus(ProjectConstants.TRUE);
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


	public boolean checkInput(RInput rInput) {
		if(rInput!=null)
		{
			if(!StringUtils.isEmpty(rInput.getInputFilePath()) && !StringUtils.isEmpty(rInput.getAlgorithmType())
					&& !StringUtils.isEmpty(rInput.getrWorkSpacePath())) {
				logger.debug("Got Required Inputs for R to be run ");
				return true;
			}
		}
		return false;

	}

	public String getAlgoPath(String algoType) {
		switch(algoType) {

		case ProjectConstants.R_PREDICTION_ALGO :{
			return "/prediction";
		}
		case ProjectConstants.CONST_PREFILTER_ALGO :{
			return "/prefilter";
		}
		case ProjectConstants.CONST_MSW_CLUSTER_ALSO :{
			return "/msw_cluster";
		}
		case ProjectConstants.CONST_TEST_RCA_ALGO :{
			return "/test_rca";
		}

		default:{
			return "notFound";
		}
		}
	}


}
