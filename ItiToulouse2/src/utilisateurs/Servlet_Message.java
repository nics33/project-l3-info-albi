package utilisateurs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelFailureException;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Servlet_Message extends HttpServlet{
	
	  private static final Logger logger = Logger.getLogger(Servlet_Message.class.getCanonicalName());
	  
	  private static ChannelService channelService = ChannelServiceFactory.getChannelService();

	  protected void doPost(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException {
			    String type = request.getParameter("type");
			    String lat = request.getParameter("lat");
			    String lng = request.getParameter("lng");
			    String latlieu = request.getParameter("latlieu");
			    String lnglieu = request.getParameter("lnglieu");
			    String friend = request.getParameter("to");
			    String outputMessage = "";
			    
				  //démarage du UserService
			      UserService userService = UserServiceFactory.getUserService();
			      User user = userService.getCurrentUser();
			      String userID = user.getUserId();
			      
			      if (friend != null && !friend.equals("") && type != null && !type.equals("")) {
			      try{
			    	  if(type.equals("Update"))
			    	  {
				    	  outputMessage = "{ \"type\" : \"UpdateCoord\",\"lat\" : \""+lat+"\",\"lng\" : \""+lng+"\",\"latlieu\" : \""+latlieu+"\",\"lnglieu\" : \""+lnglieu+"\",\"from\" :  " +userID + "}";
			    	  }
			    	  else outputMessage = "{ \"type\" : \"SendCoord\",\"lat\" : \""+lat+"\",\"lng\" : \""+lng+"\",\"latlieu\" : \""+latlieu+"\",\"lnglieu\" : \""+lnglieu+"\",\"from\" :  " +userID + "}";


			      	/*String outputMessage ="<data>" +
					  "<type>updateChatBox</type>" +
					  "<message>"+message+"</message>" +
					  "<from>"+from+"</from>" +
					  "</data>"; 
			      		*/
			        logger.log(Level.INFO,"sending message  into the channel");
			      	sendMessageToChannel(friend, outputMessage);
			      } catch (ChannelFailureException channelFailure) {
			      	logger.log(Level.WARNING, "Failed in sending message to channel");
			        response.getWriter().print("OFFLINE");
			      } catch (Exception e) {
			        logger.log(Level.WARNING, "Unknow error while sending message to the channel");
			        response.getWriter().print("OFFLINE");
			      }
			    }
			  }

			  /**
			   * Creates the Channel Message and sends to the client  
			   * @param user the user to whom the message is sent 
			   * @param message the message that needs to pass 
			   */
	  public void sendMessageToChannel(String user,String message) throws ChannelFailureException{
           channelService.sendMessage(new ChannelMessage(user, message));
	 }
}
