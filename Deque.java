/**
 * @author Seweryn Fornalik
 * Date: 23-03-2017
 *
 * This assignment is a part of the Algorithm I online course
 * Week 2: Queues, Stacks & Sorting
 *
 * Compilation: javac Deque.java
 *
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

/**
 * The Class Deque.
 *
 * @param <Item> the generic type
 */

/*
 * A double-ended queue or deque (pronounced "deck") is a generalization of a
 * stack and a queue that supports adding and removing items from either the
 * front or the back of the data structure.
 */

/*
 * Corner cases. Throw a java.lang.NullPointerException if the client attempts
 * to add a null item;
 *
 * throw a java.util.NoSuchElementException if the client attempts to remove an
 * item from an empty deque;
 *
 * throw a java.lang.UnsupportedOperationException if the client calls the
 * remove() method in the iterator;
 *
 * throw a java.util.NoSuchElementException if the client calls the next()
 * method in the iterator and there are no more items to return.
 */

/*
 * Performance requirements. Your deque implementation must support each deque
 * operation in constant worst-case time. A deque containing n items must use at
 * most 48n + 192 bytes of memory. and use space proportional to the number of
 * items currently in the deque.
 *
 * Additionally, your iterator implementation must support each operation
 * (including construction) in constant worst-case time.
 */

public class Deque<Item> implements Iterable<Item> {

    /** The m head. */
    private Node mHead;

    /** The m tail. */
    private Node mTail;

    /** The m size. */
    private int mSize;

    /**
     * Instantiates a new deque.
     */
    public Deque() {
        // construct an empty deque
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
     * Adds the first.
     *
     * @param item the item
     */
    public void addFirst(Item item) {
        verifyItemNotNull(item);
        // add the item to the front
        Node first = new Node(item);
        first.mNextNode = mHead;
        first.mPrevNode = null;

        // collection not empty
        if (mHead != null) {
            mHead.mPrevNode = first;

        } else {
            mTail = first;
        }
        mHead = first;
        mSize++;
    }

    /**
     * Adds the last.
     *
     * @param item the item
     */
    public void addLast(Item item) {
        verifyItemNotNull(item);
        // add the item to the end
        Node last = new Node(item);
        last.mNextNode = null;
        last.mPrevNode = mTail;

        // empty collection
        if (mTail != null) {
            mTail.mNextNode = last;
        } else {
            mHead = last;
        }
        mTail = last;
        mSize++;
    }

    /**
     * Removes the first.
     *
     * @return the item
     */
    public Item removeFirst() {
        // remove and return the item from the front
        if (mSize > 0) {
            Node newFirst = mHead.mNextNode;

            // more than one element
            if (newFirst != null) {
                newFirst.mPrevNode = null;
            } else {
                mTail = null;
            }
            mHead.mNextNode = null;
            Node tmp = mHead;
            mHead = newFirst;
            mSize--;
            return tmp.mItem;
        } else {
            throw new NoSuchElementException("Cannot remove item from empty dequeu!!!");
        }
    }

    /**
     * Removes the last.
     *
     * @return the item
     */
    public Item removeLast() {
        // remove and return the item from the end
        if (mSize > 0) {
            Node newLast = mTail.mPrevNode;

            // more than one element
            if (newLast != null) {
                newLast.mNextNode = null;
            } else {
                mHead = null;
            }
            mTail.mPrevNode = null;
            Node tmp = mTail;
            mTail = newLast;
            mSize--;
            return tmp.mItem;
        } else {
            throw new NoSuchElementException("Cannot remove item from empty dequeu!!!");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeueIterator();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        StdOut.println("Is deque empty? " + deque.isEmpty() + " [EXPECTED: TRUE]");
        deque.addFirst("1");
        deque.addLast("2");
        deque.addFirst("0");
        deque.addLast("3");
        debugDeque(deque, "0 1 2 3");

        StdOut.println("Is deque empty? " + deque.isEmpty() + " [EXPECTED: FALSE]");
        StdOut.println("Deque size: " + deque.size() + " [EXPECTED: 4]");

        deque.removeFirst();
        deque.removeFirst();
        deque.removeLast();
        debugDeque(deque, "2");


    }

    /**
     * Verify item not null.
     *
     * @param item the item
     */
    private void verifyItemNotNull(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item!");
        }
    }

    /**
     * Debug deque.
     *
     * @param <Item> the generic type
     * @param deque the deque
     * @param expValue the exp value
     */
    private static <Item> void debugDeque(Deque<Item> deque, String expValue) {
        StdOut.print("Deque elements: ");
        for (Item item : deque) {
            StdOut.print(item + " ");
        }
        StdOut.println(" [EXPECTED: " + expValue + "]");
    }

    /**
     * The Class Node.
     */
    private class Node {

        /** The m item. */
        private Item mItem;

        /** The m prev node. */
        private Node mPrevNode;

        /** The m next node. */
        private Node mNextNode;

        /**
         * Instantiates a new node.
         *
         * @param item the item
         */
        Node(Item item) {
            mItem = item;
        }
    }

    /**
     * The Class DequeueIterator.
     */
    private class DequeueIterator implements Iterator<Item> {

        /** The m current node. */
        private Node mCurrentNode;

        /**
         * Instantiates a new dequeue iterator.
         */
        public DequeueIterator() {
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return (mCurrentNode == null ? mHead != null : mCurrentNode.mNextNode != null);
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        @Override
        public Item next() {
            if (mCurrentNode == null && mHead != null) {
                mCurrentNode = mHead;
                return mCurrentNode.mItem;
            }
            if (mCurrentNode != null && mCurrentNode.mNextNode != null) {
                mCurrentNode = mCurrentNode.mNextNode;
                return mCurrentNode.mItem;
            }
            // no next element
            throw new NoSuchElementException("No next element that can be returned");
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() method is not supported");
        }
    }
}
