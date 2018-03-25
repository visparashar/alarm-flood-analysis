package com.siemens.daac.poc.worker;

import org.rosuda.REngine.Rserve.RConnection;

public class RunRCode {
	
	
	public static void main(String[] args) {
		RConnection rConnection = null;
		try {
			rConnection = new RConnection("localhost",6311);
			System.out.println(rConnection.isConnected());
			System.out.println(rConnection.needLogin());
			rConnection.eval("source('C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction/NaiveBayes.R')");
		String inputFilePath ="C:/Workspace_alarmflood/alarm-food-analysis/merged_file";
		String name ="CustomNaiveBayesFunc('"+inputFilePath+"','C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction')";
		System.out.println(name);
		rConnection.eval("CustomNaiveBayesFunc('"+inputFilePath+"','C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction')");
//		System.out.println(result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
