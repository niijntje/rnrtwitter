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
	private @Inject
	Service service;

	public UserBean() {
		this.setCurrentUser(new User("", "", ""));
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
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

	public void validateUserName(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String userName = (String) value;

		if (service.userNameAvailable(userName)) {
			throw new ValidatorException(new FacesMessage(
					"Userbean says: Username does not exist"));
		}

	}

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
			return "profile";
		} else
			resetCurrentUser();
			return "index";
	}
	public User resetCurrentUser(){
		currentUser = new User(currentUser.getUserName(), "", "");
		return currentUser;
	}
	
	public String createNew(){
		service.createUser(currentUser);
		currentUser = service.getCleanCopy(currentUser);
		return "profile";
	}


	public String logout() {
		resetCurrentUser();
		return "index";
	}
	
	public List<String> autoCompleteSearch(String s){
		List<String> results = new ArrayList<String>();
		
		results.add("hej");
		
		return results;
	}


	public void saveProfileText(){
		service.saveProfileText(currentUser);
		service.saveRealName(currentUser);
	}

	public void cancelProfileTextEdit(){
//		currentUser.setProfileText(service.getProf);
	}
	
	public void cancelProfileEdit(){
		currentUser = service.getCleanCopy(currentUser);
	}

}
