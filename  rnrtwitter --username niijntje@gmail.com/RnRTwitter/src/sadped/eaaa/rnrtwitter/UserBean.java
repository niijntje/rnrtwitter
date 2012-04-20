package sadped.eaaa.rnrtwitter;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class UserBean implements Serializable {

	private User currentUser;
	private @Inject Service service;
	
	public UserBean(){
		this.setCurrentUser(new User("", ""));
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public void createNewUser(){
		service.createUser(currentUser);
	}
	
	public void validateNewUserName(FacesContext fc, UIComponent c, Object value){
		String proposedUserName = (String) value;
		if (!service.userNameAvailable(proposedUserName)){
		      throw new ValidatorException(
		         new FacesMessage("Userbean says: Username not available"));
		}
	}
}
