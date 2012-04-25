package sadped.eaaa.rnrtwitter;

import java.util.ArrayList;
import java.util.List;

public class User {

	//Login-info
	private String userName;
	private String password;
	
	//Profile-info
	private String realName;
	private String email;
	private String profileText;
	//Andre forslag: alder, køn, dato for oprettelse
	
	private List<Tweet> tweets;
	private List<User> subscriptions;

	public User(String userName, String password, String profileText) {
		this.userName = userName;
		this.password = password;
		this.realName = "";
		this.email = "";
		this.profileText = profileText;
		this.tweets = new ArrayList<Tweet>();
		this.subscriptions = new ArrayList<User>();
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean verifyPassword(String password) {	//Formentlig overflødig pga. equals-metode
		return this.password==password;
	}
	public String getPassword(){	//Formentlig overflødig pga. equals-metode
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void addTweet(String text){
		this.tweets.add(new Tweet(text,this));
	}
	public void removeTweet(Tweet t){
		tweets.remove(t);
	}
	public List<Tweet> getTweets() {
		return tweets;
	}
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public void addSubscriptions(User u){
		subscriptions.add(u);
	}
	public void removeSubscription(User u){
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
		System.out.println(this.profileText);
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
	public String toString(){
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

}
