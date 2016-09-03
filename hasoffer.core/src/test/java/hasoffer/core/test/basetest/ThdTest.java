package hasoffer.core.test.basetest;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Date : 2016/2/22
 * Function :
 */

public class ThdTest {

    @Test
    public void f() {
        Set<A> aset = new HashSet<A>();

        aset.add(new A(0));
        aset.add(new A(0));
        aset.add(new A(0));

        System.out.println(aset.size());

    }

}

class A {
    int a = 0;

    public A(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        A a1 = (A) o;

        return a == a1.a;

    }

    @Override
    public int hashCode() {
        return a;
    }
}
