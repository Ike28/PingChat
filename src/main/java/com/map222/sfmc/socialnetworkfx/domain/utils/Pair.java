package com.map222.sfmc.socialnetworkfx.domain.utils;

import java.util.Objects;

/**
 * Generates objects pairing two other objects of any datatype
 * @param <E1> -datatype of the first object in the pair
 * @param <E2> -datatype of the second object in the pair
 */
public class Pair<E1, E2> {
    private E1 e1;
    private E2 e2;

    public Pair(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * @return the first object in the pair
     */
    public E1 getFirstOfPair() {
        return e1;
    }

    public void setFirstInPair(E1 e1) {
        this.e1 = e1;
    }

    /**
     * @return the second object in the pair
     */
    public E2 getSecondOfPair() {
        return e2;
    }

    public void setSecondInPair(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return "" + e1 + "," + e2;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Pair))
            return false;
        Pair<E1, E2> other = (Pair) obj;
        return this.e1.equals(other.e1) && this.e2.equals(other.e2) || this.e1.equals(other.e2) && this.e2.equals(other.e1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
