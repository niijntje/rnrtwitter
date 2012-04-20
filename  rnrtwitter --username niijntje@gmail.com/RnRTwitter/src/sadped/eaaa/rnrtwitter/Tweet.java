package sadped.eaaa.rnrtwitter;

import java.util.Date;


public class Tweet {
	
	private String text;
	private User user;
	private Date time;
	
	public Tweet(String text, User user) {
		this.text = text;
		this.user = user;
		this.time = new Date();
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
}
