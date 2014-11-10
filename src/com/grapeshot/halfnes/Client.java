package com.grapeshot.halfnes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private final int port = 1337;
	private String hostIPAddress = "127.0.0.1";
	private Socket socket              = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	
	public void start() throws IOException {
		console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
	}

	public void stop() {
		try {
			if (console != null)
				console.close();
			if (streamOut != null)
				streamOut.close();
			if (socket != null)
				socket.close();
		} catch (IOException ioe) {
			System.out.println("Error closing ...");
		}
	}	
	
	public void connect() {	
    	DataInputStream is = null;
    	DataOutputStream os = null;
    	DataInputStream stdIn = new DataInputStream(System.in);
				
    	try {
    		socket = new Socket(hostIPAddress, port);

    		start();
	    	
	    	System.out.println("Accepted connection : " + socket);     	    
    	    
			String line = "";
			while (!line.equals(".bye")) {
				try {
					line = console.readLine();
					streamOut.writeUTF(line);
					streamOut.flush();
				} catch (IOException ioe) {
					System.out.println("Sending error: " + ioe.getMessage());
				}
			}		
    		
    	} catch (UnknownHostException e) {
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    		stop();
    	}
	}
}