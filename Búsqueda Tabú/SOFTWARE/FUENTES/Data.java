/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mhpr1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author Grupo 7
 */
public class Data {

    /**
     * Matriz de enteros para guardar los flujos
     */
    private int[][] matF = new int [0][0];
    
    /**
     * Matriz de enteros para guardar las distancias
     */
    private int[][] matD = new int [0][0];
    
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
        File file = new File(fichero);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String linea = br.readLine(); // Almacenamos cada linea del fichero
        String espacio = linea.split(" ")[1]; //Utilizamos una variable auxiliar para almacenar el tamaño
        int tam = Integer.parseInt(espacio); //Tamaño de las matrices

        String[] Vaux; //Usamos un vector auxiliar para almacenar los valores
        br.readLine(); //Leemos la linea en blanco
        //Generamos dos matrices auxiliares
        int[][] mat1Aux = new int[tam][tam];
        int[][] mat2Aux = new int[tam][tam];
        for (int i = 0; i < tam; i++) {
            linea = br.readLine();
            Vaux = linea.split(" "); //Ignoramos los espacios y añadimos el contenido al vector
            for (int j = 0; j < tam; j++) {
                mat1Aux[i][j] = Integer.parseInt(Vaux[j]);
            }
        }

        br.readLine();
        for (int i = 0; i < tam; i++) {
            linea = br.readLine();
            Vaux = linea.split(" ");
            for (int j = 0; j < tam; j++) {
                mat2Aux[i][j] = Integer.parseInt(Vaux[j]);
            }
        }
        //Asignamos los valores a las variables globales

        tamGlobal = tam;
        matF = mat1Aux;
        matD = mat2Aux;
       
    }
   
}
