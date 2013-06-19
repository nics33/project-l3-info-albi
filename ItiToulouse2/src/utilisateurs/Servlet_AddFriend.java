package utilisateurs;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmf.PMF;

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
		 	int valeurRetour = 0;
		 	
		 	emailFriend = req.getParameter("email");
		 //	System.out.println(emailFriend);
		 	
	        UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();	 
	        
	        if (user != null) { 
	        	PersistenceManager pm = PMF.get().getPersistenceManager();
	        	AppUser myAppUser = pm.getObjectById(AppUser.class, user.getUserId());
	        	if(myAppUser.getEmail().equals(emailFriend)) // on vérifie que l'utilisateur ne s'ajoute pas lui même en ami
	    		{
	    			valeurRetour = 4;
	    		}
	    		else
	    		{
	    			//Check si l'utilisateur que l'on veut ajouter en ami existe déja dans la base de données
	    			// en premier on fait la requête
	    			Query query = pm.newQuery(AppUser.class);
	    		    query.setFilter("email == emailParam");
	    		    query.declareParameters("String emailParam");
	    		    //on execute la requete
	    			@SuppressWarnings("unchecked")
	    			List<AppUser> results = (List<AppUser>) query.execute(emailFriend);
	    	        switch (results.size()) 
	    	        {
	    	        case 0: valeurRetour = 1;
	    	        		break;
	    	        		
	    	        case 1:	if(myAppUser.checkIfFriendExistInListFriend(results.get(0)))
	    	        		{
	    	        			valeurRetour = 3;
	    	        		}
	    	        		else
	    	        		{
	    	        			AppUser myFriend = pm.getObjectById(AppUser.class, results.get(0).getAppUserId());
	    	        			myAppUser.AddtodemandeEnvoye(results.get(0).getAppUserId());
	    	    				myFriend.AddtodemandeRecu(myAppUser.getAppUserId());
	    	    				valeurRetour = 0;
	    	        		}
	    	        		break;
	    	        		
	    	        default: valeurRetour = 2;
	    	        		 break;		
	    	        }
	    		}

	    		pm.close();

	        	reponse = "{ \"status\" :" +valeurRetour + "}";

				
				// On renvoie le contenu JSON
				resp.setContentType("application/json");
				resp.getWriter().print(reponse);
				
	        } else {
	            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	        }
	    }
	}