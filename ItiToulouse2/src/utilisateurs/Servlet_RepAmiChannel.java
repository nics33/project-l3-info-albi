package utilisateurs;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmf.PMF;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
@SuppressWarnings("serial")
public class Servlet_RepAmiChannel  extends HttpServlet {
	
	  private static ChannelService channelService = ChannelServiceFactory.getChannelService();

	  
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
		        AppUser myAppUser  = pm.getObjectById(AppUser.class, user.getUserId());
		        AppUser myFriend = pm.getObjectById(AppUser.class, idFriend);		        
		        //Je rajoute les id aux listes d'amis
		        if(type.equals("accept"))
		        {

		        	myAppUser.AddtoFriendlist(idFriend);
		        
		        	myFriend.AddtoFriendlist(myAppUser.getAppUserId());
		        }
		        
		        pm.close();
    	      	FriendStore friendStore = FriendStore.getInstance();
    	    	if(friendStore.getFriends().contains(idFriend))
    	    	{
			    	String outputMessage = "{ \"type\" : \"UpdateFriendlist\",\"id\" : \""+user.getUserId()+"\",\"nickname\" : \""+user.getNickname()+"\",\"from\" : \"Server\"}";
		              channelService.sendMessage(
		        		  new ChannelMessage(idFriend,outputMessage));
		              
    	    	}
	        	reponse = "{ \"status\" :" +returnValue + "}";

				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
				resp.getWriter().print(reponse);
				
	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}