
/**
 * @author Seweryn Fornalik
 * Date: 24-03-2017
 *
 * This assignment is a part of the Algorithm I online course
 * Week 2: Queues, Stacks & Sorting
 *
 * Compilation: javac RandomizedQueue.java
 *
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * The Class RandomizedQueue.
 *
 * @param <Item>
 *            the generic type
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

	/** The Constant DECREASE_RESIZE_FACTOR. */
	private static final int DECREASE_RESIZE_FACTOR = 4;

	/** The Constant RESIZE_FACTOR. */
	private static final int RESIZE_FACTOR = 2;

	/** The m items array. */
	private Item[] mItemsArray;

	/** The m size. */
	private int mSize;

	/**
	 * Instantiates a new randomized queue.
	 */
	public RandomizedQueue() {
		mItemsArray = (Item[]) new Object[1];
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return mSize == 0;
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return mSize;
	}

	/**
	 * Enqueue item.
	 *
	 * @param item
	 *            the item to be enqueued.
	 */
	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException("Null items are not accepted");
		}
		// if not full simply add element, resize and add otherwise
		if (mItemsArray.length == mSize) {
			resizeQueue(mSize * RESIZE_FACTOR);
		}
		mItemsArray[mSize++] = item;
	}

	/**
	 * queueue.
	 *
	 * @return remove and return a random item
	 */
	public Item dequeue() {
		checkQueueNotEmpty();
		final int sample = StdRandom.uniform(mSize);
		Item result = mItemsArray[sample];
		// if removing tail element - do nothing, switch elements otherwise
		if (sample != mSize - 1) {
			mItemsArray[sample] = mItemsArray[mSize - 1];
		}
		mItemsArray[mSize - 1] = null;
		mSize--;
		// check if resizing threshold hit
		if (mSize < mItemsArray.length / DECREASE_RESIZE_FACTOR) {
			resizeQueue(mItemsArray.length / RESIZE_FACTOR);
		}
		return result;

	}

	/**
	 * Sample.
	 *
	 * @return return (but do not remove) a random item
	 */
	public Item sample() {
		checkQueueNotEmpty();
		final int sample = StdRandom.uniform(mSize);
		return mItemsArray[sample];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		RandomizedQueue<String> queue = new RandomizedQueue<>();
		StdOut.println("Is queue empty? " + queue.isEmpty() + " [EXPECTED: TRUE]");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: FALSE]");
		queue.enqueue("1");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: TRUE]");
		queue.enqueue("2");
		queue.enqueue("3");
		queue.enqueue("4");

		debugQueue(queue, "randomly sorted 1 2 3 4");
		StdOut.println("Pick sample element: " + queue.sample());

		queue.dequeue();
		debugQueue(queue, "randomly sorted 1 2 3");
		StdOut.println("Pick sample element: " + queue.sample());

		StdOut.println("Is queue empty? " + queue.isEmpty() + " [EXPECTED: FALSE]");
		StdOut.println("queue size: " + queue.size() + " [EXPECTED: 3]");

		queue.enqueue("5");
		queue.enqueue("6");
		debugQueue(queue, "randomly sorted 1 2 3 5 6");
		StdOut.println("queue size: " + queue.size() + " [EXPECTED: 5]");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: TRUE]");

		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		StdOut.println("queue size: " + queue.size() + " [EXPECTED: 1]");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: TRUE]");

		queue.dequeue();
		StdOut.println("queue size: " + queue.size() + " [EXPECTED: 0]");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: FALSE]");

		queue.enqueue("7");
		StdOut.println("queue size: " + queue.size() + " [EXPECTED: 1]");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: TRUE]");

		queue.dequeue();
		queue.enqueue("8");
		StdOut.println("queue size: " + queue.size() + " [EXPECTED: 1]");
		StdOut.println("Has queue next element? " + queue.iterator().hasNext() + " [EXPECTED: TRUE]");
	}

	private static <Item> void debugQueue(RandomizedQueue<Item> queue, String expValue) {
		StdOut.print("Queue elements: ");
		for (Item item : queue) {
			StdOut.print(item + " ");
		}
		StdOut.println(" [EXPECTED: " + expValue + "]");
	}

	/**
	 * Resize queue.
	 *
	 * @param newLength
	 *            the new length
	 */
	private void resizeQueue(int newLength) {
		Item[] newArray = (Item[]) new Object[newLength];
		for (int i = 0; i < mSize; i++) {
			newArray[i] = mItemsArray[i];
		}
		mItemsArray = newArray;
	}

	/**
	 * Check queue not empty.
	 */
	private void checkQueueNotEmpty() {
		if (mSize == 0) {
			throw new NoSuchElementException("Cannot perform this operation on empty collection");
		}
	}

	/**
	 * The Class RandomizedQueueIterator.
	 */
	private class RandomizedQueueIterator implements Iterator<Item> {

		/** The m not empty items array. */
		private Item[] mNotEmptyItemsArray;

		/** The m current item. */
		private int mCurrentItem = -1;

		/**
		 * Instantiates a new randomized queue iterator.
		 */
		public RandomizedQueueIterator() {
			if (mSize > 0) {
				createShuffledArray();
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return mNotEmptyItemsArray != null && mCurrentItem + 1 != mNotEmptyItemsArray.length;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Item next() {
			checkIfNextElementExists();
			return mNotEmptyItemsArray[++mCurrentItem];
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() method is not supported");
		}

		/**
		 * Check if next element exists.
		 */
		private void checkIfNextElementExists() {
			if (!hasNext()) {
				throw new NoSuchElementException("There is no next element");
			}
		}

		/**
		 * Creates the shuffled array.
		 */
		private void createShuffledArray() {
			if (mSize > 0) {
				mNotEmptyItemsArray = (Item[]) new Object[mSize];
				for (int i = 0; i < mSize; i++) {
					mNotEmptyItemsArray[i] = mItemsArray[i];
				}
				StdRandom.shuffle(mNotEmptyItemsArray);
			}
		}
	}

}
