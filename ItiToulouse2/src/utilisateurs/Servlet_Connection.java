package utilisateurs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	  
	  ArrayList<String> ListeAmi;
	  ArrayList<String> returnValue = new ArrayList<String>(); 
	  ArrayList<String> demandeAmi = new ArrayList<String>(); 
	  String temp;
	  String reponse;
	  Date now = new Date();

	  
	  //démarage du UserService
      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();	 
      //Si l'utilisateur est connecté a un compte Google
      if (user != null) { 
    	  //on récupere le UserId de l'utilisateur
    	  //creation de la session
    	  HttpSession session = req.getSession(true);
    	  session.setMaxInactiveInterval(5400);
    	  String monID = (String)session.getAttribute("userid"); 
    	  String token = (String)session.getAttribute("token");
    	  String monNickname = (String)session.getAttribute("nickname"); 
    	  Long datetoken = (Long)session.getAttribute("datetoken"); 
    	  Integer admin = (Integer)session.getAttribute("admin"); 
    	  if( monID==null ||!monID.equals(user.getUserId()) || monNickname==null || token==null ||datetoken ==null || !(now.getTime() < (datetoken + 6900000)) || admin ==null)// je vérifie les données de ma sesion, notamment si le token est toujours valide(je change le token s'il vit depuis plus de 1h55
    	  {
        	  logger.log(Level.INFO,"monID : {0} ",monID);
        	  logger.log(Level.INFO,"ID Google : {0} ",user.getUserId());
        	  logger.log(Level.INFO,"Mon nickname : {0} ",monNickname);
        	  logger.log(Level.INFO,"Mon token : {0}",token);
        	  logger.log(Level.INFO,"datetoken : {0}",datetoken);
        	  logger.log(Level.INFO,"admin : {0}",admin);
    		  monID = user.getUserId();
    		  monNickname = user.getNickname();
    		  
  	      	  //On créé un channel pour l'utilisateur
    		  token = createChannel(monID);
    		  
    		  datetoken = new Date().getTime();
  	           
    		  if(userService.isUserAdmin()) admin = 1; else admin = 0;
    		  
    		  session.setAttribute("userid", monID);
    		  session.setAttribute("nickname", monNickname);
    		  session.setAttribute("admin", admin);
    		  session.setAttribute("token", token);
    		  session.setAttribute("datetoken",datetoken);
    		  
        	  logger.log(Level.INFO,"create a token valid for {0} ms",datetoken + 6900000-now.getTime());

    	  }
    	  else logger.log(Level.INFO,"token already exist,expire in {0} ms", datetoken+ 6900000-now.getTime());
	        AppUser myAppUser  = new AppUser(user);
	        ListeAmi = myAppUser.getListFriends();
	        
	        //On ajoute l'utilisateur a la liste des Utilisateurs déja Connectés
	      	FriendStore friendStore = FriendStore.getInstance();
	      	if(!friendStore.getFriends().contains(monID)){
	        	  logger.log(Level.INFO,"User {0} is added to list of users",monID);
	        	  friendStore.addNewFriend(monID);

	          } 
	      	else{
	        	  logger.log(Level.INFO,"User {0} is already added  to list of users\n" +"hence, not adding now",user);
	        	}
	      	
	      	//Il faut maintenant regarder qu'elles amis sont déja connectés à l'application
	        Iterator<String> friendList = ListeAmi.iterator();

	        while(friendList.hasNext()){
	        	  String friend = friendList.next() ;
	            if(friendStore.getFriends().contains(friend)) //Si le friendstore contient mon Ami(friend = id de l'ami)
	            {
		    		PersistenceManager pm = PMF.get().getPersistenceManager();
		    		AppUser myUserTemp = pm.getObjectById(AppUser.class, friend); // j'obtiens ses informations dans la base de donnée
		    		pm.close();
	    			temp = "{ \"nickname\" : \"" +myUserTemp.getEmail()+ "\",\"id\" :  \"" +myUserTemp.getAppUserId() +"\"}";
	    			returnValue.add(temp); // j'ajoute ses information dans un tableau
			    	String outputMessage = "{ \"type\" : \"UpdateFriendlist\",\"id\" : \""+monID+"\",\"nickname\" : \""+monNickname+"\",\"from\" : \"Server\"}";
	              channelService.sendMessage(
	        		  new ChannelMessage(friend,outputMessage)); // et j'indique a mon ami que je me suis connecté
	        	  }

	          }
	        
	        
	        // On regarde si il y a des demandes d'amitié et si il y en a on les met dans un tableu pour les envoyé a l'utilisateur.
	        for(int i =0; i<myAppUser.demandeRecu.size();i++)
	        {
	        	PersistenceManager pm = PMF.get().getPersistenceManager();
	        	AppUser myUserTemp = pm.getObjectById(AppUser.class, myAppUser.demandeRecu.get(i));
	        	pm.close();
    			temp = "{ \"nickname\" : \"" +myUserTemp.getEmail()+ "\",\"id\" :  \"" +myUserTemp.getAppUserId() +"\"}";
    			demandeAmi.add(temp);

	        }
	        
			// On convertit le résultat en JSON
			
        	reponse = "{ \"status\" : 0,\"admin\" : "+admin.toString()+",\"demandeami\" : "+demandeAmi+",\"token\" : \""+token+"\",\"donnees\" :  " +returnValue + "}";
        	
        	
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
