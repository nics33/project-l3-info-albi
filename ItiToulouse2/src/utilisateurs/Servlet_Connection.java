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

import utilisateurs.Servlet_Connection;
import utilisateurs.FriendStore;

@SuppressWarnings("serial")
public class Servlet_Connection extends HttpServlet{
	
	/**
	 * ChannelService object to update all clients about the new logged in user
	 */
  private static ChannelService channelService = ChannelServiceFactory.getChannelService();
	
	/**
   * Logger to log all the updates and warnings
   */
  private static final Logger logger = Logger.getLogger(Servlet_Connection.class.getCanonicalName());
	
	/**
	 * Accepts the userid from the user and updates it 
	 */
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
  throws ServletException, IOException {
  
	  // déclaration des variables
	  
	  Integer admin;
	  String userId;
	  ArrayList<String> ListeAmi;
	  ArrayList<String> returnValue = new ArrayList<String>(); 
	  String temp;
	  String reponse;

	  
	  //démarage du UserService
      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();	 
      //Si l'utilisateur est connecté a un compte Google
      if (user != null) { 
    	  //on récupere le UserId de l'utilisateur
      	if(userService.isUserAdmin()) admin = 1; else admin = 0;
	        AppUser myAppUser  = new AppUser(user);
	        userId = myAppUser.getAppUserId();
	        ListeAmi = myAppUser.getListFriends();
	        
	        //On ajoute l'utilisateur a la liste des Utilisateurs déja Connectés
	      	FriendStore friendStore = FriendStore.getInstance();
	      	if(!friendStore.getFriends().contains(userId)){
	        	  logger.log(Level.INFO,"User {0} is added to list of users",userId);
	        	  friendStore.addNewFriend(userId);

	          } 
	      	else{
	        	  logger.log(Level.INFO,"User {0} is already added  to list of users\n" +"hence, not adding now",user);
	        	}
	      	
	      	//On Connecte l'utilisateur au Channel
	        String token = createChannel(userId);
	      	
	      	//Il faut maintenant regarder qu'elles amis sont déja connectés à l'application
	        Iterator<String> friendList = ListeAmi.iterator();

	        while(friendList.hasNext()){
	        	  String friend = friendList.next() ;
	            if(friendStore.getFriends().contains(friend)) //Si le friendstore contient mon Ami(friend = id de l'ami)
	            {
		    		PersistenceManager pm = PMF.get().getPersistenceManager();
		    		AppUser myUserTemp = pm.getObjectById(AppUser.class, friend); // j'obtiens ses informations dans la base de donnée
		    		pm.close();
	    			temp = "{ \"email\" : \"" +myUserTemp.getEmail()+ "\","+ myUserTemp.getLieu()+","+myUserTemp.getUserLocalisation()+"}";
	    			returnValue.add(temp); // j'ajoute ses information dans un tableau
	    			
	              channelService.sendMessage(
	        		  new ChannelMessage(friend,"<data>" +
	        			"<type>updateFriendList</type>" +
	        			"<message>"+userId+"</message>" +
	        			"<from>Server</from>" +	"</data>")); // et j'indique a mon ami que je me suis connecté
	        	  }

	          }
			// On convertit le résultat en JSON
			
        	reponse = "{ \"status\" : 0,\"admin\" : "+admin.toString()+",\"token\" : "+token+",\"donnees\" :  " +returnValue + "}";
        	
        	
			// On renvoie le contenu JSON
			resp.setContentType("application/json");
			resp.getWriter().print(reponse);
        } else {
        	reponse = "{ \"status\" : 10,\"donnees\" : \" " + userService.createLoginURL("http://ititoulouse.appspot.com/panel.html") + "\"}";
        	resp.setContentType("application/json");
            resp.getWriter().println(reponse);
        }
	      	
      }
	/**
	 * Creates the Channel token for the user 
	 * @param userId The User whom the token is created for  
	 * @return The token string is returned
	 */
public String createChannel(String userId){
  try{
    logger.log(Level.INFO, "Creating a channel for {0}",userId);
    return channelService.createChannel(userId);
  } catch(ChannelFailureException channelFailureException){
    logger.log(Level.WARNING, "Error creating the channel");
    return null;
  } catch(Exception otherException){
    logger.log(Level.WARNING, "Unknown exception while creating channel");
    return null;
  }
}
}
