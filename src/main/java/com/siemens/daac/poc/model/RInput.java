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
	
	@Override
	public String toString() {
		return "RInput [transactionId=" + transactionId + ", inputFilePath=" + inputFilePath + ", algorithmType="
				+ algorithmType + ", outputFilePath=" + outputFilePath +"]";
	}
	
	
	
	

}
