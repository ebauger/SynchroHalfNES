package com.grapeshot.halfnes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final int port = 1337;
	private Socket socket = null;
	
	public void openSocket() {
    	// Information de connexion 
    	String hostName = "127.0.0.1";
    	ServerSocket server = null;
    	
    	try {
    	    server = new ServerSocket(port);
    	    socket = server.accept();
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
