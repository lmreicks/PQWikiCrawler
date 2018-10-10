import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String[] topics = {};
		WikiCrawler wc = new WikiCrawler("/wiki/Complexity_theory", 100, topics, null);
		
		try {
			wc.root.parseHTML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ArrayList<String> result = wc.extractLinks(wc.root.getHtml());
//		System.out.println(result.size());
//		
//		for (String s: result) {
//			System.out.println(s);
//		}
		
		
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
