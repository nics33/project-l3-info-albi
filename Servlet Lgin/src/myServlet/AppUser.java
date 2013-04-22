package myServlet;

import java.util.ArrayList;

import java.util.List;
import javax.jdo.Query;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import javax.jdo.PersistenceManager;
import isis.cloud.jdo.PMF;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class AppUser {
	
	    @PrimaryKey
	    @Persistent
		String appUserID ; 
	    
	    
	    @Persistent
	    String email;
	    
	    @Persistent
	    ArrayList<AppUser> friendList; 

		public AppUser(User user) {
			this.appUserID = user.getUserId();
			this.email = user.getEmail();
			this.friendList = new ArrayList<AppUser>();
		}

		// Accessors for the fields. JPA doesn't use these, but your application
		// does.

	public String getAppUserId() {
			return appUserID;
		}
	
	public void AddUserToDb(AppUser myUser) {
		PersistenceManager pm = PMF.getPersistenceManager();
		pm.makePersistent(myUser);
		pm.close();
	}
	// retourne 1 si le friendmail n'est pas présent dans la db, 0 si l'ajout réussi, 2 si un probleme dans la db est l'userID de l'ami est présent 2 fois(ne dois pas arriver)
	public int addFriend(String friendEmail){
		int valeurRetour = 0;
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser MyUser = pm.getObjectById(AppUser.class, this.appUserID);
		User userFriend = new User(friendEmail,"gmail.com");
		AppUser friend = new AppUser(userFriend);
		
		//Check si l'utilisateur que l'on veut ajouter en ami existe déja dans la base de données
		// en premier on fait la requête
		Query query = pm.newQuery(AppUser.class);
	    query.setFilter("appUserID == appUserIDParam");
	    query.declareParameters("String appUserIDParam");
	    System.out.println(userFriend.getNickname());
	    System.out.println(userFriend.getUserId());
	    //on exécute la requête
		List<AppUser> results = (List<AppUser>) query.execute(userFriend.getUserId());
        //on traite le résultat obtenu
        /*switch (results.size()) 
        {
        case 0: valeurRetour = 1;
        		break;
        		
        case 1:	//this.friendList.add(friend);
				//MyUser.friendList.add(friend); 
				valeurRetour = 0;
        		break;
        		
        default: valeurRetour = 2;
        		break;		
        }
        */
		pm.close();
		return valeurRetour;
	}
	
	public ArrayList<AppUser> getListFriends(){
		return friendList;
	}
	
}
