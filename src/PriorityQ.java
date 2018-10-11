import java.util.ArrayList;

public class PriorityQ<T> {
	private ArrayList<PriorityNode<Integer, T>> heap;
	
	public PriorityQ() {
		this.heap = new ArrayList<PriorityNode<Integer, T>>();
		// add a number at the 0th index for testing
		this.heap.add(new PriorityNode<Integer, T>(-1, null));
	}

	/**
	 * Adds a string s with priority p to the priority queue
	 * @param s
	 * @param p
	 */
	public void add(T s, int p) {
		// add a new priority node to the end
		this.heap.add(new PriorityNode<Integer, T>(p, s));
		// heapify from the bottom up
		this.heapifyUp(this.heap.size() - 1);
	}

	/**
	 * returns a string whose priority is maximum
	 * @return
	 */
	public T returnMax() {
		return this.heap.get(1).value();
	}

	/**
	 * returns a string whose priority is maximum and removes if form the queue
	 * @return
	 */
	public T extractMax() {
		PriorityNode<Integer, T> max = this.heap.get(1);
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
	public T remove(int i) {
		if (i <= 0 || i >= this.heap.size() - 1) {
			throw new ArrayIndexOutOfBoundsException(); 
		}
		// get the value that we are removing
		T temp = this.heap.get(i).value();
		
		// replace this index with the last element
		this.heap.set(i, this.heap.get(this.heap.size() - 1));
		// remove the last element
		this.heap.remove(this.heap.size() - 1);
		// correct the heap
		this.heapifyDown(i);
		
		return temp;
	}

	/**
	 * Decrements the priority of the ith element by k
	 * @param i
	 * @param k
	 */
	public void decrementPriority(int i, int k) {
		// get the node
		PriorityNode<Integer, T> node = this.heap.get(i);
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
	 * Returns the key(A[i])
	 * where A is the array used to represent the priority queue
	 * @param i - index of array
	 * @return key
	 */
	public int getKey(int i) {
		return this.heap.get(i).key();
	}
	
	/**
	 * Returns value(A[i])
	 * where A is the array used to represent the priority queue
	 * @param i
	 * @return
	 */
	public T getValue(int i) {
		return this.heap.get(i).value();
	}
	
	/***
	 * Returns true if and only if the queue is empty
	 * @return
	 */
	public boolean isEmpty() {
		return this.heap.size() <= 1;
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
			
			PriorityNode<Integer, T> parent = this.heap.get(parentIndex);
			if (parent.key() < this.heap.get(index).key()) {
				swap(parentIndex, index);
				heapifyUp(parentIndex);
			}
		}
	}
	
	private void swap(int x, int y) {
		PriorityNode<Integer, T> temp = this.heap.get(x);
		
		this.heap.set(x, this.heap.get(y));
		this.heap.set(y, temp);
	}
	
	private void heapifyDown(int index) {
		if (index >= this.heap.size()) return;
		
		PriorityNode<Integer, T> left = null;
		if (2 * index < this.heap.size()) {
			left = this.heap.get(2 * index);
		}
		PriorityNode<Integer, T> right = null;
		if (2 * index + 1 < this.heap.size()) {
			right = this.heap.get(2 * index + 1);
		}
		
		PriorityNode<Integer, T> largest = this.heap.get(index);
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
