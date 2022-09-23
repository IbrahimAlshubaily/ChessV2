package org.pxf.controller;

import org.pxf.model.ChessBoard;

import java.util.HashMap;

public class LRUCache {

    private Node head, tail;
    private final int capacity;
    private final HashMap<Integer, Node> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>(capacity);
    }

    public float get(ChessBoard key) {
        if (cache.containsKey(key.hashCode())) {
            Node node = cache.get(key.hashCode());
            detach(node);
            prepend(node);
            return node.winRatio();
        }
        return 0;
    }

    public void put(ChessBoard key, float isWin) {

        if (cache.containsKey(key.hashCode())) {
            Node node = cache.get(key.hashCode());
            node.value.nWins += isWin;
            node.value.nSamples += 1;
            detach(node);
            prepend(node);
            return;
        }

        if (cache.size() == capacity - 1) {
            //System.out.println(cache.size());
            this.cache.remove(tail.key);
            detach(tail);
        }

        Node newNode = new Node(key.hashCode(), isWin, 1);
        cache.put(key.hashCode(), newNode);
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
        int key;
        Sample value;
        Node prev, next;
        Node(int key, float nWins, int nSamples) {
            this.key = key;
            this.value = new Sample(nWins, nSamples);
            prev = next = null;
        }
        float winRatio() {
            return value.nWins / value.nSamples;
        }
        class Sample {
            float nWins;
            int nSamples;
            Sample(float nWins, int nSamples) {
                this.nWins = nWins;
                this.nSamples = nSamples;
            }
        }
    }

}

