package org.pxf.controller;

import java.util.HashMap;

public class LRUCache {

    private Node head, tail;
    private final int capacity;
    private final HashMap<Integer, Node> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>(capacity);
    }

    public int get(int key) {
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            detach(node);
            prepend(node);
            return node.value;
        }
        return -1;
    }

    public void put(int key, int value) {

        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            detach(node);
            prepend(node);
            return;
        }

        if (cache.size() == capacity) {
            this.cache.remove(tail.key);
            detach(tail);
        }

        Node newNode = new Node(key, value);
        cache.put(newNode.key, newNode);
        prepend(newNode);
    }

    private void detach(Node node) {

        if (node.prev != null){
            node.prev.next = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        if (node == head){
            this.head = this.head.next;
        }

        if (node == tail){
            this.tail = this.tail.prev;
        }

        node.next = node.prev = null;
    }

    private void prepend(Node node) {

        if (this.head == null){
            this.head = this.tail = node;
            return;
        }
        this.head.prev = node;
        node.next = head;
        head = node;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Node curr = this.head;
        while (curr != null){
            result.append(curr.key).append(" : ").append(curr.value).append( " | ");
            curr = curr.next;
        }
        return result.toString();
    }

    class Node {
        int key, value;
        Node prev, next;
        Node(int key, int value) {
            this.key = key;
            this.value = value;
            prev = next = null;
        }
    }
}

