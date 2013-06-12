package utilisateurs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmf.PMF;

import com.google.appengine.api.channel.ChannelFailureException;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import utilisateurs.Servlet_Disconnection;
import utilisateurs.FriendStore;

@SuppressWarnings("serial")
public class Servlet_Disconnection extends HttpServlet{
	
	/**
	 * ChannelService object to update all clients about the new logged in user
	 */
	  private static ChannelService channelService = ChannelServiceFactory.getChannelService();
		
		/**
	   * Logger to log all the updates and warnings
	   */
	  private static final Logger logger = Logger.getLogger(Servlet_Disconnection.class.getCanonicalName());
		
		/**
		 * Accepts the userid from the user and updates it 
		 */
	  
	  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			  throws ServletException, IOException {
			  
				  // déclaration des variables
				  
		  		  String userId;
				  ArrayList<String> ListeAmi;
				  String reponse;

			        System.out.println("trololo");

				  //démarage du UserService
			      UserService userService = UserServiceFactory.getUserService();
			      User user = userService.getCurrentUser();	 
			      //Si l'utilisateur est connecté a un compte Google
			      if (user != null) { 
			    	  //on récupere le UserId de l'utilisateur
				        AppUser myAppUser  = new AppUser(user);
				        userId = myAppUser.getAppUserId();
				        System.out.println(userId);
				        
				        //On ajoute l'utilisateur a la liste des Utilisateurs déja Connectés
				      	FriendStore friendStore = FriendStore.getInstance();
				      	if(friendStore.getFriends().contains(userId)){
				        	  logger.log(Level.INFO,"User {0} has been removed from list of users",userId);
				        	  friendStore.removeFriend(userId);

				          } 
				      	else{
				        	  logger.log(Level.INFO,"User {0} is absent of list of users\n" +"hence, not adding now",user);
				        	}

						// On convertit le résultat en JSON
						
			        	reponse = "{ \"status\" : 0,}";
			        	
			        	
						// On renvoie le contenu JSON
						resp.setContentType("application/json");
						resp.getWriter().println(reponse);
			        } else {
			        	reponse = "{ \"status\" : 10,\"donnees\" : \" " + userService.createLoginURL("http://ititoulouse.appspot.com/panel.html") + "\"}";
			        	resp.setContentType("application/json");
			            resp.getWriter().println(reponse);
			        }
				      	
			      }

}
