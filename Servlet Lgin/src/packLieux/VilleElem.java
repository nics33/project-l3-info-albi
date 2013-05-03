package packLieux;

import java.util.ArrayList;

 public class VilleElem {
	 
	 private String ville;

	 private String type;
	 
	 private ArrayList<ArrayList<Double>> liste;
	 
	 public ArrayList<ArrayList<Double>> getCoord(){
		 
		 return this.liste;
		 
	 }
	 
	 public String getVille(){
		 return this.ville;
	 }
	 
	 public String getType(){
		 return this.type;
	 }
	 
	
}

 
