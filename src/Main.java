import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String[] topics = {
				"teen",
				"ellen page",
				"pregnancy"
		};
		WikiCrawler wc = new WikiCrawler("/wiki/Juno_(film)", 100, topics, "./output.txt");
		
		try {
			wc.crawl(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
