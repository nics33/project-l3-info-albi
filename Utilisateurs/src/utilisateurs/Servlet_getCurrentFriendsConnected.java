package utilisateurs;

import pmf.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class Servlet_getCurrentFriendsConnected extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 	int i = 0;
		 	ArrayList<String> returnValue = new ArrayList<String>(); 
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        if (user != null) { 
		        AppUser myAppUser  = new AppUser(user);
		        Date myDate = new Date();
		        Date Datetemp = new Date();
		        Datetemp.setTime(myDate.getTime()-300000);
		        for(i=0;i<myAppUser.friendList.size();i++)
		        {
		    		PersistenceManager pm = PMF.get().getPersistenceManager();
		    		AppUser myUserTemp = pm.getObjectById(AppUser.class, myAppUser.friendList.get(i));
		    		pm.close();
		    		if(myUserTemp.getDateLastConnection().after(Datetemp))
		    		{
		    			returnValue.add(myUserTemp.getAppUserId());
		    		}
		        }
				// On convertit le résultat en JSON
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