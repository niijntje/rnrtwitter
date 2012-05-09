package sadped.eaaa.rnrtwitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Service implements Serializable {

	private ArrayList<User> registeredUsers;

	public Service() {
		this.setRegisteredUsers(new ArrayList<User>());
		createSomeData();
	}

	public User createUser(User currentUser) {
		User newUser = new User(currentUser.getUserName(),
				currentUser.getPassword(), currentUser.getProfileText());
		addUser(newUser);
		return newUser;
	}

	public Tweet createNewTweet(String tweetText, User user){
		User realUser = findUser(user);
		String regEx="@[A-Za-z0-9]+";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(tweetText);
		ArrayList<User> taggedUsers = new ArrayList<User>();
		while (matcher.find()){
			String tagged = matcher.group().substring(1);
			User u = findUser(tagged);
			if (u != null){
				taggedUsers.add(u);
			}
		}
		Tweet tweet = realUser.addTweet(tweetText, taggedUsers);
		for (User tagged : taggedUsers){
			tagged.addMention(tweet);
		}
		return tweet;
	}

	public User findUser(String userName){
		User found = null;
		int i = 0;
		while (found == null && i < registeredUsers.size()){
			if (registeredUsers.get(i).getUserName().equalsIgnoreCase(userName)){

				found = registeredUsers.get(i);

			}
			i++;
		}
		return found;
	}

	private User findUser(User u) {
		User realUser = null;
		if (registeredUsers.contains(u)) {
			realUser = registeredUsers.get(registeredUsers.indexOf(u));
		}
		return realUser;
	}

	public void addUser(User u) {
		registeredUsers.add(u);
	}

	public void removeUser(User u) {
		registeredUsers.remove(u);
	}

	public ArrayList<User> getRegisteredUsers() {
		return registeredUsers;
	}

	public void setRegisteredUsers(ArrayList<User> registeredUsers) {
		this.registeredUsers = registeredUsers;
	}

	public List<String> getUserNames() {
		List<String> names = new ArrayList<String>();
		for (User u : registeredUsers) {
			names.add(u.getUserName());
		}
		return names;
	}

	public boolean verifyUser(User u) {
		if (registeredUsers.contains(u)) {
			return true;
		} else
			return false;
	}

	/**
	 * Finder en user ud fra username og password og returnerer en kopi, hvor
	 * alle de andre felter er korrekte eller sat til null. Benyttes af userBean
	 * når en ny bruger logger ind, så gamle user-attributter fra den
	 * foregående ikke 'bliver hængende' i currentUser.
	 * 
	 * @param currentUser
	 * @return
	 */
	public User getCleanCopy(User currentUser) {
		User realUser = findUser(currentUser);
		User u = new User(currentUser.getUserName(), currentUser.getPassword(),
				realUser.getProfileText());
		u.setRealName(realUser.getRealName());
		u.setEmail(u.getEmail());
		u.setSubscriptions(null);
		u.setTweets(null);
		return u;
	}

	/**
	 * Finder en bruger ud fra userName og returnerer en kopi, hvor alle felter
	 * er sat undtagen password. Bruges n�r en "loggedin"-user vil se en anden
	 * users profil
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserToView(String userName) {
		User user = new User(userName, "", "");
		for (User u : this.registeredUsers) {
			if (u.getUserName().equals(userName)) {
				user.setRealName(u.getRealName());
				user.setProfileText(u.getProfileText());
				user.setEmail(u.getEmail());
				user.setSubscriptions(u.getSubscriptions());
				user.setTweets(u.getTweets());
				user.setPassword(u.getPassword());
			}
		}
		return user;
	}

	public boolean userNameAvailable(String value) {
		boolean available = true;
		for (User u : registeredUsers) {
			if (u.getUserName().equals(value)) {
				available = false;
			}
		}
		return available;
	}

	/**
	 * Måske unødigt overhead at service skal søge efter samme user to gange,
	 * men det gør det nemmere at genbruge koden, og fastholder den skarpe
	 * adskillelse mellem userBean og 'rigtige' User-objekter.
	 */
	public void saveRealName(User currentUser) {
		User u = findUser(currentUser);
		u.setRealName(currentUser.getRealName());
	}

	public void saveEmail(User currentUser) {
		User u = findUser(currentUser);
		u.setEmail(currentUser.getEmail());
	}

	public void saveProfileText(User currentUser) {
		User u = findUser(currentUser);
		u.setProfileText(currentUser.getProfileText());
	}

	/**
	 * for-løkken kan udskiftes med den udkommenterede linje, hvis vi opdaterer
	 * equals-metoden i User
	 * 
	 * @param currentUser
	 */
	public void changePassword(User currentUser) {
		// User realUser = findUser(currentUser);
		User realUser = null;
		for (User u : registeredUsers) {
			if (u.getUserName().equals(currentUser.getUserName())) {
				realUser = u;
			}
		}
		if (realUser != null) {
			realUser.setPassword(currentUser.getPassword());
		}
	}

	/**
	 * Til test af kode (især om ændringer er slået igennem helt ned i
	 * service) Det er helt ok at ændre på hvad der bliver udskrevet! :-)
	 */
	public void printUsers() {
		for (User u : registeredUsers) {
			System.out.println(u.getUserName() + ": " + u.getProfileText());
		}
	}

	public List<Tweet> recentTweets(User viewedUser, int howMany) {
		User realUser = findUser(viewedUser);
		if (realUser == null){
			throw new RuntimeException("User not found :-(");
		}
		List<Tweet> tweetsAndMentions = new ArrayList<Tweet>();

		Stack<Tweet> tweetStack = realUser.getTweetStack(howMany);
		Stack<Tweet> mentionStack = realUser.getMentionStack(howMany);

		while (tweetsAndMentions.size() < howMany && tweetStack.size()>0 && mentionStack.size()>0){
			if (tweetStack.peek().compareTo(mentionStack.peek())>0){
				tweetsAndMentions.add(tweetStack.pop());
			}
			else {
				tweetsAndMentions.add(mentionStack.pop());
			}
		}
		while (tweetStack.size()>0 && tweetsAndMentions.size()<howMany){
			tweetsAndMentions.add(tweetStack.pop());
		}
		while (mentionStack.size()>0 && tweetsAndMentions.size()<howMany){
			tweetsAndMentions.add(mentionStack.pop());
		}
		return tweetsAndMentions;
	}

	public List<Tweet> tweetFeed(User currentUser, int howMany){
		List<Tweet> tweetFeed = new ArrayList<Tweet>();
		List<Stack<Tweet>> stacks = new ArrayList<Stack<Tweet>>();
		User realUser = findUser(currentUser);
		if (realUser==null){
			throw new RuntimeException("User not found :-(");
		}
		int tweetsLeft = 0;
		for (User u : realUser.getSubscriptions()){
			Stack<Tweet> st = u.getTweetStack(howMany);
			if (st.size()>0){
				stacks.add(st);
				tweetsLeft += st.size();
			}
		}

		while (tweetFeed.size() < howMany && tweetsLeft>0){
			Stack<Tweet> hasMostRecent = stacks.get(0);
			for (Stack<Tweet> stack : stacks){
				if (stack.peek().compareTo(hasMostRecent.peek()) > 0){
					hasMostRecent = stack;
				}
			}
			tweetFeed.add(hasMostRecent.pop());
			tweetsLeft--;
			if (hasMostRecent.isEmpty()){
				stacks.remove(hasMostRecent);
			}
		}
		return tweetFeed;
	}


	public String getLastTweet(User u) {
		User realUser = findUser(u);
		String lastTweet = " ";
		if (realUser==null){
			//			throw new RuntimeException("User not found :-(");
			lastTweet = "Service says: Didn't find user :-(";
		}
		else if(realUser.getTweets().size()>0){
			lastTweet = realUser.getTweets().get(0).getText();
		}
		return lastTweet;
	}

	public List<User> getSubscriptions(User viewedUser) {
		User realUser = findUser(viewedUser);
		List<User> subscriptions = new ArrayList<User>();
		if (realUser!=null){
			for (User u: realUser.getSubscriptions()){
				subscriptions.add(getCleanCopy(u));
			}
			subscriptions.remove(realUser);
		}
		return subscriptions;
	}

	public void subscribe(User currentUser, User subscription) {
		User realCurrent = findUser(currentUser);
		User realSubscription = findUser(subscription);
		realCurrent.addSubscription(realSubscription);
	}

	public void unSubscribe(User currentUser, User viewedUser) {
		User realCurrent = findUser(currentUser);
		User realViewed = findUser(viewedUser);
		realCurrent.removeSubscription(realViewed);
	}

	public boolean alreadySubscribed(User currentUser, User viewedUser) {
		User realCurrent = findUser(currentUser);
		User realViewed = findUser(viewedUser);
		if (realCurrent!=null && realViewed != null){
			return realCurrent.getSubscriptions().contains(realViewed);
		}
		else return true;
	}

	public boolean notSubscribed(User currentUser, User viewedUser){
		User realCurrent = findUser(currentUser);
		User realViewed = findUser(viewedUser);
		if (realCurrent!=null && realViewed != null){
			return !realCurrent.getSubscriptions().contains(realViewed);
		}
		else return true;
	}




	public void createSomeData(){
		User u1 = createUser(new User("Rasmus", "pw", ""));
		User u2 = createUser(new User("Rita", "pw", ""));
		User u3 = createUser(new User("Jonas", "pw", ""));
		User u4 = createUser(new User("Erik", "pw", ""));
		User u5 = createUser(new User("Jorn", "pw", ""));
		u5.setRealName("J�rn");
		u1.addSubscription(u2);
		u1.addSubscription(u5);
		u2.addSubscription(u1);
		u2.addSubscription(u3);
		u2.addSubscription(u4);
		u3.addSubscription(u2);
		u4.addSubscription(u2);
		u5.addSubscription(u2);
		//		u1.setProfilePicFileName("Rasmus.jpg");
		//		u2.setProfilePicFileName("Rita.png");
		//		u3.setProfilePicFileName("Jonas.jpg");
		//		u4.setProfilePicFileName("Erik.png");
		//		u5.setProfilePicFileName("Jörn.png");

		Tweet t1 = createNewTweet("Min allerfoerste tweet", u1);
		t1.getTime().setHours(t1.getTime().getHours()-10);
		t1 = createNewTweet("Jeg kan ogsaa tweete!", u2);
		t1.getTime().setHours(t1.getTime().getHours()-9);
		t1 = createNewTweet(
				"Og her er MIN anden (eller er det 'mit andet?') tweet :-D", u2);
		t1.getTime().setHours(t1.getTime().getHours()-8);
		t1 = createNewTweet("Min anden tweet - nu vil jeg ogsaa tagge nogen: @Rita", u1);
		t1.getTime().setHours(t1.getTime().getHours()-7);
		t1 = createNewTweet(
				"CNN's Geek Out on Joss: Master of the Whedonverse http://t.co/wb0k3UXG #awesomesauce", u3);
		t1.getTime().setHours(t1.getTime().getHours()-13);
		t1 = createNewTweet("Need a place to store your ideas and projects? We think Wunderkit is the perfect tool for the job: http://t.co/xedRpERf", u3);
		t1.getTime().setHours(t1.getTime().getHours()-12);
		t1 = createNewTweet("Thanks for all your comments on the new mag so far! If you haven't seen the issue yet, it's officially on sale from tomorrow!", u4);
		t1.getTime().setHours(t1.getTime().getHours()-11);
		t1 = createNewTweet("Thinking about becoming a summer organizer? The experience could change your life: http://OFA.BO/sKUzQE #SumOrg12", u5);
		t1.getTime().setHours(t1.getTime().getHours()-10);
		t1 = createNewTweet("Gad vide om mine studerende har fattet noget som helst om traeer...?", u5);
		t1.getTime().setHours(t1.getTime().getHours()-6);
		t1 = createNewTweet("Jeg glaeder mig helt vildt til at se de seje PrimeFaces-projekter i 11V i dag!", u4);
		t1.getTime().setHours(t1.getTime().getHours()-5);
		t1 = createNewTweet("Vi er bare alt for seje her hos RnR!", u1);
		t1.getTime().setHours(t1.getTime().getHours()-4);
		t1 = createNewTweet("RT @Rasmus: Vi er bare alt for seje her hos RnR!", u2);
		t1.getTime().setHours(t1.getTime().getHours()-3);
		t1 = createNewTweet("@Rita Det er fordi vi bruger Mac! ;-)", u1);
		t1.getTime().setHours(t1.getTime().getHours()-2);

	}


}
