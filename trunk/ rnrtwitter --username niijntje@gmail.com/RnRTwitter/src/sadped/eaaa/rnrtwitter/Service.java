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
	
	public void createUser(User u){
		User newUser = new User(u.getUserName(),u.getPassword(), u.getProfileText());
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

	public boolean userNameAvailable(String value) {
		boolean available = true;
		for (User u : registeredUsers){
			if (u.getUserName().equals(value)){
				available = false;
			}
		}
		return available;
	}

	public void saveProfileText(User currentUser) {
		User u = findUser(currentUser);
		u.setProfileText(currentUser.getProfileText());
	}

}
