package myServlet;

import java.util.ArrayList;
import java.util.Date;

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
public class AppUser {
	
	    @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private String key;
	    
	    @Persistent
	    String email;
	    
	    @Persistent
	    ArrayList<String> friendList; 
	    
	    @Persistent
	    private Date LastConnection;

		public AppUser(User user) {
			this.key = user.getUserId();
			this.email = user.getEmail();
			this.friendList = new ArrayList<String>();
			this.LastConnection = new Date();
		}

		// Accessors for the fields. JPA doesn't use these, but your application
		// does.
		
	public Date getDateLastConnection(){
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		pm.close();
		return myUser.LastConnection;
	}
	
	public void modifyDateLastConnection(){
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		Date Temp = new Date();
		myUser.LastConnection = Temp;
		pm.close();
	}

	public String getAppUserId() {
			return key;
		}
	
	public void AddUserToDb(AppUser myUser) {
		PersistenceManager pm = PMF.getPersistenceManager();
		pm.makePersistent(myUser);
		pm.close();
	}
	// retourne 1 si le friendmail n'est pas pr�sent dans la db, 0 si l'ajout r�ussi, 2 si un probleme dans la db est l'userID de l'ami est pr�sent 2 fois(ne dois pas arriver),3 si jamais l'ami est d�ja pr�sent dans la liste d'ami,4 si l'utilisateur s'ajoute lui m�me en ami
	public int addFriend(String friendEmail){
		int valeurRetour = 0;
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		if(myUser.email.equals(friendEmail)) // on v�rifie que l'utilisateur ne s'ajoute pas lui m�me en ami
		{
			valeurRetour = 4;
		}
		else
		{
			//Check si l'utilisateur que l'on veut ajouter en ami existe d�ja dans la base de donn�es
			// en premier on fait la requ�te
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
		}

		pm.close();
		return valeurRetour;
	}
	
	public ArrayList<String> getListFriends(){
		PersistenceManager pm = PMF.getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		pm.close();
		return myUser.friendList;
	}
	
	public boolean checkIfFriendExistInListFriend(AppUser myFriend){
		boolean exist = false;
		int i = 0;
		System.out.println(this.friendList.size());
		for(i=0;i < this.friendList.size();i++)
		{
			System.out.println(this.friendList.get(i));
			System.out.println(myFriend.getAppUserId());
			System.out.println("plop");
			if(this.friendList.get(i).equals(myFriend.getAppUserId()))//a mis 10h a se rendre compte que this.friendList.get(i) == myFriend.getAppUserId() marchais en local mais pas qu'en on import� l'app sur appengine. VDM
			{
				System.out.println(this.friendList.get(i));
				exist = true;
			}
		}
		return exist;
	}
	
}
