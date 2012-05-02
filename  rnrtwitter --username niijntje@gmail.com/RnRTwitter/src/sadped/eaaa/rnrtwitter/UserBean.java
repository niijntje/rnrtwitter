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
	private String userNameSearch; // Til at holde paa indtastning i soegefeltet
	private String newTweetText; // til indtastning af ny tweet p� home.xhtml
	private List<Tweet> displayedTweets;
	private int remainingCharacters;
	private @Inject
	Service service;

	public UserBean() {
		this.setCurrentUser(new User("", "", ""));
		viewedUser = currentUser;
		this.remainingCharacters = 144;
		this.newTweetText = "";

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
//		this.displayedTweets = service.recentTweets(viewedUser, 20);
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
					"Userbean says: Username not available")); // Only there for
																// debugging-purposes
		}
	}

	/**
	 * Faktisk tjekker service.verifyUser(), som bruges nedenfor, allerede på
	 * både username og password. Var der tale om et rigtigt system, hvor
	 * sikkerhed talte med, burde man nok ikke få at vide om et brugernavn
	 * fandtes - udelukkende at en af delene var forkert.
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
		User u = new User(currentUser.getUserName(), (String) value,
				"");
		if (!service.verifyUser(u)) {
			resetCurrentUser();
			throw new ValidatorException(new FacesMessage(
					"Userbean says: Wrong password!"));
		}
	}

	/**
	 * Sørger, ud over at tjekke password, for, at der ikke hænger en gammel
	 * currentUser med dertilhørende private informationer, fast i userBean'en.
	 * 
	 * @return
	 */
	public String verifyUser() {
		System.out.println(currentUser);
		if (service.verifyUser(currentUser)) {
			currentUser = service.getCleanCopy(currentUser);
//			viewedUser = currentUser;
			return "login";
		} else {			 
			return "";
		}

	}

	public User resetCurrentUser() {
		currentUser = new User(currentUser.getUserName(), "", "");
		return currentUser;
	}

	public User resetViewedUser() {
		setViewedUser(new User("", "", ""));
		return viewedUser;
	}

	/**
	 * @return
	 */
	public String createNew() {
		service.createUser(currentUser);
		currentUser = service.getCleanCopy(currentUser);
		if (viewedUser == null || viewedUser.getUserName().equals("")) {
			// Hvis der allerede findes en viewedUser, går vi ud fra, at
			// personen ønsker at
			// se dennes profil igen efter login - altså fortsætte hvor man
			// kom fra
			setViewedUser(service.getCleanCopy(currentUser));
		}
		return "login";
	}
	
	public void createNewTweet(){
		if(loggedIn()){
			service.createNewTweet(newTweetText, currentUser);
			this.newTweetText = "";
		}
	}

	public String logout() {
		resetCurrentUser();
		resetViewedUser();
		return "index";
	}

	public List<String> autoCompleteSearch(String input) {
		List<String> results = new ArrayList<String>();
		List<String> names = new ArrayList<String>(service.getUserNames());

		for (int i = 0; i < names.size(); i++) {
			String match = names.get(i).substring(0, input.length());
			if (match.equalsIgnoreCase(input)) {
				results.add(names.get(i));
			}
		}

		return results;
	}

	public String viewUserProfile() {
		if (!service.userNameAvailable(userNameSearch)) {
			if (userNameSearch.equals(currentUser.getUserName())) {
				setViewedUser(currentUser);
			} else {
				setViewedUser(service.getUserToView(userNameSearch));

			}
			this.userNameSearch = "";
		}
		return "profile";

	}

	public String viewOwnProfile() {
		setViewedUser(currentUser);
		return "profile";
	}

	public boolean ownProfile() {
		return viewedUser.equals(currentUser);
	}

	public boolean otherUsersProfile() {
		return !viewedUser.equals(currentUser);
	}

	public boolean loggedIn() {
		return !currentUser.equals(new User(currentUser.getUserName(), "", ""));
	}

	public boolean NotloggedIn() {
		return currentUser.equals(new User(currentUser.getUserName(), "", ""));
	}

	// -----Profil-opdateringer-----//

	public void saveProfileText() {
		service.saveProfileText(currentUser);
		service.saveRealName(currentUser);
	}

	public void cancelProfileTextEdit() {
		// currentUser.setProfileText(service.getProf);
	}

	public void saveRealName() {
		service.saveRealName(currentUser);
	}

	public void saveEmail() {
		service.saveEmail(currentUser);
	}

	/**
	 * Tænkes brugt når man forlader profilsiden uden at have gemt sine
	 * ændringer
	 */
	public void cancelProfileEdit() {
		currentUser = service.getCleanCopy(currentUser);
	}

	public List<Tweet> recentTweets(){
		System.out.println("UserBean får: "+service.recentTweets(viewedUser, 20));
		return service.recentTweets(viewedUser, 20);
	}

	public String viewUser(Tweet t){
		viewedUser = service.getCleanCopy(t.getUser());
		return "profile";
	}

	// -----.-----//

	public String getUserNameSearch() {
		return userNameSearch;
	}

	public void setUserNameSearch(String userNameSearch) {
		this.userNameSearch = userNameSearch;
	}

	public List<Tweet> getDisplayedTweets() {
		this.displayedTweets = service.recentTweets(viewedUser, 20);
		return displayedTweets;
	}

	public void setDisplayedTweets(List<Tweet> displayedTweets) {
		this.displayedTweets = displayedTweets;
	}


	public String getNewTweetText() {
		return newTweetText;
	}

	public void setNewTweetText(String newTweetText) {
		this.newTweetText = newTweetText;
	}

	
	public List<Tweet> getTweetStream(){
		this.displayedTweets = service.tweetFeed(currentUser, 20);
		return displayedTweets;
	}

	public int getRemainingCharacters() {
		return remainingCharacters-getNewTweetText().length();
	}

	public void setRemainingCharacters(int remainingCharacters) {
		this.remainingCharacters = remainingCharacters;
	}

}
