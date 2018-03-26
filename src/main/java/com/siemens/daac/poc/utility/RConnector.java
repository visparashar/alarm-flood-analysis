package com.siemens.daac.poc.utility;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RConnector {
	
	private static RConnection rConnection =null;
	
	public static RConnection getConnection(String host,int port) throws RserveException {
		if(rConnection !=null) {
			return rConnection;
		}else {
			rConnection = new RConnection(host,port);
			return rConnection;
		}
	}
}
