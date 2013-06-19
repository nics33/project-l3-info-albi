package utilisateurs;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmf.PMF;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
@SuppressWarnings("serial")
public class Servlet_RepAmi extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 
		 	String reponse = "";
		 	int returnValue = 0;
		 	String idFriend = "";
		 	String type = "";
		 	ArrayList<String> templist = new ArrayList<String>(); 
		 	
		 	idFriend = req.getParameter("idfriend");
		 	type = req.getParameter("type");
		 	
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        
	        if (user != null) { 
	        	
				PersistenceManager pm = PMF.get().getPersistenceManager();
		        AppUser myAppUser  = new AppUser(user);
		        AppUser myFriend = pm.getObjectById(AppUser.class, idFriend);
		        
		        //Je supprime la demande dans ma BDD
		        templist = myAppUser.getdemandeRecu();
		        templist.remove(templist.indexOf(idFriend));
		        myAppUser.ModifydemandeRecu(templist);
		        
		      //Je supprime la demande dans la BDD de mon ami
		        templist.clear();
		        templist = myFriend.getdemandeEnvoye();
		        templist.remove(templist.indexOf(myAppUser.getAppUserId()));
		        myFriend.ModifydemandeEnvoye(templist);
		        
		        //Je rajoute les id aux listes d'amis
		        if(type.equals("accept"))
		        {
		        templist.clear();
		        templist= myAppUser.getListFriends();
		        templist.add(idFriend);
		        myAppUser.Modifyfriendlist(templist);
		        
		        templist.clear();
		        templist= myFriend.getListFriends();
		        templist.add(myAppUser.getAppUserId());
		        myFriend.Modifyfriendlist(templist);
		        }
		        
		        pm.close();
	        	reponse = "{ \"status\" :" +returnValue + "}";

				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
				resp.getWriter().print(reponse);
				
	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}