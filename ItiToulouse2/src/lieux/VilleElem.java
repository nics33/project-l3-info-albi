package lieux;

import java.util.ArrayList;

 public class VilleElem {
	 
	 private String ville;

	 private String type;
	 
	 private ArrayList<ArrayList<Float>> liste;
	 
	 public ArrayList<ArrayList<Float>> getCoord(){
		 
		 return this.liste;
		 
	 }
	 
	 public String getVille(){
		 return this.ville;
	 }
	 
	 public String getType(){
		 return this.type;
	 }
	 
	
}

 
