package lieux;

import pmf.PMF;

import java.io.IOException;
import java.text.DecimalFormat;
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
public class Servlet_GetEntities extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException,
			IOException {		
		
		
		String valeureRetour = "0";
		String chaine ="";
		
		//Déclaration du format de la latitude et de la longitude
		DecimalFormat df = new DecimalFormat("########.00000000000"); 
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Lecture du Json passé en paramètre ( méthode POST)
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");			
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
		
		@SuppressWarnings("unused")
		ArrayList<ArrayList<Float>> listCoord = villeElem.getCoord();
		
		
		//Requête qui récupère tout les éléments d'un paquet
		// paquet sélectionné grâce à  sa ville et son type d'éléments
		Query query = pm.newQuery(AppLieux.class);
		query.setFilter("ville == villeparam && type == typeparam");
	    query.declareParameters("String villeparam, String typeparam");
	    query.setOrdering("ville asc");
	   
		@SuppressWarnings("unchecked")
		List<AppLieux> results = (List<AppLieux>) query.execute(villeElem.getVille().toLowerCase(), villeElem.getType().toLowerCase());
		
		//System.out.println( "il y a  : "  +  results.size());
		
		//S'il ne nombre de résultat est nul.
		if(results.size() == 0) {

			
			valeureRetour = "1";
				
		}
		
		//Sinon, pour chaques éléments, on l'enregistre dans une chaîne de caractère au format Json,
		// Puis on renvoie 0
		else
		{
			valeureRetour = "0";
			for(int i =0; i< results.size(); i++){
				
				
				chaine += "{\"lat\" :"+ df.format(results.get(i).getLatitude()) + ", \"lng\" :"+ df.format(results.get(i).getLongitude()) +"}";
				
				
				//Pour tout les éléments, sauf le dernier
				// on rajoute une virgule pour les séparer
				if(i < results.size()-1){
				chaine += ",";
				}
				
			}
		}
		
		pm.close();
		
		// On convertit le résultat en JSON
		String reponse = "";
		reponse = "{ \"status\" : "+valeureRetour+",\"donnees\" : [" + chaine + "]}";
    	
    	//System.out.println(reponse);

		// On renvoie le contenu JSON
    	
		resp.setContentType("application/json");
		resp.getWriter().println(reponse);

	}
}
