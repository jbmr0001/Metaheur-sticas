/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mhpr2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Grupo 7
 */
public class Configuracion {
    
    /**
     * Semilla a usar en esta ejecución
     */
    private int semilla;
    
    /**
     * Nombre del archivo de entrada de datos
     */
    private String archivoEntrada;
    
    /**
     * Ruta para el guardado de los logs
     */
    private String rutaSalida;
    
    /**
     * Entero con el tamaño de la población
     */
    private int tamañoPoblacion;
    
    /**
     * Entero con el número de numEvaluaciones máximo
     */
    private int numEvaluaciones;
    
    /**
     * Float con la probabilidad de cruce para el Algoritmo Genético Estacionario
     */
    private float probCruceGeneracional;
    
    /**
     * Float con la probabilidad de cruce para el Algoritmo Genético Generacional 
     */
    private float probCruceEstacionario;
    
    /**
     * Float con la probabilidad de mutación para cada gen
     */
    private float constanteParaFactorMutacionGen;
    
    /**
     * Array para almacenar todas las semillas a ejecutar
     */
    private String[] _semillas;
    
    /**
     * Entero con el tamaño de la lista restringida de candidatos para la población inicial
     */
    private int tamañoLRC;
   
    /**
     * Entero con el número de candidatos greedy aleatorizados
     */
    private int candidatosGreedyAleatorizada;
    
    /**
     * String con el tipo de cruce para el algoritmo generacional
     */
    private String cruceGeneracional;
    
    /**
     * String con el tipo de cruce para el algoritmo estacionario
     */
    private String cruceEstacionario;
    
    /**
     * @Brief Constructor del la configuración
     * @param ruta String con la ruta del fichero de configuración a leer
     */
    public Configuracion(String ruta) {
        String linea;
        FileReader f;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            
            while ((linea=b.readLine())!=null){
               //Cortamos la línea despues del =
               String caso=linea.split("=")[0];
               switch (caso){
                   case "Semilla":
                       String semillas = linea.split("=")[1];
                       _semillas = semillas.split(" ");
                       break;
                   case "Fichero de Entrada":
                       this.archivoEntrada=linea.split("=")[1];
                       break;
                   case "Fichero de Salida":
                       this.rutaSalida=linea.split("=")[1];
                       break;
                   case "Tamano LRC":
                       this.tamañoLRC=Integer.parseInt(linea.split("=")[1]);
                   break;
                   case "Candidatos de la GREEDY aleatorizada":
                       this.candidatosGreedyAleatorizada=Integer.parseInt(linea.split("=")[1]);
                   break;
                   case "Tamano poblacion":
                       this.tamañoPoblacion=Integer.parseInt(linea.split("=")[1]);
                       break;
                   case "Evaluaciones":
                       this.numEvaluaciones=Integer.parseInt(linea.split("=")[1]);
                   break;
                   case "Probabilidad Cruce Estacionario":
                       this.probCruceEstacionario=Float.parseFloat(linea.split("=")[1]);
                   break;
                   case "Probabilidad Cruce Generacional":
                       this.probCruceGeneracional=Float.parseFloat(linea.split("=")[1]);
                   break;
                   case "Constante para Factor Mutacion Gen":
                       this.constanteParaFactorMutacionGen=Float.parseFloat(linea.split("=")[1]);
                   case "Cruce Estacionario":
                       this.cruceEstacionario=linea.split("=")[1];
                   case "Cruce Generacional":
                       this.cruceGeneracional=linea.split("=")[1];  
                   break;       
               }
            }
            //Comprobación de la lectura
            System.out.println("////////////LECTURA ARCHIVO CONFIGURACIÓN/////////////");
            System.out.println("Semillas: ");
            for(int i=0;i<_semillas.length;i++){
                System.out.println(_semillas[i]);
            }
            System.out.println("Archivo de Entrada: "+archivoEntrada);
            System.out.println("Archivo de Salida: "+rutaSalida);
            System.out.println("Tamaño LRC: "+tamañoLRC);
            System.out.println("Candidatos de la GREEDY aleatorizada: "+candidatosGreedyAleatorizada);
            System.out.println("Tamaño Población: "+tamañoPoblacion);
            System.out.println("Numero Evaluaciones: "+numEvaluaciones);
            System.out.println("Probabilidad Cruce Estacionario: "+probCruceEstacionario);
            System.out.println("Probabilidad Cruce Generacional: "+probCruceGeneracional);
            System.out.println("Constante para Factor Mutacion Gen: "+constanteParaFactorMutacionGen);
            System.out.println("Cruce Estacional: "+cruceEstacionario);
            System.out.println("Cruce Generacional: "+cruceGeneracional);
            
                   
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * @Brief Getter de archivo de datos
     * @return String con el nombre del archivo de datos
     */
    public String getArchivo() {
        return archivoEntrada;
    }

    /**
     * @Brief Getter del cruce para el algoritmo generacional
     * @return Un String con el tipo de cruce
     */
    public String getCruceGeneracional() {
        return cruceGeneracional;
    }

    /**
     * @Brief Getter del cruce para el algoritmo estacionario
     * @return Un String con el tipo de cruce
     */
    public String getCruceEstacionario() {
        return cruceEstacionario;
    }

    /**
     * @Brief Getter del numero de iteraciones
     * @return Entero con el numero de interaciones
     */
    public int getIteraciones() {
        return tamañoPoblacion;
    }

    /**
     * @Brief Getter de la semilla
     * @return Entero con la semilla
     */
    public int getSemilla() {
        return semilla;
    }

    /**
     * @Brief Getter del archivo de entrada de configuración
     * @return String con el archivo de entrada 
     */
    public String getArchivoEntrada() {
        return archivoEntrada;
    }

    /**
     * @Brief Getter de la ruta de salida para guardar los logs
     * @return String con con la ruta
     */
    public String getRutaSalida() {
        return rutaSalida;
    }

    /**
     * @Brief Getter del tamaño de la población
     * @return Entero con tamaño
     */
    public int getTamañoPoblacion() {
        return tamañoPoblacion;
    }

    /**
     * @Brief Getter del numEvaluaciones
     * @return Entero con el número de evaluaciones
     */
    public int getNumEvaluaciones() {
        return numEvaluaciones;
    }

    /**
     * @Brief Getter del tamaño del LRC
     * @return Entero con el tamaño del LRC
     */
    public int getTamañoLRC() {
        return tamañoLRC;
    }

    /**
     * @Brief Getter de la probabilidad de cruce para el generacional
     * @return 
     */
    public float getProbCruceGeneracional() {
        return probCruceGeneracional;
    }

    /**
     * @Brief Getter con la probabilidad de cruce para el estacionario
     * @return 
     */
    public float getProbCruceEstacionario() {
        return probCruceEstacionario;
    }

    /**
     * @Brief Getter para la probabilidad de mutación de un gen
     * @return 
     */
    public float getConstanteParaFactorMutacionGen() {
        return constanteParaFactorMutacionGen;
    }

    /**
     * @Brief Getter del número de candidatos de la greedy aleatorizada
     * @return 
     */
    public int getCandidatosGreedyAleatorizada() {
        return candidatosGreedyAleatorizada;
    }

    /**
     * @Brief Getter del tamaño del LRC
     * @return Entero con el tamaño del LRC
     */
    public int getTamañoaLRC() {
        return tamañoLRC;
    }
    
    /**
     * @Brief Getter del Array de semillas
     * @return Array de String con las semillas a ejecutar
     */
    public String [] getSemillas(){
        return _semillas;
    }
}