package main;

import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

public class RemoveAll {
    public static void main (String[] args){
        final HashSet<AtomicString> set1 = new HashSet<>();
        final TreeSet<AtomicString> set2 = new TreeSet<>((a, b) -> a.toString().compareTo(b.toString()));
        set1.add(new AtomicString("A"));
        set1.add(new AtomicString("B"));
        set1.add(new AtomicString("C"));
        set1.removeAll(set2);

        set2.add(new AtomicString("A"));
        set1.removeAll(set2);

        set2.add(new AtomicString("B"));
        set1.removeAll(set2);
        set2.add(new AtomicString("C"));
        set1.removeAll(set2);

    }
}


class AtomicString{
    private AtomicReference<String> s;

    public AtomicString(String s) {
        this.s = new AtomicReference<>(s);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AtomicString that = (AtomicString) o;
        return this.s.get().equals(that.getS().get());
    }

    public AtomicReference<String> getS() {
        return s;
    }

    @Override public int hashCode() {
        return Objects.hash(s.get());
    }
}
