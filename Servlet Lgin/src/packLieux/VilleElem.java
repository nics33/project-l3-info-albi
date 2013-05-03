package packLieux;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


 public class VilleElem {
	 
	 private String ville;

	 private String type;
	 
	 private ArrayList<Coord> elements;
	 
	 public ArrayList<Coord> getCoord(){
		 
		 return this.elements;
		 
	 }
	 
	 public String getVille(){
		 return this.ville;
	 }
	 
	 public String getType(){
		 return this.type;
	 }
	 
	 
	 
	 public class Coord {
		 
		 private int lat;
		 
		 private int lng;
		 
		 public int getLat(){
			 
			 return this.lat;
		 }
		 
		 public int getLng() {
			 
			 return this.lng;
		 }
		 
	
		 
	 }
	 
	
}

 
