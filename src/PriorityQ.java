import java.util.ArrayList;

public class PriorityQ {
	private ArrayList<PriorityNode<Integer, String>> heap;
	
	public PriorityQ() {
		this.heap = new ArrayList<PriorityNode<Integer, String>>();
		// add a number at the 0th index for testing
		this.heap.add(new PriorityNode<Integer, String>(-1, null));
	}

	/**
	 * Adds a string s with priority p to the priority queue
	 * @param s
	 * @param p
	 */
	public void add(String s, int p) {
		// add a new priority node to the end
		this.heap.add(new PriorityNode<Integer, String>(p, s));
		// heapify from the bottom up
		this.heapifyUp(this.heap.size() - 1);
	}

	/**
	 * returns a string whose priority is maximum
	 * @return
	 */
	public String returnMax() {
		return this.heap.get(1).value();
	}

	/**
	 * returns a string whose priority is maximum and removes if form the queue
	 * @return
	 */
	public String extractMax() {
		PriorityNode<Integer, String> max = this.heap.get(1);
		this.heap.set(1, this.heap.get(this.heap.size() - 1));
		this.heap.remove(this.heap.size() - 1);

		this.heapifyDown(1);
	
		return max.value();
	}

	/**
	 * Removes the element from the priority queue whose array index is i
	 * @param i
	 * @return
	 */
	public String remove(int i) {
		if (i <= 0 || i >= this.heap.size() - 1) {
			throw new ArrayIndexOutOfBoundsException(); 
		}
		// get the value that we are removing
		String temp = this.heap.get(i + 1).value();
		
		// replace this index with the last element
		this.heap.set(i + 1, this.heap.get(this.heap.size() - 1));
		// remove the last element
		this.heap.remove(this.heap.size() - 1);
		// correct the heap
		this.heapifyDown(i + 1);
		
		return temp;
	}

	/**
	 * Decrements the priority of the ith element by k
	 * @param i
	 * @param k
	 */
	public void decrementPriority(int i, int k) {
		// get the node
		PriorityNode<Integer, String> node = this.heap.get(i);
		// set the node's priority to the current priority - k
		node.setKey(node.key() - k);
		
		// correct the heap
		this.heapifyDown(i);
	}

	/**
	 * Returns an array B with the following property: B[i] = key(A[i)
	 * for all i in the array A used to implement the priority queue
	 * @return
	 */
	public int[] priorityArray() {
		int[] temp = new int[this.heap.size()];
		int i = 0;
		for (int index = 1; index < this.heap.size(); index++) {
			temp[i] = this.heap.get(index).key();
			i++;
		}
		return temp;
	}
	
	/**
	 * Returns the size of the heap
	 * @return size of the array
	 */
	public int size() {
		return this.heap.size();
	}
	
	private void heapifyUp(int index) {
		if (index == 1) {
			return; // root
		} else {
			int parentIndex = index % 2 == 0 ? index / 2 : (index - 1) / 2;
			
			PriorityNode<Integer, String> parent = this.heap.get(parentIndex);
			if (parent.key() < this.heap.get(index).key()) {
				swap(parentIndex, index);
				heapifyUp(parentIndex);
			}
		}
	}
	
	private void swap(int x, int y) {
		PriorityNode<Integer, String> temp = this.heap.get(x);
		
		this.heap.set(x, this.heap.get(y));
		this.heap.set(y, temp);
	}
	
	private void heapifyDown(int index) {
		if (index >= this.heap.size()) return;
		
		PriorityNode<Integer, String> left = null;
		if (2 * index < this.heap.size()) {
			left = this.heap.get(2 * index);
		}
		PriorityNode<Integer, String> right = null;
		if (2 * index + 1 < this.heap.size()) {
			right = this.heap.get(2 * index + 1);
		}
		
		PriorityNode<Integer, String> largest = this.heap.get(index);
		int largestIndex = index;
		
		if (left != null && left.key() > largest.key()) {
			largest = left;
			largestIndex = 2 * index;
		}
		if (right != null && right.key() > largest.key()) {
			largest = right;
			largestIndex = 2 * index + 1;
		}
		
		if (largest == this.heap.get(index)) return;
		else {
			this.swap(index, largestIndex);
			this.heapifyDown(largestIndex);
		}
	}
}