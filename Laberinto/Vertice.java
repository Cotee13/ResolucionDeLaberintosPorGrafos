package Laberinto;

public class Vertice {
    private int id;
    private int fila;
    private int columna;

    public Vertice(int id, int fila, int columna) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
    }

    public int getId() {
        return id;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void display() {
        System.out.println("Vertice " + id + " (" + fila + ", " + columna + ")");
    }
}

