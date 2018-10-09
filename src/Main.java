import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		WikiCrawler wc = new WikiCrawler("/wiki/Physics", 10, null, null);
		ArrayList<String> result = wc.extractLinks(wc.seed);
		
		for (String s : result) {
			System.out.println(s);
		}
	}
}
