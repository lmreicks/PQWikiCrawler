import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {
	public String seed;
	public int max;
	public String[] topics;
	public String output;
	
	class Edge {
		public WebNode source;
		public WebNode dest;
		
		public Edge(WebNode source, WebNode destination) {
			this.source = source;
			this.dest = destination;
		}
	}
	
	class WebNode {
		private String relativeUrl;
		private String html;
		private ArrayList<String> links;
		private ArrayList<WebNode> children;
		private int relevance;
		
		public WebNode(String relativeUrl) {
			this.relativeUrl = relativeUrl;
			this.links = new ArrayList<String>();
			this.children = new ArrayList<WebNode>();
			this.setHtml("");
		}
		
		public void parseHTML() throws IOException {
			URL url = new URL(BASE_URL + relativeUrl);
			InputStream is = url.openStream();
			BufferedReader buff = new BufferedReader(new InputStreamReader(is));
			
		    String line;
		    boolean foundMain = false;
		    while ((line = buff.readLine()) != null) {
		    	if (line.contains("<p>")) foundMain = true;
		    	if (foundMain) {
			    	this.setHtml(this.getHtml().concat(line));	
		    	}
		    }
		    
		    this.links = extractLinks(this.html);
		    
		    for (String link : this.links) {
		    	WebNode child = new WebNode(link);
		    	this.children.add(child);
		    }
		}
		
		public void calculateRelevance() {
	 	   	int matches = 0;

			String pattern = "\b";
			for (int i = 0; i < topics.length; i++) {
				pattern += topics[i];
				pattern += i != topics.length - 1 ?  "|" : "\b";
			}
			final Pattern topicRegex = Pattern.compile(pattern);
	 	   	final Matcher matcher = topicRegex.matcher(this.getHtml());
		    while (matcher.find()) {
		        matches++;
		    }
			
		    this.relevance = matches;
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}
	}
	
	static final String BASE_URL = "https://en.wikipedia.org";
	public WebNode root;

	/**
	 * Matches a string in href="{link}"
	 * That doesn't contain a # or a :
	 */
	// private static final Pattern LINK_REGEX = Pattern.compile("href=\"([^\" >#:]*?)\"");
	   private static final Pattern LINK_REGEX = Pattern.compile("href=\"(/wiki/([^\" >#:]*?))\"");
	

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
		
		this.root = new WebNode(seed);
	}
	
	/**
	 * Returns list of links from the document given
	 * @param document - string representing HTML document
	 * @return - List of strings
	 */
	public ArrayList<String> extractLinks(String document) {
		ArrayList<String> links = new ArrayList<String>();
 	   	final Matcher matcher = LINK_REGEX.matcher(document);
	    while (matcher.find()) {
	        links.add(matcher.group(1));
	    }
		return links;
	}
	
	/**
	 * crawls/explores the web pages starting from the seed URL. Crawl the first max number of pages (including seed page)
	 * that contains every keyword in the Topics list (if topics list is empty then this condition is vacuously considered true),
	 * and are explored started from the seed
	 * @param focused
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void crawl(boolean focused) throws IOException, InterruptedException {
		// TODO
		// a) if focused is false then explore in a BFS fashion
		// b) if focused is true then for every page a, computer Relevance(T, a), and during exploration, instead of adding the pages in the FIFO queue,
			// i) add the pages to and their corresponding relevance (to topic) to priority queue. The priority of the page is its relevance
			// ii) extract elements using extractMax
		// after the crawl is done, the edges explored in the crawl method should be written to the output file
		if (focused) {
			// Priority();
		} else {
			BFS();
		}
	}
	
	private void BFS() throws IOException, InterruptedException {
		ArrayList<WebNode> fifoQueue = new ArrayList<WebNode>();
		ArrayList<Edge> output = new ArrayList<Edge>();
		ArrayList<String> discovered = new ArrayList<String>();

		fifoQueue.add(this.root);
		
		root.parseHTML();
		
		int requests = 0;
		
		while (!fifoQueue.isEmpty() && requests < this.max) {
			WebNode vertex = fifoQueue.remove(0);
			
			if (!hasTopics(vertex)) {
				continue;
			}
			
			for (WebNode child: vertex.children) {
				child.parseHTML();
				requests++;
				
				if (requests % 20 == 0) {
					Thread.sleep(3000);
				}
				
//				Edge e = new Edge(vertex, child);
//				if (hasTopics(child) && !output.contains(e)) {
//					output.add(e);
//				}

				if (!discovered.contains(child.relativeUrl)) {
					discovered.add(child.relativeUrl);
					fifoQueue.add(child);
					output.add(new Edge(vertex, child));
				}
			}
		}
		
		for (Edge node : output) {
			System.out.println(node.source.relativeUrl + "  " + node.dest.relativeUrl);	
		}
	}
	
	private boolean hasTopics(WebNode node) {
		boolean hasAllTopics = topics.length == 0;
		for (String t: topics) {
			hasAllTopics = node.html.contains(t);
		}
		return hasAllTopics;
	}
	
	private void Priority(WebNode[] nodes) {
		PriorityQueue<WebNode> q = new PriorityQueue<WebNode>();
		
		for (WebNode node : nodes) {
			q.add(node);
		}
	}
}
