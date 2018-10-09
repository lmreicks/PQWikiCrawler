import java.util.ArrayList;

public class WikiCrawler {

	/**
	 * Crawls wiki pages
	 * @param seed - related address of seed URL (with wiki domain)
	 * @param max - maximum number of pages to consider 
	 * @param topics - array of strings representing keyword in a topic list 
	 * @param output - string representing the filename where the web graph over discoved pages are written
	 */
	public WikiCrawler(String seed,
						int max,
						String[] topics,
						String output) {
		
	}
	
	/**
	 * Returns list of links from the document given
	 * @param document - string representing HTML document
	 * @return - List of strings
	 */
	public ArrayList<String> extractLinks(String document) {
		// TODO
		// a) extract only relative addresses of wiki links, i.e. only links that are of the form /wiki/XXX
		// b) only extract links that appear after the first occurance of the html tag <p>
		// c) Must not extract any wiki link that contains characters such as # or :
		// d) the order in which the links in the returned array must be exactly the same order in which they appear in the document
		return null;
	}
	
	/**
	 * crawls/explores the web pages starting from the seed URL. Crawl the first max number of pages (including seed page)
	 * that contains every keyword in the Topics list (if topics list is empty then this condition is vacuously considered true),
	 * and are explored started from the seed
	 * @param focused
	 */
	public void crawl(boolean focused) {
		// TODO
		// a) if focused is false then explore in a BFS fashion
		// b) if focused is true then for every page a, computer Relevance(T, a), and during exploration, instead of adding the pages in the FIFO queue,
			// i) add the pages to and their corresponding relevance (to topic) to priority queue. The priority of the page is its relevance
			// ii) extract elements using extractMax
		// after the crawl is done, the edges explored in the crawl method should be written to the output file
	}
	
	
}
