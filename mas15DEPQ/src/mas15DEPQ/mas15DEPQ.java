package mas15DEPQ;

/**
 * Double ended priority queue
 * 
 * To implement a double-ended priority queue I used dual structure method. In
 * this method we need two different priority queues. One that stores the
 * smallest element at the beginning and the biggest at the end and the second
 * one that keeps the biggest at the beginning. On remove we take the top item
 * from one heap and remove the same item from the other heap. Thus,every
 * element in each PQs needs to know where is the same element in the other
 * queue. To solve this problem each node has got variables called
 * minHeapLocation and maxHeapLocation that keeps indexes of the elements in
 * both PQs.
 * 
 * Both PQs are heaps of Node objects. Each node has got comparable data and
 * indexes of places in min and max heap.
 * 
 * I have made a Heap class and two classes: MinHeap and MaxHeap, that extends
 * Heap class. This classes have to implement compareTo and
 * setSecondHeapLocation methods. CompareTo method is different for min and max
 * heap so we can sort heaps with different order. SetSecondHeapLocation is also
 * different for minHeap and maxHeap because minHeap must change variable called
 * maxHeapLocation and maxHeap has to change minHeapLocation.
 * \
 * 
 * @author Mateusz Stankiewicz
 *
 */
public class mas15DEPQ implements DEPQ {

	/**
	 * Two object of Heap class as min and max heaps.
	 */
	private Heap minHeap;
	private Heap maxHeap;

	/**
	 * Inner class for node objects. Each node consist of data(which is
	 * Comparable) and index of location in min and max heaps.
	 * 
	 */
	class Node {
		Comparable data;
		int minHeapLocation, maxHeapLocation;

		/**
		 * Method that compares data of this node to data of a node passed as an
		 * argument.
		 * 
		 * @param no
		 * @return negative int when actual node's data is bigger than data of
		 *         passed one, positive int when is lower, 0 when they are equal
		 */
		public int compareTo(Node no) {
			return this.data.compareTo(no.data);
		}

		/**
		 * Node constructor that gets data and min, max locations as parameters
		 * 
		 * @param data
		 * @param minHeapLocation
		 *            Location in min heap
		 * @param maxHeapLocation
		 *            Location of max heap
		 */
		public Node(Comparable<?> data, int minHeapLocation, int maxHeapLocation) {
			this.data = data;
			this.minHeapLocation = minHeapLocation;
			this.maxHeapLocation = maxHeapLocation;
		}

		/**
		 * Sets location in min heap
		 * 
		 * @param i
		 *            location in min heap
		 */
		public void setminHeapLocation(int i) {
			this.minHeapLocation = i;
		}

		/**
		 * Sets location in max heap
		 * 
		 * @param i
		 *            location in max heap
		 */
		public void setmaxHeapLocation(int i) {
			this.maxHeapLocation = i;
		}
	}

	/**
	 * Heap class
	 * 
	 * Tree-based data structure. The smallest element is on the top (first in
	 * array)
	 * 
	 * @author Mateusz Stankiewicz
	 *
	 */
	abstract class Heap {
		/**
		 * Max size of the array. When the array is full, maxsize will be
		 * incremented and the array will be expanded
		 */
		int maxsize;

		/**
		 * Number of elements on heap array
		 */
		int size;

		/**
		 * Array of Node type
		 */
		Node heap[];

		/**
		 * Constructor of heap object with maximal size of an array as an
		 * argument
		 * 
		 * @param maxsize
		 *            maximal size of the heap array
		 */
		public Heap(int maxsize) {
			this.heap = new Node[maxsize];
			this.maxsize = maxsize;
			this.size = 0;
		}

		/**
		 * Inspects the first node of the heap
		 * 
		 * @return first node in the heap
		 */
		public Node inspectFirstNode() {
			if (size > 0)
				return heap[0];
			return null;
		}

		/**
		 * Expand method to increase the size of the backing array This method
		 * is the same as this which was on the lectures
		 */
		private void expand() {
			int newmax = heap.length * 2;
			Node[] newHeap = new Node[newmax];
			System.arraycopy(heap, 0, newHeap, 0, heap.length);
			heap = newHeap;
		}

		/**
		 * Add a node to the heap
		 * 
		 * @param n
		 *            the node to add to the heap
		 */
		public void add(Node n) {
			size++;
			// expand if size is smaller that max size of the array
			if (size > heap.length) {
				expand();
			}
			heap[size - 1] = n;
			// bubble up new node to place it in a correct place in the heap
			bubbleUp(size - 1);
		}

