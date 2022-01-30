/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mhpr2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Grupo 7
 */
public final class Data {

    /**
     * Matriz de enteros para guardar los flujos
     */
    private int[][] matF = new int[0][0];

    /**
     * Matriz de enteros para guardar las distancias
     */
    private int[][] matD = new int[0][0];

    /**
     * Entero para guardar el número de filas/columnas/unidades
     */
    private int tamGlobal = 0;

    /**
     * @Brief Constructor de la clase Data
     * @param fichero String con el fichero de datos a leer
     * @throws IOException
     */
    public Data(String fichero) throws IOException {
        lectura(fichero);
    }

    /**
     * @Brief Getter de la matriz de flujos
     * @return
     */
    public int[][] getMatF() {
        return matF;
    }

    /**
     * @Brief Getter de la matriz de distancias
     * @return
     */
    public int[][] getMatD() {
        return matD;
    }

    /**
     * @Brief Getter del tamaño global
     * @return
     */
    public int getTamGlobal() {
        return tamGlobal;
    }

    /**
     * @Brief Función para mostrar la matriz de flujos y distancias
     */
    void muestramatrices() {
        System.out.println("Imprime la primera matriz: \n");
        for (int i = 0; i < tamGlobal; i++) {
            for (int j = 0; j < tamGlobal; j++) {
                System.out.print(" " + matF[i][j]);
            }
            System.out.println("");
        }

        System.out.println("Imprime la segunda matriz: \n");
        for (int i = 0; i < tamGlobal; i++) {
            for (int j = 0; j < tamGlobal; j++) {
                System.out.print(" " + matD[i][j]);
            }
            System.out.println("");
        }
    }

    /**
     * @Brief Método de lectura del archivo de datos
     * @param fichero String con el nombre del fichero de datos
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void lectura(String fichero) throws FileNotFoundException, IOException {
        String linea;
        FileReader f;
        try {
            f = new FileReader(fichero);
            BufferedReader b = new BufferedReader(f);
            int N = Integer.parseInt(b.readLine().substring(1));
            int[][] matriz1 = new int[N][N];
            int[][] matriz2 = new int[N][N];
            linea = b.readLine();

            //Rellenamos la primera matriz
            for (int i = 0; i < N; i++) {
                linea = b.readLine();
                String[] separador = linea.split(" ");
                int errores = 0;
                for (int j = 0; j < separador.length; j++) {
                    try{
                        matriz1[i][j - errores] = Integer.parseInt(separador[j]);
                    } catch (NumberFormatException e){
                        errores++;
                    }
                }
            }

            linea = b.readLine();
            
            //Rellenamos la segunda matriz
            for (int i = 0; i < N; i++) {
                linea = b.readLine();
                String[] separador = linea.split(" ");
                int errores = 0;
                for (int j = 0; j < separador.length; j++) {
                    try{
                        matriz2[i][j - errores] = Integer.parseInt(separador[j]);
                    } catch (NumberFormatException e){
                        errores++;
                    }
                }
            }
            tamGlobal = N;
            matF = matriz1;
            matD = matriz2;

        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
