package sadped.eaaa.rnrtwitter;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
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
		this.setCurrentUser(new User("", ""));
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
					"Userbean says: Username not available"));
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
		User u = new User(currentUser.getUserName(), (String) value);
		if (!service.verifyUser(u)) {
			throw new ValidatorException(new FacesMessage(
					"Userbean says: Wrong password!"));
		}
	}

	public String verifyUser() {
		System.out.println(currentUser);
		if (service.verifyUser(currentUser)) {
			return "home";
		} else
			return "index";
	}

}
