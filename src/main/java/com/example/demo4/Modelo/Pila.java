package com.example.demo4.Modelo;

public class Pila <T>{
    private T[] pila;
    private int tope;

    public Pila(){
        pila = (T[]) new Object[10];
        tope = -1;
    }

    public Pila(int tamano){
        pila = (T[]) new Object[tamano];
        tope = -1;
    }

    public boolean llena(){
        return tope == pila.length -1;
    }
}
