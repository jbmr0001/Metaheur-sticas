package mhpr1;

import java.util.LinkedList;

/**
 *
 * @author Grupo 7
 */
public final class AlgPMDLBrandom_clase4_Grupo7 {

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
     * Entero con el coste de la solución inicial
     */
    private final int primerCoste;
    
    /**
     * Lista Enlazada con la solución actual
     */
    private LinkedList<Integer> solucionactual = new LinkedList<>();
    
    /**
     * Lista Enlazada con la solución obtenida tras la ejecución del algoritmo
     */
    private LinkedList<Integer> nuevoVectorSolucion = new LinkedList<>();

    /**
     * Entero con el número de iteraciones máximas para el algoritmo
     */
    private final int numIteraciones;

    /**
     * Array de Enteros con el don't look bits
     */
    private int[] dlb = new int[tamTotal];
    
    /**
     * Entero para guardar el último intercambio del búsquedaVecino
     */
    int UltimoIntercambio = 0;
    
    /**
     * MetaRandom para crear aleatorios con nuestra semilla
     */
    private final MetaRandom aleatorio;

    /**
     * @param semilla Semilla
     * @Brief Constructor del Primer Mejor DLB Iterativo
     * @param tam Entero con el tañamo global
     * @param MFlujo Matriz de enteros con los flujos
     * @param MDistancia Matriz de enteros con las distancias
     * @param solucionInicial Lista Enlazada con la solución inicial
     * @param numIt Entero con el número de iteraciones
     */
    public AlgPMDLBrandom_clase4_Grupo7(int tam, int[][] MFlujo, int MDistancia[][], LinkedList<Integer> solucionInicial, long semilla,int numIt) {
        this.tamTotal = tam;
        this.MF = MFlujo;
        this.MD = MDistancia;
        this.numIteraciones=numIt;
        calculaSumatoriaDistancia();
        calculaSumatoriaFlujo();
        this.solucionactual = (LinkedList<Integer>) solucionInicial.clone();
        this.nuevoVectorSolucion = solucionInicial;
        log = new StringBuilder();

        int[] vaux = new int[tamTotal];
        for (int i = 0; i < tamTotal; i++) {
            vaux[i] = 0;
        }
        this.dlb = vaux;
        this.primerCoste = coste(solucionInicial);
        this.aleatorio = new MetaRandom();
        this.aleatorio.Set_random(semilla);
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
     * @Brief Función para calcular la sumatoria de distancia por filas 
     */
    private void calculaSumatoriaDistancia() {
        System.out.println();
        int distanciaAux;
        for (int i = 0; i < tamTotal; i++) {
            distanciaAux = 0;
            for (int j = 0; j < tamTotal; j++) {
                distanciaAux += MD[i][j];

            }

            vectorDistancias.add(distanciaAux);
        }
    }

    /**
     * @Brief Función para calcular la sumatoria de flujo por filas 
     */
    private void calculaSumatoriaFlujo() {
        System.out.println();
        int flujoAux;
        for (int i = 0; i < tamTotal; i++) {
            flujoAux = 0;
            for (int j = 0; j < tamTotal; j++) {
                flujoAux += MF[i][j];
            }

            vectorFlujos.add(flujoAux);
        }

    }

    /**
     * @Brief Operador 2-opt que realiza un permutación en el vector en las posiciones dadas
     * @param vector Lista Enlazada con el vector a permutar
     * @param i Entero con la primera posición del intercambio
     * @param j Entero con la segunda posición del intercambio
     */
    void permutacion(LinkedList<Integer> vector, int i, int j) {
        int a = vector.get(i);
        int b = vector.get(j);
        //System.out.println("Valor a: " + a + " Valor b: " + b);
        vector.set(i, b);
        vector.set(j, a);
    }

   /**
     * @Brief Funcion para comprobar si una permutación mejora en función de la factorizacón
     * @param i Entero con la posición 1 de la permutación
     * @param j Entero con la posición 2 de la permutación
     * @return 
     */
    boolean check(int i, int j) {
        boolean minimiza;
        int Nuevovalor = factorizacion(i, j);
        //System.out.println("Valor vector pi: " + coste + " Valor vector pi': " + Nuevovalor);
        minimiza = Nuevovalor < 0;
        return minimiza;
        //return coste < coste(vaux);
    }
   
   /**
     * @Brief Función principal de nuestro algoritmo Primer Mejor DLB Iterativo
   */
   void algoritmo() {
        //La solución actual ya está generada
        long inicio = System.currentTimeMillis();
        int cont = 0;
        boolean vecino;
        int num = aleatorio.Randint(0, tamTotal - 1);;
        while (cont != numIteraciones) {
            vecino = busquedaVecino(num);
            if (vecino) {
                solucionactual = nuevoVectorSolucion;
            }
            cont++;
            num = UltimoIntercambio;
            if(espacioCompleto()) cont = 1000;
        }
        long fin = System.currentTimeMillis();
        log.append("TIEMPO: ").append(fin - inicio).append(" ms\n");
    }

    /**
     * @Brief Función para explorar todas las unidades y encontrar mejorPrimerVecinos
     * @param num Entero con la unidad de la que partimos
     * @return Boolean que indica si hemos encontrado vecino
    */
    boolean busquedaVecino(int num) {

        boolean improve_flag = false;
        boolean vecino = false;

        LinkedList<Integer> vaux = solucionactual;
        int conti = 0;
        int contj = 0;
        int i = num;
        int j;
        while (i != num - 1) {
            if (dlb[i] == 0) {
                improve_flag = false;
                j = i + 1;
                if (j == tamTotal) {
                    j = 0;
                }
                contj = 0;
                //System.out.println("i " + i);
                while (j != num - 1) {
                    if (j == tamTotal - 1) {
                        j = 0;
                    }
                    improve_flag = check(i, j);
                    if (improve_flag) {
                        permutacion(vaux, i, j);
                        dlb[i] = dlb[j] = 0;
                        vecino = true;
                        nuevoVectorSolucion = vaux;
                        //System.out.println(nuevoVectorSolucion.toString()+" Coste vecino: " + coste(vaux));
                        log.append("\n").append(nuevoVectorSolucion.toString()).append(" Coste vecino: ").append(coste(vaux)).append("\n");
                        UltimoIntercambio = i;
                    }
                    j++;
                    contj++;
                    //System.out.println("jota " + j);
                    if (contj == tamTotal - 1) {
                        j = num - 1;
                    }
                }
                if (improve_flag == false) {
                    dlb[i] = 1;
                }
            }
            if (i == tamTotal - 1) {
                i = 0;
            }
            i++;
            conti++;
            if (conti == tamTotal - 1) {
                i = num - 1;
            }
        }
        //System.out.println("Solucion parcial: " + coste(vaux));
        return vecino;
    }

    /**
     * @Brief Función para comprobar si todo el dlb está a 1 (espacio completo)
     * @return Boolean que indica si todo el dlb esta a 1
     */
    boolean espacioCompleto() {
        boolean completo = true;
        for (int i = 0; i < tamTotal; i++) {
            if (dlb[i] == 0) {
                completo = false;
            }
        }
        return completo;
    }

    /**
     * @Función para calcular la factorización parte 1
     * @param k Entero con el valor desde 0 hasta el tam máximo
     * @param r Entero con la i
     * @param s Entero con la j
     * @return Un entero con la instacia k
     */
    int funcion(int k, int r, int s) {
        int valor = MF[r][k] * (MD[solucionactual.get(s)][solucionactual.get(k)] - MD[solucionactual.get(r)][solucionactual.get(k)])
                + MF[s][k] * (MD[solucionactual.get(r)][solucionactual.get(k)] - MD[solucionactual.get(s)][solucionactual.get(k)])
                + MF[k][r] * (MD[solucionactual.get(k)][solucionactual.get(s)] - MD[solucionactual.get(k)][solucionactual.get(r)])
                + MF[k][s] * (MD[solucionactual.get(k)][solucionactual.get(r)] - MD[solucionactual.get(k)][solucionactual.get(s)]);

        return valor;
    }

    /**
     * @Brief función para calcular la factorización parte 2
     * @param r Entero con la i
     * @param s Entero con la j
     * @return Entero con la factorización
     */
    int factorizacion(int r, int s) {
        int valor = 0;
        valor += MF[r][r] * (MD[solucionactual.get(s)][solucionactual.get(s)] - MD[solucionactual.get(r)][solucionactual.get(r)])
                + MF[s][s] * (MD[solucionactual.get(r)][solucionactual.get(r)] - MD[solucionactual.get(s)][solucionactual.get(s)])
                + MF[r][s] * (MD[solucionactual.get(s)][solucionactual.get(r)] - MD[solucionactual.get(r)][solucionactual.get(s)])
                + MF[s][r] * (MD[solucionactual.get(r)][solucionactual.get(s)] - MD[solucionactual.get(s)][solucionactual.get(r)]);

        for (int k = 0; k < tamTotal; k++) {
            if (r != k && s != k) {
                valor += funcion(k, r, s);
            }
        }
        return valor;
    }

    /**
     * @Brief Función para mostrar los resultados de nuestro algoritmo así como guardarlos en el log
     */
    public void mostrarResultados() {
        log.append("El valor inicial era de: ").append(primerCoste).append("\n");
        System.out.println("El valor inicial era de: " + primerCoste);
        log.append("El nuevo coste es de: ").append(coste(nuevoVectorSolucion)).append("\n");
        System.out.println("El nuevo coste es de: " + coste(nuevoVectorSolucion));
        log.append("Se ha mejorado en el coste: ").append(primerCoste - coste(nuevoVectorSolucion)).append("\n");
        System.out.println("Se ha mejorado en el coste: " + (primerCoste - coste(nuevoVectorSolucion)));
        log.append("El vector solucion es: " + "\n");
        System.out.println("El vector solucion es: ");
        log.append(nuevoVectorSolucion.toString());
        System.out.println(nuevoVectorSolucion.toString());
    }

    /**
     * @Brief Getter del log
     * @return Un StringBuilder con las cadenas de texto insertadas en el log
     */
    public StringBuilder getLog() {
        return log;
    }

}
