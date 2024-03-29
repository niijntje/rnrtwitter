package sadped.eaaa.rnrtwitter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class User {

	// Login-info
	private String userName;
	private String password;

	// Profile-info
	private String realName;
	private String email;
	private String profileText;
	private String profilePicFileName;
	// Andre forslag: alder, køn, dato for oprettelse

	private List<Tweet> tweets;
	private List<User> subscriptions;
	private List<Tweet> mentions;



	public User(String userName, String password, String profileText) {
		this.userName = userName;
		this.password = password;
		this.realName = "";
		this.email = "";
		this.profileText = profileText;
		this.tweets = new LinkedList<Tweet>(); // Rita: Jeg foreslaar at vi
		// bruger en linkedList og saa
		// altid indsaetter ved index 0;
		this.mentions = new LinkedList<Tweet>();
		this.subscriptions = new ArrayList<User>();
		subscriptions.add(this);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean verifyPassword(String password) { // Formentlig overflødig
		// pga. equals-metode
		return this.password == password;
	}

	public String getPassword() { // Formentlig overflødig pga. equals-metode
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Tweet addTweet(String text, ArrayList<User> taggedUsers){
		Tweet t = new Tweet(text,this,taggedUsers);

		this.tweets.add(0, t);
		return t;
	}

	public void removeTweet(Tweet t) {
		tweets.remove(t);
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public void addSubscription(User u) {
		subscriptions.add(u);
	}

	public void removeSubscription(User u) {
		subscriptions.remove(u);
	}

	public List<User> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<User> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public String getProfileText() {
		return profileText;
	}

	public void setProfileText(String profileText) {
		this.profileText = profileText;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public Stack<Tweet> getTweetStack(int howMany) {
		Stack<Tweet> tweetStack = new Stack<Tweet>();
			for (int i = Math.min(howMany - 1, tweets.size() - 1); i >= 0; i--) {
				tweetStack.push(tweets.get(i));
			}
		return tweetStack;
	}

	public Stack<Tweet> getMentionStack(int howMany) {
		Stack<Tweet> mentionStack = new Stack<Tweet>();
		for (int i = Math.min(howMany - 1, mentions.size() - 1); i >= 0; i--) {
			mentionStack.push(mentions.get(i));
		}
		return mentionStack;
	}

	public String getProfilePicFileName() {
		return profilePicFileName;
	}

	public void setProfilePicFileName(String profilePicFileName) {
		this.profilePicFileName = profilePicFileName;
	}

	public void addMention(Tweet tweet) {
		mentions.add(0, tweet);

	}

	public List<Tweet> getMentions() {
		return mentions;
	}

	public void setMentions(List<Tweet> mentions) {
		this.mentions = mentions;
	}

}
