package sadped.eaaa.rnrtwitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

	public static void main(String[] args) {
		//		User current = new User("Aa", "Bb", "UIOUIPUPP");
		//		User u1 = new User("Aa", "Bb", "CcijjweqjewJP");
		//		User u2 = new User("Gg", "Hh", "Ii");
		//		User u3 = new User("Jj", "Kk", "Ll");
		//		User u4 = new User("Mm", "Nn", "Oo");
		//		Service service = new Service();
		//		service.createUser(u1);
		//		service.createUser(u2);
		//		service.createUser(u3);
		//		service.createUser(u4);
		//
		////		System.out.println(u1);
		//		service.printUsers();
		//		service.saveProfileText(current);
		//		service.printUsers();

		//		Stack<String> stack = new Stack<String>();
		//		stack.push("A");
		//		stack.push("B");
		//		stack.push("C");
		//		stack.push("D");
		//		stack.push("E");
		//		System.out.println(stack);
		//		Iterator<String> it = stack.iterator();
		//		while (it.hasNext()){
		//			System.out.println(it.next());
		//		}

		//		Service service = new Service();
		//		User u = service.getRegisteredUsers().get(0);
		//		
		//		System.out.println(service.tweetFeed(u, 5));

//		String regEx = "@[A-Za-z0-9]+";
//		Pattern pattern = Pattern.compile("@[A-Za-z0-9]+");
//		String inputString = "hihi hej @Rasmus hyp @Rita hov";
//		System.out.println(inputString);
		//		String[] strings = inputString.split(regEx);
		//		System.out.println(strings[0]);
//		Matcher matcher = pattern.matcher(inputString);
		//		matcher.find();
		//		matcher.find();
		//		String group = matcher.group();
		//		System.out.println(group);
		//		matcher.find();
		//		String group2 = matcher.group();
		//		System.out.println(group2);
		//		int i = 0;
		//		while (matcher.find() && i < 5){
		//			System.out.println(matcher.group());
		//			
		//			System.out.println(matcher.group());
		//			System.out.println(inputString);
		//			i++;
		//		}

		//		matcher.find();
		//		matcher.replaceAll("<a href=\"#\">"+matcher.group()+"<\\a>");
		//		System.out.println(inputString);
		//		String resultString = inputString.replaceAll(regEx, "<a href=\"#\">@");
		//		System.out.println(resultString);
		//	
//		String[] theRest = inputString.split(regEx);
//		String[] links = new String[theRest.length];
//		String resultString = "";
//		int i = 0;
//		while (matcher.find()){
//			resultString += matcher.group();
//			links[i] = "<a href=\"profile.xhtml\">"+matcher.group()+"</a>";
//			i++;
//		}
//		System.out.println(resultString);
//		for (int j = 0; j < links.length; j++){
//			resultString += theRest[j]+links[j];
//		}
//		System.out.println(resultString);
//		ArrayList<String> userMatches = new ArrayList<String>();
//		while (matcher.find()){
//			userMatches.add(matcher.group());
//		}
		
//		Service service = new Service();
//		service.createSomeData();
//		User u = service.findUser("Rasmus");
//		Tweet t = service.createNewTweet("En tweet fra @Rasmus hvor @Rita er tagget", u);
////		System.out.println(u.getTweets());
//		User u2 = service.findUser("Rita");
//		System.out.println("Tweets hvor Rita er tagget: "+u2.getMentions());
		
//		String tweetText = "Hej @Rita, nu tagger jeg dig!";
//		tweetText = tweetText.replaceAll("@Rita", "<a href=\"profile.xhtml\">@"+"Rita"+"</a>");
//		System.out.println(tweetText);
	}
}
