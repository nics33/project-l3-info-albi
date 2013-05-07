package utilisateurs;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class Servlet_imAlive extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
	        String reponse = "";
	        if (user != null) { 
	        	
		        AppUser myAppUser  = new AppUser(user);
		        myAppUser.modifyDateLastConnection();
		        
				// On convertit le résultat en JSON
		        reponse = "{ \"status\" : 0,\"donnees\" : \"\"}";
		        
				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
		        resp.getWriter().print(reponse);
	        } else {
	        	reponse = "{ \"status\" : 1,\"donnees\" : \" " + userService.createLoginURL("http://utilisateurstoutouf.appspot.com/panel.html") + "\"}";
	        	resp.setContentType("application/json");
	            resp.getWriter().print(reponse);
	        }
	    }
	}