import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String[] topics = {};
		WikiCrawler wc = new WikiCrawler("/wiki/Complexity_theory", 100, topics, "./output.txt");
		
		try {
			wc.crawl(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
