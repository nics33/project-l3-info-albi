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
		//System.out.println("début");

		// On initialise la valeure de retour à 0
		String valeureRetour = "0";
		
		//Ouverture du PMF
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Lecture du Json fournit en paramètre ( méthode POST)
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");			
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
		
		ArrayList<ArrayList<Float>> listCoord = villeElem.getCoord();
		
		
		// Requête query qui prend en paramètre une ville et un type
		// Si 0 résultats : Le paquets n'est pas présent dans la base de données
		// on peut donc l'enregistrer
		// Sinon on renvoie une erreure
		
		
		Query query = pm.newQuery(AppLieux.class);
		query.setFilter("ville == villeparam && type == typeparam");
	    query.declareParameters("String villeparam, String typeparam");
	    query.setOrdering("ville asc");
	   
		@SuppressWarnings("unchecked")
		List<AppLieux> results = (List<AppLieux>) query.execute(villeElem.getVille().toLowerCase(), villeElem.getType().toLowerCase());
		
		// Si aucun résulats	
		if(results.size() == 0) {

			// Alors on enregistre tout les éléments du paquets
			for( int i = 0 ; i < listCoord.size(); i++){
				
				AppLieux lieux = new AppLieux(listCoord.get(i).get(0),//latitude
											 listCoord.get(i).get(1), //longitude
											 villeElem.getVille().toLowerCase(),
											 villeElem.getType().toLowerCase()
											);		
				pm.makePersistent(lieux);
				
			}
			
			//et on renvoie le statut 0 pour dire que l'enregistrement à été effectué.
			valeureRetour = "0";
				
		}
		// Sinon, on renvoie le statut 1.
		else valeureRetour = "1";
		
		pm.close();
		
		// On convertit le résultat en JSON
		String reponse = "{ \"status\" : \""+ valeureRetour + "\"}";
    	
    	//System.out.println(reponse);

		// On renvoie le contenu JSON
		response.setContentType("application/json");
		response.getWriter().println(reponse);

	}
}
