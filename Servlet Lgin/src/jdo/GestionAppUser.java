package jdo;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;

import myServlet.AppUser;
import isis.cloud.jdo.PMF;

/**
 * Servlet implementation class ListServlet
 */
@SuppressWarnings("serial")
public class GestionAppUser{
	
	public GestionAppUser() {
	}
	
	public void AddUser(AppUser myUser) {
		PersistenceManager pm = PMF.getPersistenceManager();
		pm.makePersistent(myUser);
		pm.close();
	}

}
