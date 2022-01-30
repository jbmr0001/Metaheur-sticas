package mhpr1;

import java.util.LinkedList;

/**
 *
 * @author Grupo 7
 */
public class AlgGreedy_Clase4_Grupo7 {

    /**
     * Número total de unidades
     */
    private int tamTotal = 0;

    /**
     * Matriz de enteros con los flujos
     */
    private int[][] MF = new int[tamTotal][tamTotal];

    /**
     * Matriz de enteros con las distancias
     */
    private int[][] MD = new int[tamTotal][tamTotal];

    /**
     * StringBuilder con el Logger
     */
    private final StringBuilder log;

    /**
     * Lista Enlazada para almacenar en vector con la sumatoria de flujos de
     * cada fila
     */
    LinkedList<Integer> vectorFlujos = new LinkedList<>();

    /**
     * Lista Enlazada para almacenar en vector con la sumatoria de distancias de
     * cada fila
     */
    LinkedList<Integer> vectorDistancias = new LinkedList<>();

    /**
     * Lista Enlazada para almacenar la solución de nuestro AlgGreedy_Clase4_Grupo7
     */
    LinkedList<Integer> vectorSolucion = new LinkedList<>();

    /**
     * @brief Constructor
     * @param tam Número de unidades
     * @param MFlujo Matriz de flujos
     * @param MDistancia Matriz de distancia
     */
    public AlgGreedy_Clase4_Grupo7(int tam, int[][] MFlujo, int MDistancia[][]) {
        this.tamTotal = tam;
        this.MF = MFlujo;
        this.MD = MDistancia;
        log = new StringBuilder();

        calculaSumatoriaFlujo();
        calculaSumatoriaDistancia();
        //Inicialización del vector solución
        for (int i = 0; i < tamTotal; i++) {
            vectorSolucion.add(0);
        }
    }

    /**
     * @brief Función para encontrar la posición dentro del vector de sumatoria
     * de flujos con mayor valor
     * @return Un entero con la fila con mayor sumatoria de flujos
     */
    private int encuentraMayor() {
        int unidad = 0;
        int mayorFlujo = -99;
        //Búsqueda del mayor
        for (int i = 0; i < tamTotal; i++) {
            if (vectorFlujos.get(i) > mayorFlujo) {
                mayorFlujo = vectorFlujos.get(i);
                unidad = i;
            }
        }

        return unidad;
    }

    /**
     * @brief Función para encontrar la posición dentro del vector de sumatoria
     * de ditancias con menor valor
     * @return Un entero con la fila con mayor sumatoria de flujos
     */
    private int encuentraMenor() {
        int menorDistancia = 1000;
        int posDistancia = 0;
        //Búsqueda del menor
        for (int i = 0; i < tamTotal; i++) {
            if (vectorDistancias.get(i) < menorDistancia) {
                menorDistancia = vectorDistancias.get(i);
                posDistancia = i;
            }
        }
        return posDistancia;
    }

    /**
     * Función para ejecutar el AlgGreedy_Clase4_Grupo7
     */
    void algoritmo() {
        long inicio = System.currentTimeMillis();
        int contador = 0;
        //Lazo greedy
        while (contador != tamTotal) {
            int unidad;
            int posDistancia;

            unidad = encuentraMayor();
            //Ponemos un valor muy pequeño para retirar esa posición de la búsqueda
            vectorFlujos.set(unidad, -9999);
            posDistancia = encuentraMenor();
            //Ponemos un valor muy grande para retirar esa posición de la búsqueda
            vectorDistancias.set(posDistancia, 10000);
            vectorSolucion.set(unidad, posDistancia);
            contador++;
        }
        long fin = System.currentTimeMillis();
        log.append("TIEMPO: ").append(fin - inicio).append(" ms\n");
    }

    /**
     * @Brief Funcion para calcular el coste de la solución
     * @param Vsolucion Lista Enlazada con el vector del que queremos calcular el coste
     * @return Entero con el coste
     */
    int coste(LinkedList<Integer> Vsolucion) {
        int valor = 0;
        for (int i = 0; i < tamTotal; i++) {
            for (int j = 0; j < tamTotal; j++) {
                valor += MF[i][j] * MD[Vsolucion.get(i)][Vsolucion.get(j)];
            }
        }
        return valor;
    }

    /**
     * @Brief Función para calcular la sumatoria de flujo por filas
     */
    private void calculaSumatoriaFlujo() {
        System.out.println();
        int flujoAux;
        //Por cada fila sumamos todos los flujos de esa fila
        for (int i = 0; i < tamTotal; i++) {
            flujoAux = 0;
            for (int j = 0; j < tamTotal; j++) {
                flujoAux += MF[i][j];
            }

            vectorFlujos.add(flujoAux);
        }

    }

    /**
     * @Brief Función para mostrar los resultados de nuestro algoritmo así como
     * guardarlos en el log
     */
    public void mostrarResultados() {
        
        System.out.println("El vector solucion es: "+vectorSolucion.toString()+" Coste: "+coste(vectorSolucion));
        log.append(vectorSolucion.toString()).append(" Coste: ").append(coste(vectorSolucion));
    }

    /**
     * @Brief Función para calcular la sumatoria de distancia por filas
     */
    private void calculaSumatoriaDistancia() {
        System.out.println();
        int distanciaAux;
        //Por cada fila sumamos todos las distancias de esa fila
        for (int i = 0; i < tamTotal; i++) {
            distanciaAux = 0;
            for (int j = 0; j < tamTotal; j++) {
                distanciaAux += MD[i][j];

            }

            vectorDistancias.add(distanciaAux);
        }
    }

    /**
     * Getter de la matriz de distancias
     *
     * @return Una matriz de enteros
     */
    public int[][] getMD() {
        return MD;
    }

    /**
     * Getter de la matriz de flujos
     *
     * @return Una matriz de enteros
     */
    public int[][] getMF() {
        return MF;
    }

    /**
     * @Brief Getter del tamaño total
     * @return Un entero con el tamaño total
     */
    public int getTamTotal() {
        return tamTotal;
    }

    /**
     * @Brief Función para mostrar el vector de flujos
     */
    void mostrarVF() {
        for (int i = 0; i < tamTotal; i++) {
            System.out.print(" Flujo en fila " + i + ": ");
            System.out.println(vectorFlujos.get(i));
        }
        System.out.println();
    }

    /**
     * @Brief Función para mostrar el vector de distancias
     */
    void mostrarVD() {
        for (int i = 0; i < tamTotal; i++) {
            System.out.print(" Distancia en fila:" + i + ": ");
            System.out.println(vectorDistancias.get(i));
        }
        System.out.println();
    }

    /**
     * @Brief Función para devolver el vector solución
     * @return Lista Enladaza de enteros con el vector solución
     */
    LinkedList<Integer> getVectorSolucion() {
        return vectorSolucion;
    }

    /**
     * @Brief Getter del log
     * @return Un StringBuilder con las cadenas de texto insertadas en el log
     */
    public StringBuilder getLog() {
        return log;
    }

}
