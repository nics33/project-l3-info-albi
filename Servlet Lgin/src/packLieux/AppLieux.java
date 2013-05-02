package packLieux;



	

	import java.util.List;
import java.util.logging.Logger;

	import javax.jdo.Query;

	import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

	import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import javax.jdo.PersistenceManager;
import isis.cloud.jdo.PMF;

import com.google.appengine.api.users.User;

	@PersistenceCapable
	public class AppLieux {
		
		    @PrimaryKey
		    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		    private long key;
		    
		    @Persistent
		    private String latitude;
		    
		    @Persistent
		    private String longitude; 
		    
		    @Persistent
		    private String ville;
		    
		    @Persistent
		    private String type; 
		   
		 
			public AppLieux(String x, String y, String nomVille, String typeLieux) {
				
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
			
			public String getLatitude() {
				return this.latitude;
			}
			
			public String getLongitude() {
				return this.longitude;
			}
			
			
			public void setVille( String nomVille) {
				this.ville = nomVille;
			}
			
			public void setType( String typeVille) {
				this.type = typeVille;
			}
			
			public void setLatitude( String y ) {
				this.latitude = y;
			}
			
			public void SetLongitude(String x) {
				this.longitude = x;
			}
	}