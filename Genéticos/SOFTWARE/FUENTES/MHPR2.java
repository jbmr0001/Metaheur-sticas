package mhpr2;

import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Grupo 7
 */
public class MHPR2 {

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

        //data.muestramatrices();
        /////////////////////////GREEDY///////////////////////////
        System.out.println("//////////////////////////////GREEDY///////////////////////////////");
        AlgGreedy_Clase4_Grupo7 greedy = new AlgGreedy_Clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD());
        //greedy.mostrarVD();
        //greedy.mostrarVF();
        greedy.algoritmo();
        LinkedList<Integer> solucionGreedy = greedy.getVectorSolucion();
        greedy.mostrarResultados();

        System.out.println("/////////////"+config.getArchivoEntrada()+"/////////////");
        for (String semilla : config.getSemillas()) {
            /////////////////////////GENETICO GENERACIONAL///////////////////////////
            System.out.println("/////////////////////////////GENETICO ESTACIONARIO//////////////////////////////////////");
            AGE_Clase4_Grupo7 est = new AGE_Clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD(), solucionGreedy, config, Long.parseLong(semilla));
            est.algoritmo();
            String ficheroSalidaEstacionario = config.getRutaSalida() + "Estacionario" +config.getCruceEstacionario()+ "-"+ Long.parseLong(semilla) + "-nissan"+config.getArchivoEntrada().split("nissan")[1] + ".txt";
            Logger lg = new Logger(ficheroSalidaEstacionario, est.getLog().toString());
            
        }
        
        System.out.println("");
        for(String semilla : config.getSemillas()){
            System.out.println("/////////////////////////////GENETICO GENERACIONAL//////////////////////////////////////");
            //System.out.println("/////////////////////////////GENETICO GENERACIONAL//////////////////////////////////////");
            AGG_Clase4_Grupo7 gen = new AGG_Clase4_Grupo7(data.getTamGlobal(), data.getMatF(), data.getMatD(), solucionGreedy, config, Long.parseLong(semilla));
            gen.algoritmo();
            String ficheroSalidaEstacionario = config.getRutaSalida() + "Generacional" +config.getCruceGeneracional()+ "-"+ Long.parseLong(semilla) + "-nissan"+config.getArchivoEntrada().split("nissan")[1] + ".txt";
            Logger lg = new Logger(ficheroSalidaEstacionario, gen.getLog().toString());
        }

        /////////////////////////LOG///////////////////////////
        //String ficheroSalidaGreedy = config.getRutaSalida() + "Greedy-" +"nissan"+config.getArchivoEntrada().split("nissan")[1] +".txt";
        //Logger lg = new Logger(ficheroSalidaGreedy, greedy.getLog().toString());

    }

}