		/**
		 * Swaps current node with its parent node when parent is bigger and
		 * current node is not a root
		 * 
		 * @param curr
		 *            index of node to bubble up
		 */
		private void bubbleUp(int curr) {
			// calculate parent position
			int parent = (curr - 1) / 2;

			// continue swapping when current node is not a root and it is
			// smaller than parent
			while (curr > 0 && (compare(parent, curr) > 0)) {
				// swap the current node with its parent
				swap(curr, parent);
				// set index of current node to its parent so can bubble up
				// upper nodes
				curr = parent;
				// calculate parent index for new current node
				parent = (curr - 1) / 2;
			}
		}

		/**
		 * Swaps two heap nodes at the given positions.
		 * 
		 * @param child
		 *            index of first node
		 * @param parent
		 *            index of second node
		 */
		private void swap(int child, int parent) {
			// swaps locations to inform second node of a new position (inform
			// max heap about a new position in the min heap and vice versa )
			setSecondHeapLocation(child, parent);
			setSecondHeapLocation(parent, child);
			// swap nodes
			Node temp = heap[child];
			heap[child] = heap[parent];
			heap[parent] = temp;
		}

		/**
		 * Removes a node and fixes the order of the heap after deletion
		 * 
		 * @param nodeLocation
		 *            index of the node to remove
		 */
		private void remove(int nodeLocation) {
			if (size > 0)
				size--;
			else
				return;
			// check if it was the last one, if was the last then there is no
			// need to fix the order
			if (nodeLocation == size) {
				heap[nodeLocation] = null;
			} else {
				// replace removed object with the last one
				Node last = heap[size];
				heap[size] = null;
				heap[nodeLocation] = last;
				setSecondHeapLocation(nodeLocation, nodeLocation);

				// while parent of the new node (that replaced removed one) is
				// smaller we need to bubble up
				bubbleUp(nodeLocation);
				// if its smaller than parent then bubble down
				bubbleDown(nodeLocation);
			}
			return;
		}

		/**
		 * Sets index of location in a second heap This method will be
		 * implemented by min and max heaps
		 * 
		 * @param whichNode
		 *            is the index of node that we want to set new location
		 * @param newLocation
		 *            is new location of node in a second heap
		 */
		abstract void setSecondHeapLocation(int whichNode, int newLocation);

		/**
		 * Swaps current node with its children nodes when they are smaller
		 * 
		 * @param nodeLocation
		 *            index of a node to bubble down
		 */// The DEPQ constructor
		private void bubbleDown(int nodeLocation) {

			while (true) {
				// Choose the least of the left or right children
				int child;
				int left = nodeLocation * 2 + 1, right = nodeLocation * 2 + 2;

				if (left >= size) // if there is no children
					break;

				if (right >= size)
					child = left;
				else if (compare(left, right) <= 0)
					child = left;
				else
					child = right;
				// Swap and continue or return
				if (compare(nodeLocation, child) > 0) {
					// parent(actual node) is bigger than child so swap
					swap(child, nodeLocation);
					nodeLocation = child;
				} else
					// The parent(actual node) isn't bigger so we can stop
					// bubbling
					break;
			}
		}

		/**
		 * Compares one node to the second one
		 */
		abstract int compare(int n1, int n2);

	}

	/**
	 * Class of min heap that extends Heap class
	 *
	 * @author Mateusz Stankiewicz
	 *
	 */
	class MinHeap extends Heap {

		public MinHeap(int maxsize) {
			super(maxsize);
		}

		/**
		 * Sets index of location in min heap
		 * 
		 * @param whichNode
		 *            is the index of node that we want to set new minLocation
		 * @param newLocation
		 *            is new location of node in min heap
		 */
		void setSecondHeapLocation(int whichNode, int newLocation) {
			heap[whichNode].setminHeapLocation(newLocation);
		}

		/**
		 * Compares one node to the second one
		 * 
		 * @param n1
		 *            index of the first node to compare
		 * @param n2
		 *            index of the second node to compare
		 * @return negative int when first node's is bigger then second one,
		 *         positive int when is lower, 0 when they are equal
		 */
		int compare(int n1, int n2) {
			return heap[n1].compareTo(heap[n2]);
		}
	}

	/**
	 * Class of min heap that extends Heap class
	 * 
	 * CompareTo method works inversely so returns negative int when first
	 * node's is lower then second one and positive int when is bigger
	 * 
	 * Thus,the heap will be sorted that the biggest elements will be at the
	 * beginning and smallest at the end.
	 * 
	 * @author Mateusz Stankiewicz
	 *
	 */
	class MaxHeap extends Heap {

		public MaxHeap(int maxsize) {
			super(maxsize);
		}

		/**
		 * Sets index of location in max heap
		 * 
		 * @param whichNode
		 *            is the index of node that we want to set new maxLocation
		 * @param newLocation
		 *            is new location of node in max heap
		 */
		void setSecondHeapLocation(int whichNode, int newLocation) {
			heap[whichNode].setmaxHeapLocation(newLocation);
		}

