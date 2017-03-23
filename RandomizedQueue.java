import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    @Override
    public Iterator<Item> iterator() {
        // TODO Auto-generated method stub
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Item next() {
            // TODO Auto-generated method stub
            return null;
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
