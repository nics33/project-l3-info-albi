package myServlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Servlet_imAlive extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        if (user != null) { 
	        	
		        AppUser myAppUser  = new AppUser(user);
		        myAppUser.modifyDateLastConnection();
	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}