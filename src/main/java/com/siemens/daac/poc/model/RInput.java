package com.siemens.daac.poc.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RInput implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String transactionId;
	private String inputFilePath;
	private String algorithmType;
	private String outputFilePath;
	private boolean isRunForTraining;
	private String rWorkSpacePath;
	private String secondInputFilePath;
	private String patternMiningFilePath;
	private String clusterResultFilePath;
	private String mergedResultFilePath;
	private String testRcaFilePath;
	private String frequencyCountFilePath;
	private String otherRequiredPath;
	
	
	public RInput() {
	}
	
	public RInput(String transactionId, String inputFilePath, String algorithmType, String outputFilePath,
			String status) {
		super();
		this.transactionId = transactionId;
		this.inputFilePath = inputFilePath;
		this.algorithmType = algorithmType;
		this.outputFilePath = outputFilePath;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getInputFilePath() {
		return inputFilePath;
	}
	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}
	public String getAlgorithmType() {
		return algorithmType;
	}
	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}
	public String getOutputFilePath() {
		return outputFilePath;
	}
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
	
	public boolean isRunForTraining() {
		return isRunForTraining;
	}

	public void setRunForTraining(boolean isRunForTraining) {
		this.isRunForTraining = isRunForTraining;
	}
	
	public String getrWorkSpacePath() {
		return rWorkSpacePath;
	}

	public void setrWorkSpacePath(String rWorkSpacePath) {
		this.rWorkSpacePath = rWorkSpacePath;
	}

	public String getSecondInputFilePath() {
		return secondInputFilePath;
	}

	public void setSecondInputFilePath(String secondInputFilePath) {
		this.secondInputFilePath = secondInputFilePath;
	}

	public String getPatternMiningFilePath() {
		return patternMiningFilePath;
	}

	public void setPatternMiningFilePath(String patternMiningFilePath) {
		this.patternMiningFilePath = patternMiningFilePath;
	}

	public String getClusterResultFilePath() {
		return clusterResultFilePath;
	}

	public void setClusterResultFilePath(String clusterResultFilePath) {
		this.clusterResultFilePath = clusterResultFilePath;
	}

	public String getMergedResultFilePath() {
		return mergedResultFilePath;
	}

	public void setMergedResultFilePath(String mergedResultFilePath) {
		this.mergedResultFilePath = mergedResultFilePath;
	}

	public String getTestRcaFilePath() {
		return testRcaFilePath;
	}

	public void setTestRcaFilePath(String testRcaFilePath) {
		this.testRcaFilePath = testRcaFilePath;
	}

	public String getFrequencyCountFilePath() {
		return frequencyCountFilePath;
	}

	public void setFrequencyCountFilePath(String frequencyCountFilePath) {
		this.frequencyCountFilePath = frequencyCountFilePath;
	}
	
	
	public String getOtherRequiredPath() {
		return otherRequiredPath;
	}

	public void setOtherRequiredPath(String otherRequiredPath) {
		this.otherRequiredPath = otherRequiredPath;
	}

	@Override
	public String toString() {
		return "RInput [transactionId=" + transactionId + ", inputFilePath=" + inputFilePath + ", algorithmType="
				+ algorithmType + ", outputFilePath=" + outputFilePath + ", isRunForTraining=" + isRunForTraining
				+ ", rWorkSpacePath=" + rWorkSpacePath + ", secondInputFilePath=" + secondInputFilePath
				+ ", patternMiningFilePath=" + patternMiningFilePath + ", clusterResultFilePath="
				+ clusterResultFilePath + ", mergedResultFilePath=" + mergedResultFilePath + ", testRcaFilePath="
				+ testRcaFilePath + ", frequencyCountFilePath=" + frequencyCountFilePath + ", otherRequiredPath="
				+ otherRequiredPath + "]";
	}

	
	
	
	
	

}
