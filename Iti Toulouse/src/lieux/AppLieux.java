package lieux;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


	@PersistenceCapable
	public class AppLieux {
		
		    @PrimaryKey
		    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		    private Long id;
		    
		    @Persistent
		    private float latitude;
		    
		    @Persistent
		    private float longitude; 
		    
		    @Persistent
		    private String ville;
		    
		    @Persistent
		    private String type; 
		   
		 
			public AppLieux(float x, float y, String nomVille, String typeLieux) {
				
				this.longitude = x;
				this.latitude = y;
				this.ville = nomVille;
				this.type = typeLieux;
				
				
			}
			
			public long getKey() {
				return this.id;
			}
			
			public String getVille() {
				return this.ville;
			}
			
			public String getType() {
				return this.type;
			}
			
			public float getLatitude() {
				return this.latitude;
			}
			
			public float getLongitude() {
				return this.longitude;
			}
			
			
			public void setVille( String nomVille) {
				this.ville = nomVille;
			}
			
			public void setType( String typeVille) {
				this.type = typeVille;
			}
			
			public void setLatitude( float y ) {
				this.latitude = y;
			}
			
			public void SetLongitude(float x) {
				this.longitude = x;
			}
	}