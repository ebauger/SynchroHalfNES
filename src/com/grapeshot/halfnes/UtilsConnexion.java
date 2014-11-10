package com.grapeshot.halfnes;

public class UtilsConnexion {
	private static boolean m_connected = false;

	public static boolean getConnected() {
		return m_connected;
	}
	
	public static void setConnected(boolean p_connected) {
		m_connected = p_connected;
	}
}
