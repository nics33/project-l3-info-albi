package packlieu;

import isis.cloud.jdo.PMF;

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




 
public class AddLieux extends HttpServlet {

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		//System.out.println("1" );
		
		
		PersistenceManager pm2 = PMF.getPersistenceManager();
		
		
		
		
		
		
		Gson gson = new Gson();
		String jsonFile = request.getParameter("donnees");	
	//	System.out.println(jsonFile);
		
		VilleElem villeElem = gson.fromJson(jsonFile, VilleElem.class);
	//	System.out.println(villeElem.getVille());
	//	System.out.println(villeElem.getType());
	//	System.out.println(villeElem.getCoord().size());
		ArrayList<ArrayList<Double>> listCoord = villeElem.getCoord();
		
		
		
		Query query = pm2.newQuery(AppLieux.class);
		
		query.setFilter("ville = villeParam && type == typeParam");
	    query.declareParameters("String villeParam, String typePram");
	    List<AppLieux> results = (List<AppLieux>) query.execute(villeElem.getVille(), villeElem.getType());
	    
	    int valeureRetour = 0;

		if(results.size() == 0) {

			for( int i = 0 ; i < listCoord.size(); i++){
				
				AppLieux lieux = new AppLieux(listCoord.get(i).get(0),//latitude
											 listCoord.get(i).get(1), //longitude
											 villeElem.getVille(),
											 villeElem.getType()
											);		
				pm2.makePersistent(lieux);
				
			}
			
			valeureRetour = 0;
				
		}
		else {
			
			System.out.println( "Le paquet de type" + villeElem.getType() + " existe déjà pour la ville de " + villeElem.getVille() + ".");
			System.out.println( " Souhaitez-vous le remplacer ?");
			
			
			
			valeureRetour = 1;
		}
		
		
		
		
		
		

		
		pm2.close();
		
		
	
		// Create a record
		
		//AppLieux lieux = new AppLieux(lat, lng, nomVille, typeVille);
		// // La clé est initialisée ici
		
		//response.sendRedirect("/menu.jsp?message=createdOK");
		
		
	}
}
