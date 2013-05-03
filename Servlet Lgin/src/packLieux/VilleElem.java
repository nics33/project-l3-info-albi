package packLieux;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


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
	 
	 
	 
	 public class Coord {
		 
		 private Double lat;
		 
		 private Double lng;
		 
		 public Double getLat(){
			 
			 return this.lat;
		 }
		 
		 public Double getLng() {
			 
			 return this.lng;
		 }
		 
	
		 
	 }
	 
	
}

 
