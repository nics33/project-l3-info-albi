

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
 public class Servlet_GetVille  extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException,
			IOException {		


		String valeureRetour = "0";
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Requête JQuery qui renvoie les villes présente dans la base de données ( sans doublons )
		Query query = pm.newQuery("JDOQL" , "SELECT DISTINCT ville FROM lieux.AppLieux");
	   
		@SuppressWarnings("unchecked")
		List<String> results = (List<String>) query.execute();
		
	
		
		if(results.size() > 0) {
			
			valeureRetour = "0";
				
		}
		else valeureRetour = "1";
		
		pm.close();
		
		
		//On enregistre toutes les villes dans une chaîne de caractères au format Json
		String chaine = "[";
		for(int i =0; i< results.size(); i++){
			
			
			chaine += '"'+ results.get(i) +'"';
			
			if(i < results.size()-1){
			chaine += ",";
			}
			
		}
		
		chaine += "]";
		
		//System.out.println(chaine);

		
		
		// On convertit le résultat en JSON
		String reponse = "";
		reponse = "{ \"status\" : "+valeureRetour+",\"donnees\" :  " +chaine + "}";
    	
    	//System.out.println(reponse);

		// On renvoie le contenu JSON	
		resp.setContentType("application/json");
		resp.getWriter().println(reponse);

	}
}
