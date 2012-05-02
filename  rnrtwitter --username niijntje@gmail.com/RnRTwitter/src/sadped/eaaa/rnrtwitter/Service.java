package sadped.eaaa.rnrtwitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
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
		System.out.println("New user:" + newUser.getUserName() + ". "
				+ newUser.getProfileText());
		System.out.println("Updated user list: " + registeredUsers);
		return newUser;
	}

	public void createNewTweet(String text, User user){
		User realUser = findUser(user);
		realUser.addTweet(text);
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
		User u = findUser(viewedUser);
		System.out.println("User-parameter fra userbean: "+viewedUser);
		System.out.println("Service.findUser: "+ u);
		List<Tweet> tweets = new ArrayList<Tweet>();
		if (u != null){
			tweets = u.getTweets();
			System.out.println(tweets);
		}
		System.out.println(tweets);
		if (tweets.size() < howMany){
			return tweets;
		}
		else
		{
			return tweets.subList(tweets.size()-howMany-1, tweets.size());
		}
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
	
	
	public void createSomeData(){
		User u1 = createUser(new User("Rasmus", "pw", ""));
		User u2 = createUser(new User("Rita", "pw", ""));
		User u3 = createUser(new User("Jonas", "pw", ""));
		User u4 = createUser(new User("Erik", "pw", ""));
		User u5 = createUser(new User("Jörn", "pw", ""));
		u1.addSubscription(u2);
		u2.addSubscription(u1);
		u2.addSubscription(u3);
		u2.addSubscription(u4);
		u2.addSubscription(u5);
		
		createNewTweet("Min allerførste tweet", u1);
		createNewTweet("Jeg kan også tweete!", u2);
		createNewTweet(
				"Og her er MIN anden (eller er det 'mit andet?') tweet :-D", u2);
		createNewTweet("Min anden tweet - nu vil jeg også tagge nogen: @Rita", u1);
		createNewTweet(
				"CNN's Geek Out on Joss: Master of the Whedonverse http://t.co/wb0k3UXG #awesomesauce", u3);
		createNewTweet("Need a place to store your ideas and projects? We think Wunderkit is the perfect tool for the job: http://t.co/xedRpERf", u3);
		createNewTweet("Thanks for all your comments on the new mag so far! If you haven't seen the issue yet, it's officially on sale from tomorrow!", u4);
		createNewTweet("Thinking about becoming a summer organizer? The experience could change your life: http://OFA.BO/sKUzQE #SumOrg12", u5);
		createNewTweet("Gad vide om mine studerende har fattet noget som helst om træer...?", u5);
		createNewTweet("Jeg glæder mig helt vildt til at se de seje PrimeFaces-projekter i 11V i dag!", u4);
		createNewTweet("Vi er bare alt for seje her hos RnR!", u1);
		createNewTweet("RT @Rasmus: Vi er bare alt for seje her hos RnR!", u2);
		createNewTweet("@Rita Det er fordi vi bruger Mac! ;-)", u1);
	}

}
