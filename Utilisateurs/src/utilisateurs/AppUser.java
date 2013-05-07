package utilisateurs;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import javax.jdo.Query;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import javax.jdo.PersistenceManager;
import pmf.PMF;;

@PersistenceCapable
public class AppUser {
	
	    @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private String key;
	    
	    @Persistent
	    private String email;
	    
	    @Persistent
	    ArrayList<String> friendList; 
	    
	    @Persistent
	    private Date LastConnection;
	  //constructeur : check si l'utilisateur exist dans la base de donnée, s'il n'existe pas instacie l'utilisateur avec sa key + email extraite de l'objet de type User avec une liste d'ami vide et la date de derniere connexion initialisé a l'instant de la connexion.
	    //si l'utilisateur existe on extrait ses détails de la base de donnée et on insere la date actuelle en date de derniere connexion.
		public AppUser(User user) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(AppUser.class);
		    query.setFilter("email == emailParam");
		    query.declareParameters("String emailParam");
		    @SuppressWarnings("unchecked")
			List<AppUser> results = (List<AppUser>) query.execute(user.getEmail());
		    System.out.println(results.size());
		    if(results.size()==0)
		    {
				this.key = user.getUserId();
				this.email = user.getEmail();
				this.friendList = new ArrayList<String>();
				this.LastConnection = new Date();
				pm.makePersistent(this);
		    }
		    else 
		    	{
		    	AppUser userTemp = pm.getObjectById(AppUser.class, user.getUserId());
		    	this.key = userTemp.key;
		    	this.email = userTemp.email;
		    	this.friendList = userTemp.friendList;
		    	this.LastConnection = new Date();
		    	userTemp.LastConnection = this.LastConnection;
		    	}
		    pm.close();
		}

		// Accessors for the fields. JPA doesn't use these, but your application
		// does.
	public Date getDateLastConnection(){
		return this.LastConnection;
	}
	
	public void modifyDateLastConnection(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		this.LastConnection = new Date();
		myUser.LastConnection = this.LastConnection;
		pm.close();
	}

	public String getAppUserId() {
			return key;
		}
	// retourne 1 si le friendmail n'est pas présent dans la db, 0 si l'ajout réussi, 2 si un probleme dans la db est l'userID de l'ami est présent 2 fois(ne dois pas arriver),3 si jamais l'ami est déja présent dans la liste d'ami,4 si l'utilisateur s'ajoute lui même en ami
	public int addFriend(String friendEmail){
		int valeurRetour = 0;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		if(myUser.email.equals(friendEmail)) // on vérifie que l'utilisateur ne s'ajoute pas lui même en ami
		{
			valeurRetour = 4;
		}
		else
		{
			//Check si l'utilisateur que l'on veut ajouter en ami existe déja dans la base de données
			// en premier on fait la requête
			Query query = pm.newQuery(AppUser.class);
		    query.setFilter("email == emailParam");
		    query.declareParameters("String emailParam");

		    //on execute la requete
			@SuppressWarnings("unchecked")
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
		return this.friendList;
	}
	
	public boolean checkIfFriendExistInListFriend(AppUser myFriend){
		boolean exist = false;
		int i = 0;
		for(i=0;i < this.friendList.size();i++)
		{
			if(this.friendList.get(i).equals(myFriend.getAppUserId()))//a mis 10h a se rendre compte que this.friendList.get(i) == myFriend.getAppUserId() marchais en local mais pas qu'en on importé l'app sur appengine. VDM
			{
				exist = true;
			}
		}
		return exist;
	}
	
}
