package utilisateurs;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
@SuppressWarnings("serial")
public class Servlet_AddFriend extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 
		 	String reponse = "";
		 	int returnValue = 0;
		 	String emailFriend = "";
		 	
		 	emailFriend = req.getParameter("email");
		 //	System.out.println(emailFriend);
		 	
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        
	        if (user != null) { 
	        	
		        AppUser myAppUser  = new AppUser(user);
		        returnValue = myAppUser.addFriend(emailFriend);
		        
	        	reponse = "{ \"status\" :" +returnValue + "}";

				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
				resp.getWriter().print(reponse);
				
	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}