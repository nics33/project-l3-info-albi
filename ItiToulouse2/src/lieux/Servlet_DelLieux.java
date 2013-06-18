package lieux;

import pmf.PMF;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.jdo.Query;

import com.google.gson.Gson;

 
@SuppressWarnings("serial")
public class Servlet_DelLieux extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {		
		
		// On ouvre le PMF
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Lecture du fichier Json passé en paramètre ( méthode POST).
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");			
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
			
		//Requête JQuery permettant de sélection un paquet
		// en fonction de sa ville et de son type
		Query query = pm.newQuery(AppLieux.class);
		query.setFilter("ville == villeparam && type == typeparam");
	    query.declareParameters("String villeparam, String typeparam");
	   
	    //Puis on le supprime de la base de données
	    query.deletePersistentAll(villeElem.getVille().toLowerCase(), villeElem.getType().toLowerCase());
		
			
		pm.close();
		
	

	}
}
