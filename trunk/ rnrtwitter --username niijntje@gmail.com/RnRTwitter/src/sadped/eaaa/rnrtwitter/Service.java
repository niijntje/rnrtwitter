package sadped.eaaa.rnrtwitter;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Service implements Serializable{

	private ArrayList<User> registeredUsers;

	public Service(){
		this.setRegisteredUsers(new ArrayList<User>());
		registeredUsers.add(new User("Rasmus", "pw", ""));
		registeredUsers.add(new User("Rita", "pw", ""));
	}

	public void createUser(User currentUser){
		User newUser = new User(currentUser.getUserName(),currentUser.getPassword(), currentUser.getProfileText());
		addUser(newUser);
		System.out.println("New user:"+newUser.getUserName()+". "+newUser.getProfileText());
		System.out.println("Updated user list: "+registeredUsers);

	}

	private User findUser(User u){
		User realUser = null;
		if (registeredUsers.contains(u)){
			realUser = registeredUsers.get(registeredUsers.indexOf(u));
		}
		return realUser;
	}

	public void addUser(User u){
		registeredUsers.add(u);
	}

	public void removeUser(User u){
		registeredUsers.remove(u);
	}

	public ArrayList<User> getRegisteredUsers() {
		return registeredUsers;
	}

	public void setRegisteredUsers(ArrayList<User> registeredUsers) {
		this.registeredUsers = registeredUsers;
	}

	public boolean verifyUser(User u){
		if (registeredUsers.contains(u)){
			return true;
		}
		else return false;
	}

	/**
	 * Finder en user ud fra username og password og returnerer en kopi,
	 * hvor alle de andre felter er korrekte eller sat til null.
	 * Benyttes af userBean når en ny bruger logger ind, så gamle user-attributter
	 * fra den foregående ikke 'bliver hængende' i currentUser.
	 * 
	 * @param currentUser
	 * @return
	 */
	public User getCleanCopy(User currentUser){
		User realUser = findUser(currentUser);
		User u = new User(currentUser.getUserName(), currentUser.getPassword(),realUser.getProfileText());
		u.setRealName(realUser.getRealName());
		u.setSubscriptions(null);
		u.setTweets(null);
		return u;
	}

	public boolean userNameAvailable(String value) {
		boolean available = true;
		for (User u : registeredUsers){
			if (u.getUserName().equals(value)){
				available = false;
			}
		}
		return available;
	}

	/**
	 * Måske unødigt overhead at service skal søge efter samme user to gange, men
	 * det gør det nemmere at genbruge koden, og fastholder den skarpe adskillelse
	 * mellem userBean og 'rigtige' User-objekter.
	 */
	public void saveRealName(User currentUser) {
		User u = findUser(currentUser);
		u.setRealName(currentUser.getRealName());
	}	
	public void saveEmail(User currentUser){
		User u = findUser(currentUser);
		u.setEmail(currentUser.getEmail());
	}
	public void saveProfileText(User currentUser) {
		User u = findUser(currentUser);
		u.setProfileText(currentUser.getProfileText());
	}

	/**
	 * Til test af kode (især om ændringer er slået igennem helt ned i service)
	 * Det er helt ok at ændre på hvad der bliver udskrevet! :-)
	 */
	public void printUsers(){
		for (User u: registeredUsers){
			System.out.println(u.getUserName()+": "+u.getProfileText());
		}
	}


}
