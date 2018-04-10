package com.siemens.daac.poc.model;

import java.io.Serializable;

public class ROutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int trueFloodCount;
	private int falseFloodCount;
	private String status;
	private String errorMsg;
	private String transactionId;
	private String algoType;
	private Object rResponse;
	public int getTrueFloodCount() {
		return trueFloodCount;
	}
	public void setTrueFloodCount(int trueFloodCount) {
		this.trueFloodCount = trueFloodCount;
	}
	public int getFalseFloodCount() {
		return falseFloodCount;
	}
	public void setFalseFloodCount(int falseFloodCount) {
		this.falseFloodCount = falseFloodCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getAlgoType() {
		return algoType;
	}
	public void setAlgoType(String algoType) {
		this.algoType = algoType;
	}
	
	public Object getrResponse() {
		return rResponse;
	}
	public void setrResponse(Object rResponse) {
		this.rResponse = rResponse;
	}
	@Override
	public String toString() {
		return "ROutput [trueFloodCount=" + trueFloodCount + ", falseFloodCount=" + falseFloodCount + ", status="
				+ status + ", errorMsg=" + errorMsg + ", transactionId=" + transactionId + "]";
	}



}
