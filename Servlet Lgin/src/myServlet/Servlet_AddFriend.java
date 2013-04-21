package myServlet;

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
public class Servlet_AddFriend extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 	int returnValue = 0;
		 	String emailFriend = req.getParameter("email");
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        if (user != null) { 
	        	
		        AppUser myAppUser  = new AppUser(user);
		        //returnValue = myAppUser.addFriend(emailFriend);
		        
				// On convertit le résultat en JSON
				// Gson gson = new Gson();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String jsonResult = gson.toJson(returnValue);
				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
				resp.getWriter().print(jsonResult);
	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}