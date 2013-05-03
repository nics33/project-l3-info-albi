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
		
		
		
		
		
		
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");	
		System.out.println(jsonFile);
		
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
		System.out.println(villeElem.getVille());
		System.out.println(villeElem.getType());
		System.out.println(villeElem.getCoord().size());
		ArrayList<ArrayList<Double>> listCoord = villeElem.getCoord();
		
		for( int i = 0 ; i < listCoord.size(); i++){
			PersistenceManager pm2 = PMF.getPersistenceManager();
			AppLieux lieux = new AppLieux(listCoord.get(i).get(0),//latitude
										 listCoord.get(i).get(1), //longitude
										 villeElem.getVille(),
										 villeElem.getType()
										);
			
			pm2.makePersistent(lieux);
			pm2.close();
		}
	
	
		// Create a record
		
		//AppLieux lieux = new AppLieux(lat, lng, nomVille, typeVille);
		// // La clé est initialisée ici
		
		//response.sendRedirect("/menu.jsp?message=createdOK");
		
		
	}
}
