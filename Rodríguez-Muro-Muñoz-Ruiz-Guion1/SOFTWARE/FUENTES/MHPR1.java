package mhpr1;

import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Grupo 7
 */
public class MHPR1 {

    //Variables globales donde se almacenan los datos
    public static int[][] matF = new int[0][0];
    public static int[][] matD = new int[0][0];
    public static int tamGlobal = 0;

    /**
     * @Brief Clase principal de nuestro programa
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        /////////////////////////LECTURA DE FICHEROS///////////////////////////
        Configuracion config = new Configuracion("metaconfig.txt");
        Data data = new Data(config.getArchivo());

        /////////////////////////GREEDY///////////////////////////
        System.out.println("//////////////////////////////GREEDY///////////////////////////////");
        AlgGreedy_Clase4_Grupo7 greedy = new AlgGreedy_Clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD());
        //greedy.mostrarVD();
        //greedy.mostrarVF();
        greedy.algoritmo();
        LinkedList<Integer> solucionGreedy = greedy.getVectorSolucion();
        greedy.mostrarResultados();

        /////////////////////////PMDLB ITERATIVO///////////////////////////
        AlgPMDLBit_Clase4_Grupo7 pmdlbIterativo = new AlgPMDLBit_Clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD(), solucionGreedy,config.getIteraciones());
        System.out.println("///////////////////////////PRIMER MEJOR ITERATIVO///////////////////////////");
        pmdlbIterativo.algoritmo();
        pmdlbIterativo.mostrarResultados();

        /////////////////////////PMDLB RANDOM///////////////////////////
        AlgPMDLBrandom_clase4_Grupo7 pmdlbRandom = new AlgPMDLBrandom_clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD(), solucionGreedy, config.getSemilla(),config.getIteraciones());
        System.out.println("");
        System.out.println("////////////////////////////PRIMER MEJOR RANDOM//////////////////////////////");
        pmdlbRandom.algoritmo();
        pmdlbRandom.mostrarResultados();

        /////////////////////////MULTIARRANQUE///////////////////////////
        System.out.println("/////////////////////////////MULTI ARRANQUE//////////////////////////////////////");
        AlgMultiarranque_Clase4_Grupo7 multi = new AlgMultiarranque_Clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD(), solucionGreedy, config);
        multi.algoritmo();
        System.out.println("\n");

        /////////////////////////LOG///////////////////////////
        String ficheroSalidaGreedy = config.getRutaSalida() + "Greedy-" + config.getSemilla() +"-"+"ford"+config.getArchivoEntrada().split("ford")[1] +".txt";
        String ficheroSalidaPMBLBIt = config.getRutaSalida() + "PMDLBIt-" + config.getSemilla() +"-"+"ford"+config.getArchivoEntrada().split("ford")[1]+ ".txt";
        String ficheroSalidaPMDLBRandom = config.getRutaSalida() + "PMDLBRandom-" + config.getSemilla() +"-"+"ford"+config.getArchivoEntrada().split("ford")[1]+ ".txt";
        String ficheroSalidaMultiarranque = config.getRutaSalida() + "Multiarranque-" + config.getSemilla() +"-"+"ford"+config.getArchivoEntrada().split("ford")[1]+ ".txt";

        Logger lg = new Logger(ficheroSalidaGreedy, greedy.getLog().toString());
        Logger lbit = new Logger(ficheroSalidaPMBLBIt, pmdlbIterativo.getLog().toString());
        Logger lbrand = new Logger(ficheroSalidaPMDLBRandom, pmdlbRandom.getLog().toString());
        Logger lmulti = new Logger(ficheroSalidaMultiarranque, multi.getLog().toString());
        
    }

}
