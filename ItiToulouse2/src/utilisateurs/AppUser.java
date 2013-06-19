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
	    private String nickname; 	    
	    
	    @Persistent
	    private ArrayList<String> friendList; 
	    
	    @Persistent
	    private ArrayList<String> demandeEnvoye; 
	    
	    @Persistent
	    private ArrayList<String> demandeRecu; 
	    
	    
	    
	    public AppUser(String id, String usermail, String usernickname)
	    {
	    	this.key = id;
	    	this.email=usermail.toLowerCase();
	    	this.nickname = usernickname;
			this.friendList = new ArrayList<String>();
			this.demandeEnvoye = new ArrayList<String>();
			this.demandeRecu = new ArrayList<String>();
	    	
	    }

	/***** GETTEURS *****/	
	public String getAppUserId() {
		return key;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getNickname(){
		return this.nickname;
	}
	
	public ArrayList<String> getdemandeEnvoye()
	{
		return this.demandeEnvoye;
	}
	
	public ArrayList<String> getdemandeRecu()
	{
		return this.demandeRecu;
	}
	
	public ArrayList<String> getListFriends(){
		return this.friendList;
	}
	
	public void AddtoFriendlist(String FriendID)
	{
		this.friendList.add(FriendID);
	}
	
	public void AddtodemandeEnvoye(String FriendID)
	{
		this.demandeEnvoye.add(FriendID);
	}
	
	public void AddtodemandeRecu(String FriendID)
	{
		this.demandeRecu.add(FriendID);
	}
	
	public void DeltodemandeEnvoye(String FriendID)
	{
		this.demandeEnvoye.remove(demandeEnvoye.indexOf(FriendID));
	}
	
	public void DeltodemandeRecu(String FriendID)
	{
		this.demandeRecu.remove(demandeRecu.indexOf(FriendID));
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
