/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mhpr1;

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
     * Numero de iteraciones máximas para nuestros algoritmos
     */
    private int iteraciones;
    
    /**
     * Número de iteraciones que un elemento permanece tabú
     */
    private int tenenciaTabu;
    
    /**
     * Tamaño de la Lista Restringida de Candidatos
     */
    private int tamañoLRC;
    
    /**
     * Probabilidad para elegir entre intensificación o diversificación cuando la búsqueda se estanque
     */
    private float probOscilacionEstr;
    
    /**
     * Porcentaje de iteraciones para la oscilación
     */
    private float porcentajeIteracionesOscilacion;
    
    /**
     * Número de candidatos entre los que elegiremos uno aleatorio en el Greedy Aleatorio
     */
    private int candidatosGreedyAleatorizada;
    
    /**
     * @Brief Constructor del la configuración
     * @param ruta String con la ruta del fichero de configuración a leer
     */
    public Configuracion(String ruta) {
        String linea;
        FileReader f = null;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            
            while ((linea=b.readLine())!=null){
               //Cortamos la línea despues del =
               switch (linea.split("=")[0]){
                   case "Semilla":
                       this.semilla=Integer.parseInt(linea.split("=")[1]);
                       break;
                   case "Fichero de Entrada":
                       this.archivoEntrada=linea.split("=")[1];
                       break;
                   case "Fichero de Salida":
                       this.rutaSalida=linea.split("=")[1];
                       break;
                   case "Iteraciones":
                       this.iteraciones=Integer.parseInt(linea.split("=")[1]);
                       break;
                   case "Tenencia Tabu":
                       this.tenenciaTabu=Integer.parseInt(linea.split("=")[1]);
                   break;
                   case "Tamano LRC":
                       this.tamañoLRC=Integer.parseInt(linea.split("=")[1]);
                   break;
                   case "Probabilidad Oscilacion Estrategica":
                       this.probOscilacionEstr=Float.parseFloat(linea.split("=")[1]);
                   break;
                   case "Porcentaje de Iteraciones para Oscilacion":
                       this.porcentajeIteracionesOscilacion=Float.parseFloat(linea.split("=")[1]);
                   break;
                   case "Candidatos de la GREEDY aleatorizada":
                       this.candidatosGreedyAleatorizada=Integer.parseInt(linea.split("=")[1]);
                   break;
                       
               }
            }
            //Comprobación de la lectura
            System.out.println("////////////LECTURA ARCHIVO CONFIGURACIÓN/////////////");
            System.out.println("Semilla: "+semilla);
            System.out.println("Archivo de Entrada: "+archivoEntrada);
            System.out.println("Archivo de Salida: "+rutaSalida);
            System.out.println("Numero de iteraciones: "+iteraciones);
            System.out.println("Tenencia Tabú: "+tenenciaTabu);
            System.out.println("Tamaño LRC: "+tamañoLRC);
            System.out.println("Probabilidad Oscilacion Estrategica: "+probOscilacionEstr);
            System.out.println("Porcentaje de Iteraciones para Oscilación: "+porcentajeIteracionesOscilacion);
            System.out.println("Candidatos de la GREEDY aleatorizada: "+candidatosGreedyAleatorizada);
            
                   
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
     * @Brief Getter del numero de iteraciones
     * @return Entero con el numero de interaciones
     */
    public int getIteraciones() {
        return iteraciones;
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
     * @return String con con la 
     */
    public String getRutaSalida() {
        return rutaSalida;
    }

    /**
     * @Brief Getter de la tenecia tabú
     * @return Entero con la tenencia tabú
     */
    public int getTenenciaTabu() {
        return tenenciaTabu;
    }

    /**
     * @Brief Getter del tamaño del LRC
     * @return Entero con el tamaño del LRC
     */
    public int getTamañoaLRC() {
        return tamañoLRC;
    }

    /**
     * @Brief Getter de la probabilidad de oscilación estratégica
     * @return Float con la probabilidad de oscilación estratégica
     */
    public float getProbOscilacionEstr() {
        return probOscilacionEstr;
    }

    /**
     * @Brief Getter del procentaje de iteraciones para la oscilación
     * @return Float con el procenaje de iteraciones para la oscilación
     */
    public float getPorcentajeIteracionesOscilacion() {
        return porcentajeIteracionesOscilacion;
    }

    /**
     * @Brief Getter de los candidatos de la Greedy aleatorizada
     * @return Entero con el número candidatos de la Greedy aleatorizada
     */
    public int getCandidatosGreedyAleatorizada() {
        return candidatosGreedyAleatorizada;
    }
    
}