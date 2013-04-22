package myServlet;

import java.util.ArrayList;

import java.util.List;
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
public class AppUser {
	
	    @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private Key key;
	    
	    @Persistent
	    String email;
	    
	    @Persistent
	    ArrayList<AppUser> friendList; 

		public AppUser(User user) {
			this.key = KeyFactory.createKey(AppUser.class.getSimpleName(), user.getUserId());
			this.email = user.getEmail();
			this.friendList = new ArrayList<AppUser>();
		}

		// Accessors for the fields. JPA doesn't use these, but your application
		// does.

	public Key getAppUserId() {
			return key;
		}
	
	public void AddUserToDb(AppUser myUser) {
		PersistenceManager pm = PMF.getPersistenceManager();
		pm.makePersistent(myUser);
		pm.close();
	}
	// retourne 1 si le friendmail n'est pas pr�sent dans la db, 0 si l'ajout r�ussi, 2 si un probleme dans la db est l'userID de l'ami est pr�sent 2 fois(ne dois pas arriver)
	public int addFriend(String friendEmail){
		int valeurRetour = 0;
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		
		//Check si l'utilisateur que l'on veut ajouter en ami existe d�ja dans la base de donn�es
		// en premier on fait la requ�te
		Query query = pm.newQuery(AppUser.class);
	    query.setFilter("email == emailParam");
	    query.declareParameters("String emailParam");

	    //on ex�cute la requ�te
		List<AppUser> results = (List<AppUser>) query.execute(friendEmail);
        //on traite le r�sultat obtenu
        switch (results.size()) 
        {
        case 0: valeurRetour = 1;
        		break;
        		
        case 1:	this.friendList.add(results.get(0));
			    myUser.friendList.add(results.get(0)); 
				valeurRetour = 0;
        		break;
        		
        default: valeurRetour = 2;
        		break;		
        }
		pm.close();
		return valeurRetour;
	}
	
	public ArrayList<AppUser> getListFriends(){
		return friendList;
	}
	
}
