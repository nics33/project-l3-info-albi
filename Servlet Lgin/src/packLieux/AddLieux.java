package packLieux;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


import isis.cloud.jdo.PMF;


public class AddLieux extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		
		
		
		
		
	//	PersistenceManager pm = PMF.getPersistenceManager();
			
		Gson gson = new Gson();
		String jsonFile = request.toString();
		
		System.out.println("1" + jsonFile + "2" );
		
		/*gson.fromJson(jsonString, Latitude.class);
		
	
		String nomVille = request.getParameter("ville");
		String typeVille = request.getParameter("type");
		
		// Create a record
		AppLieux lieux = new AppLieux(lat, lng, nomVille, typeVille);
		pm.makePersistent(lieux); // La clé est initialisée ici
		pm.close();
		response.sendRedirect("/menu.jsp?message=createdOK");
		
		*/
	}
}
