package com.grapeshot.halfnes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private final int port = 1337;
	private String hostIPAddress = "127.0.0.1";
	
	public void connect() {
		Socket clientSocket = null;		
		
    	try {
    		clientSocket = new Socket(hostIPAddress, port);
    	} catch (UnknownHostException e) {
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    	    if (clientSocket != null)
    	    {
    	        try {
    	        	clientSocket.close();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	    }
    	}
	}
}
