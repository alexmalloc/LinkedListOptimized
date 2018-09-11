package collections;

import java.lang.reflect.Array;
import java.util.*;

public class LinkedListFork<T> implements List<T> {


    private int size;
    private Node first;
    private Node last;
    private Node[] indexArr = (Node[]) Array.newInstance( Node.class, 100);


    private class Node {
        private T data = null;
        private Node previous = null;
        private Node next = null;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return 0 >= size();
    }

    @Override
    public boolean contains(Object o) {
        Node current = first;
        for(int i = 0; i < size(); i++) {
            if(current.data != null && current.data.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current;

        public LinkedListIterator() {
            current = first;
        }
        @Override
        public boolean hasNext() {
            if(current.next != null) {
                return false;
            }
            return true;
        }

        @Override
        public T next() {
            T temp = current.data;
            current = current.next;
            return temp;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        int var2 = 0;
        for(Node current = this.first; current != null; current = current.next) {
            arr[var2++] = current.data;
        }
        return arr;
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        if (t1s.length < this.size) {
            t1s = (T1[]) Array.newInstance(t1s.getClass().getComponentType(), this.size);
        }

        int index = 0;
        Object[] temp = t1s;

        for(Node current = this.first; current != null; current = current.next) {
            temp[index++] = current.data;
        }

        if (t1s.length > this.size) {
            t1s[this.size] = null;
        }

        return t1s;
    }

    @Override
    public boolean add(T t) {
        if(first == null) {
            first = new Node();
            first.data = t;
            last = first;
            indexArr[size() / 100] = first;
            size++;

            return true;
        }
        last.next = new Node();
        last.next.previous = last;
        last = last.next;
        last.data = t;
        if(size() % 100 == 0) {
            if(size() >= indexArr.length * 100) {
                Node[] oldArr = indexArr.clone();
                indexArr = (Node[]) Array.newInstance( Node.class, indexArr.length * 2);
                System.arraycopy(oldArr, 0, indexArr, 0, oldArr.length);
            }
            indexArr[size() / 100] = last;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node current = first;
        for (int i = 0; i < size(); i++) {
            if (current.data != null && current.data.equals(o)) {
                if(current.previous != null) {
                    current.previous.next = current.next;
                }
                if(current.next != null) {
                    current.next.previous = current.previous;
                }
                if(size() % 100 == 0 && size() < indexArr.length) {
                    indexArr[size() / 100] = null;
                }

                size--;
                if(i == 0) {
                    first = current.next;
                }
                if(i == size()) {
                    last = current.previous;
                }
                return true;
            }
            current = current.next;
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if(collection.size() > size()) {
            return false;
        }

        for (Object item : collection) {
            if(!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for (T item : collection) {
            add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        for(int r = i; r < collection.size(); r++) {
            add(r, collection.iterator().next());
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        for(Object item : collection) {
            remove(item);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        for (Iterator<T> it = iterator(); it.hasNext(); ) {
            Object item = it.next();
            if(!collection.contains(item)) {
                remove(item);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public T get(int i) {
        Node targetNode = findNode(i);
        if (targetNode != null) {
            return targetNode.data;
        }
        return null;
    }

    @Override
    public T set(int i, T t) {
        Node targetNode = findNode(i);
        if (targetNode != null) {
            T temp = targetNode.data;
            targetNode.data = t;
            return temp;
        }
        return null;
    }

    public Node findNode(int i) {
         if(i < size()) {
                    if (i % 100 > 50 && indexArr.length > i/100+1 && indexArr[(i / 100) + 1] != null) {
                        int index = (i / 100) + 1;
                        Node current = indexArr[index];
                        for (int r = index * 100; r >= 0; r--) {
                            if (r == i) {
                                if (current != null) {

                                    return current;
                                }
                                return null;
                            }
                            if (current != null) {
                                current = current.previous;
                            }
                        }
                    } else {
                        int index = i / 100;
                        Node current = indexArr[index];
                        for (int r = index * 100; r < size(); r++) {
                            if (r == i) {
                                if (current != null)
                                    return current;

                                return null;
                            }
                            if (current == null)
                                return null;

                            current = current.next;
                        }
                    }
            }
            return null;
    }

    @Override
    public void add(int i, T t) {

        if(i == size()) {
            add(t);
            return;
        }

        Node targetNode = findNode(i);
        if(targetNode != null) {
            Node previous = targetNode.previous;
            targetNode.previous = new Node();
            previous.next = targetNode.previous;
            targetNode.previous.next = targetNode;
            targetNode.previous.previous = previous;
            targetNode.previous.data = t;
            size++;
            if(size() % 100 == 0) {
                if(size() >= indexArr.length * 100) {
                    Node[] oldArr = indexArr.clone();
                    indexArr = (Node[]) Array.newInstance( Node.class, indexArr.length * 2);
                    System.arraycopy(oldArr, 0, indexArr, 0, oldArr.length);
                }
                indexArr[size() / 100] = last;
            }
            return;
        }

    }

    @Override
    public T remove(int i) {

        if(i == size() - 1) {
            T lastObject = last.data;
            last.previous.next = null;
            last = last.previous;
            size--;
            return lastObject;
        }

        Node targetNode = findNode(i);
        if(targetNode != null) {
            T object = targetNode.data;
            targetNode.previous.next = targetNode.next;
            targetNode.next.previous = targetNode.previous;
            size--;
            return object;
        }
        return null;
    }

    @Override
    public int indexOf(Object o) {
        Node current = first;
        for (int r = 0; r < size(); r++) {
            if (current.data != null && current.data.equals(o)) {
                return r;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        Node current = first;
        for (int r = 0; r < size(); r++) {
            if (current.data != null && current.data.equals(o)) {
                lastIndex = r;
            }
            current = current.next;
        }
        return lastIndex;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new LinkedListIteratorImpl();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return new LinkedListIteratorImpl(i);
    }

    private class LinkedListIteratorImpl implements ListIterator<T> {
        Node current;
        int index;

        public LinkedListIteratorImpl() {
            current = first;
            index = 0;
        }

        public LinkedListIteratorImpl(int position) {
            current = first;
            Node targetNode = findNode(position);
            if(targetNode != null) {
                current = targetNode;
                index = position;
                return;
            }
            index = 0;
        }
        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public T next() {
            T object = current.data;
            current = current.next;
            index++;
            return object;
        }

        @Override
        public boolean hasPrevious() {
            return current.previous != null;
        }

        @Override
        public T previous() {
            T object = current.data;
            current = current.previous;
            index--;
            return object;
        }

        @Override
        public int nextIndex() {
            return index+1;
        }

        @Override
        public int previousIndex() {
            return index-1;
        }

        @Override
        public void remove() {
            LinkedListFork.this.remove(index);
        }

        @Override
        public void set(T t) {
            LinkedListFork.this.set(index, t);
        }

        @Override
        public void add(T t) {
            LinkedListFork.this.add(t);
        }
    }

    @Override
    public List<T> subList(int i, int i1) {
        LinkedListFork<T> newList = new LinkedListFork<>();
        if(i < size()) {
            if(i1 < size()) {
                for(int r = i; r < i1; r++) {
                    newList.add(get(r));
                }
            } else {
                for (int r = i; r < size(); r++) {
                    newList.add(get(r));
                }
            }
        }
        return newList;
    }
}
