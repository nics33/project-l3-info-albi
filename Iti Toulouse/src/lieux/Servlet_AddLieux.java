package lieux;

import pmf.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.jdo.Query;

import com.google.gson.Gson;




 
@SuppressWarnings("serial")
public class Servlet_AddLieux extends HttpServlet {

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {		
		System.out.println("début");

		String valeureRetour = "0";
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");			
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
		
		ArrayList<ArrayList<Float>> listCoord = villeElem.getCoord();
		
		
		
		Query query = pm.newQuery(AppLieux.class);
		query.setFilter("ville == villeparam");
	    query.declareParameters("String villeparam");
	    query.setOrdering("ville asc");
	   
		@SuppressWarnings("unchecked")
		List<AppLieux> results = (List<AppLieux>) query.execute(villeElem.getVille());
		
		if(results.size() == 0) {

			for( int i = 0 ; i < listCoord.size(); i++){
				
				AppLieux lieux = new AppLieux(listCoord.get(i).get(0),//latitude
											 listCoord.get(i).get(1), //longitude
											 villeElem.getVille(),
											 villeElem.getType()
											);		
				pm.makePersistent(lieux);
				
			}
			
			valeureRetour = "0";
				
		}
		else valeureRetour = "1";
		pm.close();
		// On convertit le résultat en JSON
		String reponse = "";
    	reponse = "{ \"status\" : \""+ valeureRetour + "\"}";
    	System.out.println(reponse);

		// On renvoie le contenu JSON
    	
		response.setContentType("application/json");
		response.getWriter().println(reponse);

	}
}
