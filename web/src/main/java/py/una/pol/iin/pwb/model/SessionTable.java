package py.una.pol.iin.pwb.model;

import java.util.HashMap;

public class SessionTable {
	
	private static HashMap<String, Object> sessionTable = new HashMap<String, Object>();

	public static HashMap<String, Object> getSessionTable() {
		return sessionTable;
	}

	public static void setSessionTable(HashMap<String, Object> sessionTable) {
		SessionTable.sessionTable = sessionTable;
	}
	
	public static void addSession(String sessionKey, Object carrito)
	{
		sessionTable.put(sessionKey, carrito);
	}
	
	public static Object getSession(String sessionKey)
	{
		return sessionTable.get(sessionKey);
	}
	
}
