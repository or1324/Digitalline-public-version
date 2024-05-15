package com.example.digitalline;

import java.io.Serializable;

public class Pair<Type1, Type2> implements Serializable {
    private Type1 left;
    private Type2 right;

    public Pair(Type1 left, Type2 right) {
        this.left = left;
        this.right = right;
    }

    public Type1 getLeft() {
        return left;
    }


    public Type2 getRight() {
        return right;
    }
}
