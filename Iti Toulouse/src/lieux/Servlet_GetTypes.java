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
public class Servlet_GetTypes extends HttpServlet {

	

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException,
			IOException {		
		System.out.println("d�but");

		String valeureRetour = "0";
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");			
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
		
		ArrayList<ArrayList<Float>> listCoord = villeElem.getCoord();
		
		
		
		Query query = pm.newQuery("select distinct type from " +AppLieux.class);
		query.setFilter("ville == villeparam");
	    query.declareParameters("String villeparam");
	    query.setOrdering("ville asc");
	   
		@SuppressWarnings("unchecked")
		List<String> results = (List<String>) query.execute(villeElem.getVille().toLowerCase());
		
		System.out.println( "il y a  : "  +  results.size());
		
		if(results.size() > 0) {
			
			valeureRetour = "0";
				
		}
		else valeureRetour = "1";
		
		pm.close();
		
		// On convertit le r�sultat en JSON
		String reponse = "";
		reponse = "{ \"status\" : "+valeureRetour+",\"donnees\" :  " +results + "}";
    	
    	System.out.println(reponse);

		// On renvoie le contenu JSON
    	
		resp.setContentType("application/json");
		resp.getWriter().println(reponse);

	}
}
