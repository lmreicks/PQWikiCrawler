
public class Main {
	public static void main(String[] args) {
		PriorityQ pq = new PriorityQ();
		
		pq.add("You", 17);
		pq.add("When", 15);
		pq.add("I", 20);
		pq.add("Love", 18);
		pq.add("Cookies", 2);
		pq.add("You", 10);
		pq.add("Me", 4);
		pq.add("Only", 19);
		pq.add("Give", 5);
		
		int size = pq.size();
		int[] priorities = pq.priorityArray();
		
		for (int i = 0; i < size - 1; i++) {
			System.out.println(priorities[i]);
		}
	}
}
