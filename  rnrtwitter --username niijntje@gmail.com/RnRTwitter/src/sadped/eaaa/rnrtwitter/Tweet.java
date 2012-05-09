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

	public ArrayList<User> getTaggedUsers() {
		return taggedUsers;
	}

	public void setTaggedUsers(ArrayList<User> taggedUsers) {
		this.taggedUsers = taggedUsers;
	}

	@Override
	public String toString(){
		return text;
	}

	@Override
	public int compareTo(Tweet otherTweet) {
		return this.time.compareTo(otherTweet.getTime());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
