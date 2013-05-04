package packlieu;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import isis.cloud.jdo.PMF;


	@PersistenceCapable
	public class AppLieux {
		
		    @PrimaryKey
		    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		    private long key;
		    
		    @Persistent
		    private double latitude;
		    
		    @Persistent
		    private double longitude; 
		    
		    @Persistent
		    private String ville;
		    
		    @Persistent
		    private String type; 
		   
		 
			public AppLieux(double x, double y, String nomVille, String typeLieux) {
				
				this.longitude = x;
				this.latitude = y;
				this.ville = nomVille;
				this.type = typeLieux;
				
				
			}
			
			public long getKey() {
				return this.key;
			}
			
			public String getVille() {
				return this.ville;
			}
			
			public String getType() {
				return this.type;
			}
			
			public double getLatitude() {
				return this.latitude;
			}
			
			public double getLongitude() {
				return this.longitude;
			}
			
			
			public void setVille( String nomVille) {
				this.ville = nomVille;
			}
			
			public void setType( String typeVille) {
				this.type = typeVille;
			}
			
			public void setLatitude( double y ) {
				this.latitude = y;
			}
			
			public void SetLongitude(double x) {
				this.longitude = x;
			}
	}