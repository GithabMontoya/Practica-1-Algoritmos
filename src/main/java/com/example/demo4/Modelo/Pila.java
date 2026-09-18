package com.example.demo4.Modelo;

public class Pila <T>{
    private T[] pila;
    private int tope;

    public Pila(){
        pila = (T[]) new Object[100];
        tope = -1;
    }

    public Pila(int tamano){
        pila = (T[]) new Object[tamano];
        tope = -1;
    }

    public boolean llena(){
        return tope == pila.length -1;
    }

    public boolean vacia(){
        return tope == -1;
    }

    public void push(T dato){
        if(llena()){
            System.out.println("Desbordamiento");
        } else {
            tope++;
            pila[tope] = dato;
        }
    }

    public T pop(){
        T dato;
        if(vacia()){
            System.out.println("Subdesbordamiento");
            return null;
        } else {
            dato = pila[tope];
            pila[tope] = null;
            tope --;
            return dato;
        }
    }

    public T peek(){
        T dato;
        if(vacia()){
            System.out.println("Subdesbordamiento");
            return null;
        } else {
            dato = pila[tope];
            return dato;
        }
    }
}
