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
		//System.out.println("1" );
		
		
		
		
		//PersistenceManager pm = PMF.getPersistenceManager();
		
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");	
		
		VilleElem villeElem = new VilleElem();
		
		villeElem = gson.fromJson(jsonFile, VilleElem.class);
		
		ArrayList<VilleElem.Coord> listCoord = villeElem.getCoord();
		
		for( int i = 0 ; i < listCoord.size(); i++){
			
			System.out.println(villeElem.getVille());
			System.out.println(villeElem.getType());
			System.out.println(listCoord.get(i).getLat());
			System.out.println(listCoord.get(i).getLng());
			
		}
	
	
		// Create a record
		
		//AppLieux lieux = new AppLieux(lat, lng, nomVille, typeVille);
		//pm.makePersistent(lieux); // La clé est initialisée ici
		//pm.close();
		//response.sendRedirect("/menu.jsp?message=createdOK");
		
		
	}
}
