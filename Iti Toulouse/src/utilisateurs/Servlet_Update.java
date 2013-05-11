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

@SuppressWarnings("serial")
public class Servlet_Update extends HttpServlet {
	 public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 
		 //déclaration des variables qui vont contenir les paramètres
		 String ville;
		 String type;
		 float latlieu;
		 float lnglieu;
		 float lat;
		 float lng;
		 
		 //déclaration de la variable qui va contenir la réponse
		 	String reponse;
		 	
		 // déclaration des variables temporaires
		 	String temp ="";
		 	int i = 0;
		 	ArrayList<String> returnValue = new ArrayList<String>(); 
		 	
		 	//placement des valeurs du parametres dans les variables
		 	ville = req.getParameter("ville");
		 	type = req.getParameter("type");
		 	latlieu = Float.parseFloat(req.getParameter("latlieu"));
		 	lnglieu = Float.parseFloat(req.getParameter("lnglieu"));
		 	lng = Float.parseFloat(req.getParameter("lng"));
		 	lat = Float.parseFloat(req.getParameter("lat"));

		 	
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        if (user != null) { 
		        AppUser myAppUser  = new AppUser(user);
		        //
		        myAppUser.modifyDateLastConnection();
		         
		        //placement des valeurs du parametres dans myAppUser donc dans la BDD
		        
		        myAppUser.modifyLieu(ville, type, latlieu, lnglieu);
		        myAppUser.modifyUserLocalisation(lat, lng);
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
		    			temp = "{ \"email\" : \"" +myUserTemp.getEmail()+ "\","+ myUserTemp.getLieu()+","+myUserTemp.getUserLocalisation()+"}";
		    			returnValue.add(temp);
		    		}
		        }
		        System.out.println(temp);
				// On convertit le résultat en JSON
				
	        	reponse = "{ \"status\" : 0,\"donnees\" :  " +returnValue + "}";

				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
				resp.getWriter().print(reponse);
	        } else {
	        	reponse = "{ \"status\" : 10,\"donnees\" : \" " + userService.createLoginURL("http://utilisateurstoutouf.appspot.com/panel.html") + "\"}";
	        	resp.setContentType("application/json");
	            resp.getWriter().println(reponse);
	        }
	    }
	}