import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {
	static final String BASE_URL = "https://en.wikipedia.org";
	private Queue<String> fifoQueue;
	private ArrayList<String> discovered;

	/**
	 * Matches a string in href="{link}"
	 * That doesn't contain a # or a :
	 */
	// private static final Pattern LINK_REGEX = Pattern.compile("href=\"(/wiki/(?!#)(.+?))\"");
	private static final Pattern LINK_REGEX = Pattern.compile("href=\"([^\" >#:]*?)\"");
	
	public String seed;
	public int max;
	public String[] topics;
	public String output;

	/**
	 * Crawls wiki pages
	 * @param seed - related address of seed URL (with wiki domain)
	 * @param max - maximum number of pages to consider 
	 * @param topics - array of strings representing keyword in a topic list 
	 * @param output - string representing the filename where the web graph over discovered pages are written
	 * @throws IOException 
	 */
	public WikiCrawler(String seed,
						int max,
						String[] topics,
						String output) {
		this.seed = seed;
		this.max = max;
		this.topics = topics;
		this.output = output;
		
		this.fifoQueue = new LinkedList<String>();
		this.discovered = new ArrayList<String>();
	}
	
	/**
	 * Returns list of links from the document given
	 * @param document - string representing HTML document
	 * @return - List of strings
	 */
	public ArrayList<String> extractLinks(String document) {
	    ArrayList<String> links = new ArrayList<String>();
		BufferedReader buff;
		try {
			buff = openConnection(document);
		    String line;
		    
		    boolean foundMain = false;
		    while ((line = buff.readLine()) != null) {
		    	// this is where the footer starts on the wikipedia pages
		    	// after this it will only contain help links that we don't want
		    	if (line.contains("id=\"mw-navigation\"")) break;
		    	if (!foundMain && line.contains("<p>")) {
		    		foundMain = true;
		    	}
		    	if (foundMain) {
		     	   final Matcher matcher = LINK_REGEX.matcher(line);
		    	    while (matcher.find()) {
		    	        links.add(matcher.group(1));
		    	    }	
		    	}
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return links;
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
		if (focused) {
			Priority();
		} else {
			BFS();
		}
	}
	
	private BufferedReader openConnection(String relativeUrl) throws IOException {
		URL url = new URL(BASE_URL + relativeUrl);
		InputStream is = url.openStream();
		return new BufferedReader(new InputStreamReader(is));
	}
	
	private void BFS() {
		
	}
	
	private void Priority() {
		
	}

	private int calculateRelevance(String[] T, String address) {
		BufferedReader buff;
		try {
			buff = openConnection(address);
			
			String line;
			while ((line = buff.readLine()) != null) {
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
