package myServlet;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class AppUser {
	
	    @PrimaryKey
	    @Persistent
		String appUserID ; 

		public AppUser(User user) {
			this.appUserID = user.getUserId();
		}

		// Accessors for the fields. JPA doesn't use these, but your application
		// does.

	public String getAppUserId() {
			return appUserID;
		}
}