		/**
		 * Compares one node to the second one
		 * 
		 * @param n1
		 *            is an index of the first node to compare
		 * @param n2
		 *            is an index of the second node to compare
		 * @return negative int when first node's is lower then second one,
		 *         positive int when is bigger, 0 when they are equal
		 */
		int compare(int n1, int n2) {
			return heap[n1].compareTo(heap[n2]) * (-1);
		}

	}

	/**
	 * The DEPQ constructor
	 * 
	 * @param maxsize
	 */
	public mas15DEPQ(int maxsize) {
		minHeap = new MinHeap(maxsize);
		maxHeap = new MaxHeap(maxsize);
	}

	/**
	 * The DEPQ constructor without maxsize as an argument Sets max size to 20
	 */
	public mas15DEPQ() {
		minHeap = new MinHeap(50);
		maxHeap = new MaxHeap(50);
	}

	/**
	 * Inspect but don't remove the smallest element
	 * 
	 * This method takes O(1) time because it just returns the first element
	 * from the array
	 * 
	 * @return the smallest element in the DEPQ
	 */
	public Comparable<?> inspectLeast() {
		// inspectLeast works by inspecting first node in the min heap
		Node n = minHeap.inspectFirstNode();
		return n.data;
	}

	/**
	 * Inspect but don't remove the biggest element
	 * 
	 * This method takes O(1) time because it just returns the first element
	 * from the array
	 * 
	 * @return the biggest element in the DEPQ
	 */
	public Comparable<?> inspectMost() {
		// inspectLeast works by inspecting first node in the max heap
		Node n = maxHeap.inspectFirstNode();
		return n.data;
	}

	/**
	 * Add new item to the DEPQ
	 * 
	 * Time complexity of this method is O(log n) because it has to bubble up a
	 * node in min and max heaps. We have two heaps so it is O(log n)+O(log n).
	 * So adding takes O(log n) time.
	 * 
	 * @param c
	 *            the item to add to the DEPQ
	 */
	public void add(Comparable c) {

		int size = minHeap.size;
		// create new node and set minHeapLocation and maxHeapLocation to
		// size(number of elements in the heap)
		// so if there is already 4 items, new node will have
		// minHeapLocation = 5 and maxHeapLocation = 5
		Node newNode = new Node(c, size, size);
		// add new node to the maxheap and minheap
		// add method in min and max heaps will place new node in the right
		// place in these heaps
		minHeap.add(newNode);
		maxHeap.add(newNode);
	}

	/**
	 * Remove the smallest element in the DEPQ and return it
	 * 
	 * Time complexity of this method is O(log n) because it just takes first
	 * element from array in MinHeap, removes it and bubbles down. Bubbling down
	 * takes O(log n) time. In maxHeap it just take one of the leafs and replace
	 * it with the last item(if it was not the last one). Then this replaced
	 * node needs to be bubbled up and down. Bubbling up and down takes O(log n)
	 * time each. So the complexity of the all the method will take 3*log n so
	 * it will be O(log n).
	 * 
	 * @return removes and returns the smallest element in the DEPQ
	 */
	public Comparable<?> getLeast() {
		if (minHeap.size == 0)
			return null;
		// inspect first element in min Heap
		Node first = minHeap.inspectFirstNode();
		// remove node from min Heap
		minHeap.remove(0);
		// remove this node from max Heap
		maxHeap.remove(first.maxHeapLocation);
		// return the smallest element in DEPQ
		return first.data;
	}

	/**
	 * Remove the biggest element in the DEPQ and return it
	 * 
	 * Time complexity of this method is O(log n) because it just takes first
	 * element from array in maxHeap, removes it and bubbles down. Bubbling down
	 * takes O(log n) time. In minHeap it just take one of the leafs and replace
	 * it with the last item(if it was not the last one). Then this replaced
	 * node needs to be bubbled up and down. Bubbling up and down takes O(log n)
	 * time each. So the complexity of the all the method will take 3*log n so
	 * it will be O(log n).
	 * 
	 * @return removes and returns the biggest element in the DEPQ
	 */
	public Comparable<?> getMost() {
		if (maxHeap.size == 0)
			return null;
		// inspect first element in max Heap
		Node first = maxHeap.inspectFirstNode();
		// remove node from max Heap
		maxHeap.remove(0);
		// remove this node from min Heap
		minHeap.remove(first.minHeapLocation);
		// return the biggest element in DEPQ
		return first.data;
	}

	/**
	 * Check if the heap is empty
	 * 
	 * This method takes O(1) time because it just takes a size and does not go
	 * through any loops
	 */
	public boolean isEmpty() {
		return minHeap.size == 0;
	}

	/**
	 * Return the number of elements in the heap
	 * 
	 * This method takes O(1) time because it just takes a size and does not go
	 * through any loops
	 */
	public int size() {
		return minHeap.size;
	}

}
