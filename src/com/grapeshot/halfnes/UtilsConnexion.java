package com.grapeshot.halfnes;

public class UtilsConnexion {
	private static Server server;
	private static Client client;
	private boolean m_connected = false;

    public static void setServer(Server p_server) {
    	server = p_server;
    }
    
    public static void setClient(Client p_client) {
    	client = p_client;
    }
    
    public static Server getServer() {
    	return server;
    }
    
    public static Client getClient() {
    	return client;
    }
}
