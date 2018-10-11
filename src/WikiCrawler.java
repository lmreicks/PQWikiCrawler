import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
		public ArrayList<String> edges;
		public ArrayList<WebNode> children;
		public int relevance;
		
		public WebNode(String relativeUrl) {
			this.relativeUrl = relativeUrl;
			this.edges = new ArrayList<String>();
			this.children = new ArrayList<WebNode>();
			this.setHtml("");
			try {
				this.parseHTML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
   private static final Pattern LINK_REGEX = Pattern.compile("href=\"(/wiki/([^\" >#:]*?))\"");
   private Pattern topicRegex;
	

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
		
		String pattern = "\b";
		for (int i = 0; i < topics.length; i++) {
			pattern += topics[i];
			pattern += i != topics.length - 1 ?  "|" : "\b";
		}
		this.topicRegex = Pattern.compile(pattern);
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
			Priority();
		} else {
			BFS();
		}
	}
	
	private void BFS() throws IOException, InterruptedException {
		ArrayList<WebNode> queue = new ArrayList<WebNode>();
		ArrayList<String> discovered = new ArrayList<String>();

		WebNode root = new WebNode(this.seed);
		queue.add(root);
		discovered.add(root.relativeUrl);
		
	    BufferedWriter writer = new BufferedWriter(new FileWriter(this.output));
	    writer.write(this.max);
		
		int requests = 1;
		
		while (!queue.isEmpty()) {
			WebNode vertex = queue.remove(0);
			ArrayList<String> links = this.extractLinks(vertex.html);
			
			for (String link: links) {
				// if this link isn't the node's link (no self-references)
				if (!vertex.relativeUrl.equals(link)) {
					if (!discovered.contains(link) && discovered.size() < this.max) {
						// if its not in the list, make a new node
						WebNode child = new WebNode(link);
						
						// constructing a new node causes the request to be made, so increment
						requests++;
						
						// every 20 requests sleep for 3 seconds
						if (requests % 20 == 0) {
							Thread.sleep(3000);
						}
						
						if (hasTopics(vertex)) {
							discovered.add(link);
							writer.append(vertex.relativeUrl + " " + link + "\n");
							queue.add(child);
						}
					}
					
					// if already in the discovered list and not in list of edges for this node, add it
					if (discovered.contains(link) && !vertex.edges.contains(link)) {
						writer.append(vertex.relativeUrl + " " + link + "\n");
					}	
				}
			}
		}
		
	    writer.close();
	}
	
	private boolean hasTopics(WebNode node) {
		boolean hasAllTopics = topics.length == 0;
		for (String t: topics) {
			hasAllTopics = node.html.contains(t);
		}
		return hasAllTopics;
	}
	
	private void Priority() throws InterruptedException {
		PriorityQ<WebNode> q = new PriorityQ<WebNode>();
		ArrayList<String> discovered = new ArrayList<String>();
		
		WebNode root = new WebNode(this.seed);
		
		if (!hasTopics(root)) {
			return;
		}
		q.add(root, calculateRelevance(root.html));
		
		int pagesVisited = 1;
		
		while (!q.isEmpty() && discovered.size() < this.max) {
			WebNode node = q.extractMax();
			System.out.println(node.relativeUrl);
			ArrayList<String> links = extractLinks(node.html);
			
			for (String link : links) {
				if (!node.relativeUrl.equals(link)) {
					if (!discovered.contains(link)) {
						WebNode child = new WebNode(link);
						pagesVisited++;
						
						// every 20 requests sleep for 3 seconds
						if (pagesVisited % 20 == 0) {
							Thread.sleep(3000);
						}
						
						if (hasTopics(child)) {
							System.out.println(link);
							q.add(child, calculateRelevance(child.html));
							discovered.add(link);	
						}
					}
				}
			}
		}
	}
	
	public int calculateRelevance(String html) {
 	   	HashMap<String, Integer> matches = new HashMap<String, Integer>();
 	   	String[] document = html.split(" ");

 	   	for (String word : document) {
 	   		word = word.toLowerCase();
 	   		if (matches.containsKey(word)) {
 	   			matches.put(word, matches.get(word) + 1);
 	   		} else {
 	   			matches.put(word, 1);
 	   		}
 	   	}
 	   	
 	   	int total = 0;
 	   	for (String topic: topics) {
 	   		topic = topic.toLowerCase().trim();
 	   		if (matches.containsKey(topic)) {
 	   			total += matches.get(topic);
 	   		}
 	   	}
		
	    return total;
	}
}
