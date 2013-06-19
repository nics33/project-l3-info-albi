package utilisateurs;

import java.text.DecimalFormat;
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
	    private ArrayList<String> friendList; 
	    
	    @Persistent
	    private ArrayList<String> demandeEnvoye; 
	    
	    @Persistent
	    private ArrayList<String> demandeRecu; 
	    
	  //constructeur : check si l'utilisateur existe dans la base de donn�es,
	    //s'il n'existe pas, instancie l'utilisateur avec sa key + email extraite de l'objet de type User 
	    //avec une liste d'ami vide et la date de derniere connexion initialis� a l'instant de la connexion.
	    //Si l'utilisateur existe on extrait ses d�tails de la base de donn�es et on ins�re la date actuelle en date de derni�re connexion.
		public AppUser(User user) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(AppUser.class);
		    query.setFilter("email == emailParam");
		    query.declareParameters("String emailParam");
		    @SuppressWarnings("unchecked")
			List<AppUser> results = (List<AppUser>) query.execute(user.getEmail().toLowerCase());
		    if(results.size()==0)
		    {
				this.key = user.getUserId();
				this.email = user.getEmail().toLowerCase();
				this.friendList = new ArrayList<String>();
				this.demandeEnvoye = new ArrayList<String>();
				this.demandeRecu = new ArrayList<String>();
				pm.makePersistent(this);
		    }
		    else 
		    	{
		    	AppUser userTemp = pm.getObjectById(AppUser.class, user.getUserId());
		    	this.key = userTemp.key;
		    	this.email = userTemp.email;
		    	this.friendList = userTemp.friendList;
		    	this.demandeEnvoye = userTemp.demandeEnvoye;
		    	this.demandeRecu = userTemp.demandeRecu;
		    	System.out.println(userTemp.friendList.size());
		    	System.out.println(results.get(0).friendList.size());
		    	}
		    pm.close();
		}

	/***** GETTEURS *****/	
	public String getEmail(){
		return this.email;
	}
	
	public ArrayList<String> getdemandeEnvoye()
	{
		return this.demandeEnvoye;
	}
	
	public ArrayList<String> getdemandeRecu()
	{
		return this.demandeRecu;
	}
	
	public void Modifyfriendlist(ArrayList<String> list)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		this.friendList = list;
		myUser.friendList = list;
		pm.close();
	}
	
	public void ModifydemandeEnvoye(ArrayList<String> list)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		this.demandeEnvoye = list;
		myUser.friendList = list;
		pm.close();
	}
	
	public void ModifydemandeRecu(ArrayList<String> list)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AppUser myUser = pm.getObjectById(AppUser.class, this.key);
		this.demandeRecu = list;
		myUser.demandeRecu = list;
		pm.close();
	}
	
	public String getAppUserId() {
			return key;
		}
	// retourne 1 si le friendmail n'est pas pr�sent dans la db, 0 si l'ajout r�ussi, 2 si un probleme dans la db est l'userID de l'ami est pr�sent 2 fois(ne dois pas arriver),3 si jamais l'ami est d�ja pr�sent dans la liste d'ami,4 si l'utilisateur s'ajoute lui m�me en ami
	public int addFriend(String friendEmail){
		int valeurRetour = 0;
		PersistenceManager pm = PMF.get().getPersistenceManager();
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
	        			AppUser myFriend = pm.getObjectById(AppUser.class, results.get(0).getAppUserId());
	        			this.demandeEnvoye.add(results.get(0).getAppUserId());
	    				//this.friendList.add(results.get(0).getAppUserId());
	    				myUser.demandeEnvoye.add(results.get(0).getAppUserId()); 
	    				myFriend.demandeRecu.add(this.getAppUserId());
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
			if(this.friendList.get(i).equals(myFriend.getAppUserId()))//a mis 10h a se rendre compte que this.friendList.get(i) == myFriend.getAppUserId() marchais en local mais pas qu'en on import� l'app sur appengine. VDM
			{
				exist = true;
			}
		}
		return exist;
	}
	
}
