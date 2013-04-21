package myServlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import jdo.GestionAppUser;


@SuppressWarnings("serial")
public class Servlet_LginServlet extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 	GestionAppUser myManager = new GestionAppUser();
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        if (user != null) {
		        AppUser myAppUser  = new AppUser(user);
		        myAppUser.AddUserToDb(myAppUser);
		        //myManager.AddUser(myAppUser);
	            resp.getWriter().println("Hello, " + user.getNickname());
	            resp.getWriter().println("!  You can <a href=\"" + userService.createLogoutURL(req.getRequestURI()) + "\">sign out</a>.</p>");
	            resp.getWriter().println(user.getUserId());

	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}