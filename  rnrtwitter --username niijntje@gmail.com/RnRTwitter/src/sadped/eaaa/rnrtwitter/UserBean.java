package sadped.eaaa.rnrtwitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author nijntje
 * 
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

	private User currentUser;
	private User viewedUser;
	private @Inject
	Service service;

	public UserBean() {
		this.setCurrentUser(new User("", "", ""));
		this.setViewedUser(currentUser);
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public User getViewedUser() {
		return viewedUser;
	}

	public void setViewedUser(User viewedUser) {
		this.viewedUser = viewedUser;
	}

	public void createNewUser() {
		service.createUser(currentUser);
	}

	/**
	 * Benyttes af index.xhtml ved (forsøg på) oprettelse af ny bruger
	 * 
	 * @author nijntje
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateNewUserName(FacesContext fc, UIComponent c, Object value) {
		String proposedUserName = (String) value;
		if (!service.userNameAvailable(proposedUserName)) {
			throw new ValidatorException(new FacesMessage(
					"Userbean says: Username not available")); //Only there for debugging-purposes
		}
	}

	/**
	 * Faktisk tjekker service.verifyUser(), som bruges nedenfor, allerede på både 
	 * username og password. Var der tale om et rigtigt system, hvor sikkerhed 
	 * talte med, burde man nok ikke få at vide om et brugernavn fandtes - udelukkende
	 * at en af delene var forkert.
	 * 
	 * @param context
	 * @param component
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateUserName(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String userName = (String) value;
		if (service.userNameAvailable(userName)) {
			throw new ValidatorException(new FacesMessage(
					"Userbean says: Username does not exist"));
		}
	}

	/**
	 * @param context
	 * @param component
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateUserPassword(FacesContext context,
			UIComponent component, Object value) throws ValidatorException {
		User u = new User(currentUser.getUserName(), (String) value, "");
		if (!service.verifyUser(u)) {
			throw new ValidatorException(new FacesMessage(
					"Userbean says: Wrong password!"));
		}
	}

	/**
	 * Sørger, ud over at tjekke password, for, at der ikke hænger en gammel currentUser med
	 * dertilhørende private informationer, fast i userBean'en.
	 * 
	 * @return
	 */
	public String verifyUser() {
		System.out.println(currentUser);
		if (service.verifyUser(currentUser)) {
			currentUser = service.getCleanCopy(currentUser);
			return "login";
		} else	//Måske overflødig, da validering (ovenfor) forhindrer, at værdier submittes
			resetCurrentUser();
			return "index";
	}
	public User resetCurrentUser(){
		currentUser = new User(currentUser.getUserName(), "", "");
		return currentUser;
	}
	
	/**
	 * @return
	 */
	public String createNew(){
		service.createUser(currentUser);
		currentUser = service.getCleanCopy(currentUser);
		if (viewedUser==null || viewedUser.getUserName().equals("")){	
			//Hvis der allerede findes en viewedUser, går vi ud fra, at personen ønsker at
			//se dennes profil igen efter login - altså fortsætte hvor man kom fra
			viewedUser = service.getCleanCopy(currentUser);
		}
		return "login";
	}

	public String logout() {
		resetCurrentUser();
		return "index";
	}
	
	public List<String> autoCompleteSearch(String input){
		List<String> results = new ArrayList<String>();
		List<String> names = new ArrayList<String>(service.getUserNames());
		
		for(int i = 0; i < names.size(); i++){
			String match = names.get(i).substring(0, input.length());
			if(match.equalsIgnoreCase(input)){
				results.add(names.get(i));
			}
		}
		
		return results;
	}

	public boolean ownProfile(){
		return viewedUser.equals(currentUser);
	}

	//-----Profil-opdateringer-----//
	public void saveProfileText(){
		service.saveProfileText(currentUser);
		service.saveRealName(currentUser);
	}

	public void cancelProfileTextEdit(){
//		currentUser.setProfileText(service.getProf);
	}
	public void saveRealName(){
		service.saveRealName(currentUser);
	}
	public void saveEmail(){
		service.saveEmail(currentUser);
	}
	
	/**
	 * Tænkes brugt når man forlader profilsiden uden at have gemt sine ændringer
	 */
	public void cancelProfileEdit(){
		currentUser = service.getCleanCopy(currentUser);
	}
	//-----.-----//
}
