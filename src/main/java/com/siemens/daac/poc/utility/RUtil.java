package com.siemens.daac.poc.utility;

import java.io.File;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;

public class RUtil {
	
	private static final String defaultWorkspace = System.getProperty("user.dir");
	
	public static boolean doPrefilterPrerequistes() {
//		create required folder of prefilter
		File inputFileForMWClusterDir = new File(CommonUtils.readProperty("r-prefilter-output-for-mwcluster-location"));
		File similarityMatrxiPath = new File(CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location"));
		if(!similarityMatrxiPath.exists())
			 similarityMatrxiPath.mkdirs();
		if(!inputFileForMWClusterDir.exists())
			return inputFileForMWClusterDir.mkdirs();
		return true;
		
	}
	
	public static boolean doMSWClusterPrerequistes() {
//		create required input folder
		File outPutFilePrefilter = new File(CommonUtils.readProperty("r-mswcluster-intermediate-output-folder-location"));
		if(!outPutFilePrefilter.exists())
			 outPutFilePrefilter.mkdirs();
		File similarityMatrxiPath = new File(CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location"));
		if(!similarityMatrxiPath.exists())
			similarityMatrxiPath.mkdirs();
		File clusterPath = new File(CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location")+"/clusters");
		if(!clusterPath.exists()){
			clusterPath.mkdirs();
		}
		File patternMiningFilePath = new File(CommonUtils.readProperty("r-frequencycount-patternmining-folder-location"));
		if(!patternMiningFilePath.exists()){
			patternMiningFilePath.mkdirs();
		}
		File mergeResultFilePath = new File(CommonUtils.readProperty("r-frequencycount-mergedresult-folder-location"));
		if(!mergeResultFilePath.exists()){
			mergeResultFilePath.mkdirs();
		}
		File testRcaInputFilePath = new File(CommonUtils.readProperty("r-testrca-input-folder-location"));
		if(!testRcaInputFilePath.exists()){
			testRcaInputFilePath.mkdirs();
		}
		File frequencyCountOutputPath = new File(CommonUtils.readProperty("r-frequencycount-output-folder-location"));
		if(!frequencyCountOutputPath.exists()){
			return frequencyCountOutputPath.mkdirs();
		}
		return true;
		
	}
	
	
	public static RInput prepareRInputForAlgo(String algotype, boolean forTrainingSet) throws Exception {

		RInput rinput= null;
		if(algotype!=null && !algotype.isEmpty()) {
			switch(algotype) {
			case ProjectConstants.R_PREDICTION_ALGO:{
				break;
			}
			case ProjectConstants.CONST_PREFILTER_ALGO :{
				rinput = new RInput();
				rinput.setAlgorithmType(ProjectConstants.CONST_PREFILTER_ALGO);
				String inputFilePath =CommonUtils.readProperty("prefilter-input-folder");
				inputFilePath =defaultWorkspace+"/"+inputFilePath+"/"+ProjectConstants.TRUE_FLOOD_PATH;
				inputFilePath= inputFilePath.replaceAll("\\\\", "/");
				rinput.setInputFilePath(inputFilePath);
				String outputFilePath = CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location");
				outputFilePath=defaultWorkspace+"/"+outputFilePath;
				outputFilePath = outputFilePath.replaceAll("\\\\", "/");
				rinput.setOutputFilePath(outputFilePath);
				String secondInputPath = defaultWorkspace+"/"+CommonUtils.readProperty("r-prefilter-output-for-mwcluster-location");
				secondInputPath=secondInputPath.replaceAll("\\\\", "/");
				rinput.setSecondInputFilePath(secondInputPath);
				rinput.setRunForTraining(forTrainingSet);
				String rWorkspacePath = CommonUtils.getRWorkspacePath();
				rWorkspacePath =rWorkspacePath.replaceAll("\\\\", "/");
				rinput.setrWorkSpacePath(rWorkspacePath);
				
//				added for frequency pattern mining
				String patternMiningPath = defaultWorkspace+"/"+CommonUtils.readProperty("r-frequencycount-patternmining-folder-location");
				patternMiningPath = patternMiningPath.replaceAll("\\\\", "/");
				rinput.setPatternMiningFilePath(patternMiningPath);
			
				String mergedresultFilePath = defaultWorkspace+"/"+CommonUtils.readProperty("r-frequencycount-mergedresult-folder-location");
				mergedresultFilePath = mergedresultFilePath.replaceAll("\\\\", "/");
				rinput.setMergedResultFilePath(mergedresultFilePath);
			
				String clusterResultFilePath = defaultWorkspace+"/"+CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location")+"/clusters";
				clusterResultFilePath = clusterResultFilePath.replaceAll("\\\\", "/");
				rinput.setClusterResultFilePath(clusterResultFilePath);
				
				String testRcaInputFile = defaultWorkspace+"/"+CommonUtils.readProperty("r-testrca-input-folder-location");
				testRcaInputFile = testRcaInputFile.replaceAll("\\\\", "/");
				rinput.setTestRcaFilePath(testRcaInputFile);
				
				String frequencyCountoutputFile = defaultWorkspace+"/"+CommonUtils.readProperty("r-frequencycount-output-folder-location");
				frequencyCountoutputFile = frequencyCountoutputFile.replaceAll("\\\\", "/");
				rinput.setFrequencyCountFilePath(frequencyCountoutputFile);
				return rinput;
			}
			case ProjectConstants.CONST_MSW_CLUSTER_ALSO :{

				rinput = new RInput();
				rinput.setAlgorithmType(ProjectConstants.CONST_MSW_CLUSTER_ALSO);
				String inputFilePath = defaultWorkspace+"/"+CommonUtils.readProperty("r-prefilter-output-for-mwcluster-location");
				inputFilePath=inputFilePath.replaceAll("\\\\", "/");
				rinput.setInputFilePath(inputFilePath);
				String inputFilePathForDataSet = defaultWorkspace+"/"+CommonUtils.readProperty("r-mswcluster-intermediate-output-folder-location");
				inputFilePathForDataSet = inputFilePathForDataSet.replaceAll("\\\\", "/");
				rinput.setSecondInputFilePath(inputFilePathForDataSet);
				String outputFilePath = defaultWorkspace+"/"+CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location");
				outputFilePath = outputFilePath.replaceAll("\\\\", "/");
				rinput.setOutputFilePath(outputFilePath);
				rinput.setRunForTraining(forTrainingSet);
				String rWorkspace = CommonUtils.getRWorkspacePath();
				rWorkspace =rWorkspace.replaceAll("\\\\", "/");
				rinput.setrWorkSpacePath(rWorkspace);
				String patternMiningPath = defaultWorkspace+"/"+CommonUtils.readProperty("r-frequencycount-patternmining-folder-location");
				patternMiningPath = patternMiningPath.replaceAll("\\\\", "/");
				rinput.setPatternMiningFilePath(patternMiningPath);
			
				String mergedresultFilePath = defaultWorkspace+"/"+CommonUtils.readProperty("r-frequencycount-mergedresult-folder-location");
				mergedresultFilePath = mergedresultFilePath.replaceAll("\\\\", "/");
				rinput.setMergedResultFilePath(mergedresultFilePath);
			
				String clusterResultFilePath = defaultWorkspace+"/"+CommonUtils.readProperty("r-mswcluster-similarity-matrix-folder-location")+"/clusters";
				clusterResultFilePath = clusterResultFilePath.replaceAll("\\\\", "/");
				rinput.setClusterResultFilePath(clusterResultFilePath);
				
				String testRcaInputFile = defaultWorkspace+"/"+CommonUtils.readProperty("r-testrca-input-folder-location");
				testRcaInputFile = testRcaInputFile.replaceAll("\\\\", "/");
				rinput.setTestRcaFilePath(testRcaInputFile);
				
				String frequencyCountoutputFile = defaultWorkspace+"/"+CommonUtils.readProperty("r-frequencycount-output-folder-location");
				frequencyCountoutputFile = frequencyCountoutputFile.replaceAll("\\\\", "/");
				rinput.setFrequencyCountFilePath(frequencyCountoutputFile);
				String sourceOfFrequencyFile = CommonUtils.getRWorkspacePath()+"/"+ProjectConstants.FREQUENCY_COUNT_RFILE_PATH;
				sourceOfFrequencyFile =sourceOfFrequencyFile.replaceAll("\\\\", "/");
				rinput.setOtherRequiredPath(sourceOfFrequencyFile);
				return rinput;
			}
			default:{
				System.out.println("algotype is invalid");
				return null;
			}
			}

		}else {
			System.out.println("algotype is null");
			throw new Exception("Algotype is null");
		}
		return rinput;
	}
	

}
