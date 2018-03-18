package com.patterncat.lucene.basic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleHashSet implements Iterable<String> {
    String[] table;

    public SimpleHashSet() {
        table = new String[100];
    }

    public boolean contains(String o) {
        if (table[o.hashCode()] != null) {
            return true;
        }
        return false;
    }

    public void add(String s) {
        table[s.hashCode()] = s;
    }

    public void addAll(HashSet<String> states) {
        for (String s : states) {
            table[s.hashCode()] = s;
        }
    }

    private class HashIterator<E> implements Iterator<String> {
        String next; // next entry to return
        int index; // current slot
        String current; // current entry

        HashIterator() {
            if (table.length > 0) { // advance to first entry
                String[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final String nextEntry() {
            String e = next;
            if (e == null)
                throw new NoSuchElementException();

            String[] t = table;
            while (index < t.length && (next = t[index++]) == null)
                ;
            current = e;
            return e;
        }

        public String next() {
            return nextEntry();
        }

        public void remove() {

        }
    }

    public Iterator<String> iterator() {
        return new HashIterator<String>();
    }

    public static void main(String[] args) {
        SimpleHashSet hash = new SimpleHashSet();
        hash.add("a");
        hash.add("b");
        System.out.println(hash.contains("c"));
        System.out.println(hash.contains("b"));
        for(String e:hash)
        {
            System.out.println(e);
        }
    }
}
