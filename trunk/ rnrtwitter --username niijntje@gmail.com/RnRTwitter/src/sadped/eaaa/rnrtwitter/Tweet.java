package sadped.eaaa.rnrtwitter;

import java.util.ArrayList;
import java.util.Date;


public class Tweet implements Comparable<Tweet>{
	
	private String text;
	private User user;
	private Date time;
	private ArrayList<User> taggedUsers;
	
	public Tweet(String text, User user, ArrayList<User> taggedUsers) {
		this.text = text;
		this.user = user;
		this.time = new Date();
		this.setTaggedUsers(taggedUsers);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString(){
		return text;
	}

	@Override
	public int compareTo(Tweet otherTweet) {
		return this.time.compareTo(otherTweet.getTime());
	}

	public ArrayList<User> getTaggedUsers() {
		return taggedUsers;
	}

	public void setTaggedUsers(ArrayList<User> taggedUsers) {
		this.taggedUsers = taggedUsers;
	}
}
