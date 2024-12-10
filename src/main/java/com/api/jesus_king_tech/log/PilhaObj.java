package com.api.jesus_king_tech.log;

public class PilhaObj<T> {
    private T[] elementos;
    private int topo;

    @SuppressWarnings("unchecked")
    public PilhaObj(int capacidade) {
        this.elementos = (T[]) new Object[capacidade];
        this.topo = -1;
    }

    public void push(T elemento) {
        if (isFull()) {
            throw new IllegalStateException("Pilha está cheia!");
        }
        elementos[++topo] = elemento;
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Pilha está vazia!");
        }
        return elementos[topo--];
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Pilha está vazia!");
        }
        return elementos[topo];
    }

    public boolean isEmpty() {
        return topo == -1;
    }

    public boolean isFull() {
        return topo == elementos.length - 1;
    }

    public void exibe() {
        if (isEmpty()) {
            System.out.println("Pilha está vazia!");
            return;
        }
        for (int i = topo; i >= 0; i--) {
            System.out.println(elementos[i]);
        }
    }
}
