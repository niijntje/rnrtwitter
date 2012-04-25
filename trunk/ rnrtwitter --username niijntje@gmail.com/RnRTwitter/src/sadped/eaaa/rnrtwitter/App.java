package sadped.eaaa.rnrtwitter;

public class App {

	public static void main(String[] args) {
		User current = new User("Aa", "Bb", "UIOUIPUPP");
		User u1 = new User("Aa", "Bb", "CcijjweqjewJP");
		User u2 = new User("Gg", "Hh", "Ii");
		User u3 = new User("Jj", "Kk", "Ll");
		User u4 = new User("Mm", "Nn", "Oo");
		Service service = new Service();
		service.createUser(u1);
		service.createUser(u2);
		service.createUser(u3);
		service.createUser(u4);

//		System.out.println(u1);
		service.printUsers();
		service.saveProfileText(current);
		service.printUsers();
	}
}
