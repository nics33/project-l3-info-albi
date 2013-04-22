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
	    ArrayList<Key> friendList; 

		public AppUser(User user) {
			this.key = KeyFactory.createKey(AppUser.class.getSimpleName(), user.getUserId());
			this.email = user.getEmail();
			this.friendList = new ArrayList<Key>();
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
	// retourne 1 si le friendmail n'est pas présent dans la db, 0 si l'ajout réussi, 2 si un probleme dans la db est l'userID de l'ami est présent 2 fois(ne dois pas arriver),3 si jamais l'ami est déja présent dans la liste d'ami
	public int addFriend(String friendEmail){
		int valeurRetour = 5;
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		
		//Check si l'utilisateur que l'on veut ajouter en ami existe déja dans la base de données
		// en premier on fait la requête
		Query query = pm.newQuery(AppUser.class);
	    query.setFilter("email == emailParam");
	    query.declareParameters("String emailParam");

	    //on execute la requete
		List<AppUser> results = (List<AppUser>) query.execute(friendEmail);
        //on traite le resultat obtenu
        switch (results.size()) 
        {
        case 0: valeurRetour = 1;
        		break;
        		
        case 1:	if(myUser.checkIfFriendExistInListFriend(results.get(0)))
        		{
        			valeurRetour = 3;
        		}
        		else
        		{
    				this.friendList.add(results.get(0).getAppUserId());
    				myUser.friendList.add(results.get(0).getAppUserId()); 
    				valeurRetour = 0;
        		}
        		break;
        		
        default: valeurRetour = 2;
        		break;		
        }
		pm.close();
		return valeurRetour;
	}
	
	public ArrayList<Key> getListFriends(){
		return friendList;
	}
	
	public boolean checkIfFriendExistInListFriend(AppUser myFriend){
		boolean exist = false;
		int i = 0;
		System.out.println(this.friendList.size());
		for(i=0;i < this.friendList.size();i++)
		{
			if(this.friendList.get(i) == myFriend.getAppUserId())
			{
				System.out.println(this.friendList.get(i).getName());
				exist = true;
			}
		}
		return exist;
	}
	
}
