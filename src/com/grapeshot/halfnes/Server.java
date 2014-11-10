package com.grapeshot.halfnes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final int port = 1337;
	private Socket socket = null;
	private DataInputStream is = null;
	private ServerSocket server = null;
	
	public void open() throws IOException {
		is = new DataInputStream(new BufferedInputStream(
				socket.getInputStream()));
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (is != null)
			is.close();
	}
	
	public void openSocket() {
    	// Information de connexion 
    	String hostName = "127.0.0.1";
    	server = null;
    	DataInputStream dis = null;
    	
    	try {
    	    server = new ServerSocket(port);
    	    
	    	socket = server.accept();
	    	open();
	    	
			boolean done = false;
			
			while (!done) {
				try {
					String line = is.readUTF();
					System.out.println(line);
					done = line.equals(".bye");
				} catch (IOException ioe) {
					done = true;
				}
			}
    	    
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    		try {
				socket.close();
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
}
