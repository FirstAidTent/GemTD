package com.example.firstaidtent.gemtd;

import java.util.HashSet;
import java.util.Set;

public class Test2 {
    public static void main(String[] args) {
        Set<Node> set = new HashSet<>();
        Node id = new Node(2, 3, 1.0);
        Node id2 = new Node(2, 3, 1.0);

        set.add(id);

        System.out.println(set.contains(id));
        System.out.println(set.contains(id2));
    }
}
