package com.example.demo4.Modelo;

public class Pila <T>{
    private T[] dato;
    private int tope;

    public Pila(){
        dato = (T[]) new Object[10];
        tope = -1;
    }

    public Pila(int tamano){
        dato = (T[]) new Object[tamano];
        tope = -1;
    }
}
