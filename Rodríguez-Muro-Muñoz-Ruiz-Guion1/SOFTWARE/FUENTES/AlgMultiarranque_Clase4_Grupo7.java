package mhpr1;

import java.util.LinkedList;

/**
 *
 * @author Grupo 7
 */
public class AlgMultiarranque_Clase4_Grupo7 {

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
     * Lista Enlazada con la solución actual
     */
    private final LinkedList<Integer> solucionactual = new LinkedList<>();
    
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
     * Lista Enlazada de Enteros con solución mejor global 
     */
    private LinkedList<Integer> mejorGlobal = new LinkedList<>();
    
    /**
     * Lista Enlazada de Enteros con la solución mejor global tabú
     */
    private LinkedList<Integer> mejorGlobalTabu = new LinkedList<>();
    
    /**
     * Lista Enlazada con la solución en la que nos encontramos
     */
    private LinkedList<Integer> actual = new LinkedList<>();
    
    /**
     * Entero con la última I de la búsqueda de vecino
     */
    int ultimaI;
    
    /**
     * Entero con la última J de la búsqueda de vecino
     */
    int ultimaJ;

    /**
     * Vector auxiliar para almacenar las cinco unidades con mayores sumatoria de flujos
     */
    private int[] vectorMayores = new int[0];
    
    /**
     * Vector auxiliar para almacenar las cinco unidades con peor sumatoria de distancias
     */
    private int[] vectorConcentricas = new int[0];

    /**
     * Lista Enlazda de Enteros para almacenar la Solución inicial Greedy
     */
    LinkedList<Integer> solInicial = new LinkedList<>();

    /**
     * Lista Enlazada de Listas Enlazadas de enteros para almacenar la Lista Restringida de Candidatos del Multiarranque
     */
    private final LinkedList<LinkedList<Integer>> LRC = new LinkedList<>();

    /**
     * Lista Enlazda de Par para almacenar la lista de mejores peores vecinos
     */
    private final LinkedList<Par> mejoresPeoresVecinos;

    /**
     * Fichero de cofiguración
     */
    private final Configuracion conf;

    /**
     * Lista Enlazada de Par para almacenar la Memoria a Corto Plazo
     */
    private final LinkedList<Par> memCortoPlazo = new LinkedList<>();
    
    /**
     * Matriz de Enteros tamTotal x tamTotal para almacenar la Memoria a Largo Plazo
     */
    private int[][] memLargoPlazo = new int[tamTotal][tamTotal];
    
    /**
     * Clase para almacenar los pares de un movimiento
     */
    public class Par {

        /**
         * Entero con la posición 1
         */
        private final int i;
        
        /**
         * Entero con la posición 2
         */
        private final int j;
        
        /**
         * Lista Enlazada de Enteros con el vector que genera este Par
         */
        private final LinkedList<Integer> vector;
        
        /**
         * Tenencia Tabú del Par
         */
        private int tenenciaTabu;

        /**
         * @Brief Constructor del Par
         * @param unI Entero con una posición 1
         * @param unJ Entero con una posición 2
         * @param v Lista Enlazada de Enteros con un vector
         */
        Par(int unI, int unJ, LinkedList<Integer> v) {
            this.i = unI;
            this.j = unJ;
            this.vector = v;
            this.tenenciaTabu = conf.getTenenciaTabu();
        }

        /**
         * @Brief Getter de la I
         * @return 
         */
        int getI() {
            return this.i;
        }

        /**
         * @Brief Getter de J
         * @return 
         */
        int getJ() {
            return this.j;
        }

        /**
         * @Brief Getter del vector
         * @return Una Lista Enlazada de enteros con el vector
         */
        LinkedList<Integer> getVector() {
            return this.vector;
        }

        /**
         * @Brief Getter de la tenencia tabú 
         * @return Un entero con la tenencia tabú
         */
        int getTenenciaTabu() {
            return this.tenenciaTabu;
        }
       
        /*
         @Brief Fucción para decrementar la tenencia tabú del par
        */
        void decrementarTenencia(){
            this.tenenciaTabu--;
        }

    }

    /**
     * @Brief Constructor de Nuestro multiarranque
     * @param tam Entero con el tamaño total de unidades
     * @param MFlujo Matriz de enteros con la Matriz de Flujos
     * @param MDistancia Matriz de enteros con la Matriz de Distancias
     * @param solucionInicial Lista Enlazada de Enteros con la solución Greedy
     * @param c Elemento de clase Configuración don los datos de configuración
     */
    public AlgMultiarranque_Clase4_Grupo7(int tam, int[][] MFlujo, int MDistancia[][], LinkedList<Integer> solucionInicial, Configuracion c) {
        this.tamTotal = tam;
        this.MF = MFlujo;
        this.MD = MDistancia;
        this.conf = c;
        this.numIteraciones = c.getIteraciones();
        this.log = new StringBuilder();
        calculaSumatoriaFlujo();
        calculaSumatoriaDistancia();
        this.solInicial = (LinkedList<Integer>) solucionInicial.clone();
        this.aleatorio = new MetaRandom();
        this.aleatorio.Set_random(c.getSemilla());
        this.vectorConcentricas = new int[conf.getCandidatosGreedyAleatorizada()];
        this.vectorMayores = new int[conf.getCandidatosGreedyAleatorizada()];
        //Inicialización
        LinkedList<Integer> ValorNulo = new LinkedList<>();
        for (int i = 0; i < tamTotal; i++) {
            LRC.add(ValorNulo);
        }
        this.mejoresPeoresVecinos = new LinkedList<>();
        this.tamTotal = tam;
        this.MF = MFlujo;
        this.MD = MDistancia;
        calculaSumatoriaDistancia();
        calculaSumatoriaFlujo();

        this.actual = (LinkedList<Integer>) solucionInicial.clone();
        //Inicialización
        int[] vaux = new int[tamTotal];
        for (int i = 0; i < tamTotal; i++) {
            vaux[i] = 0;
        }
        //Inicialización
        int[][] memLPAux = new int[tamTotal][tamTotal];
        for (int i = 0; i < tamTotal; i++) {
            for (int j = 0; j < tamTotal; j++) {
                memLPAux[i][j] = 0;
            }
        }
        //Inicialización
        int[][] tenenciasTabuAux = new int[tamTotal][tamTotal];
        for (int i = 0; i < tamTotal; i++) {
            for (int j = 0; j < tamTotal; j++) {
                tenenciasTabuAux[i][j] = 0;
            }
        }
     
        this.memLargoPlazo = memLPAux;
        this.dlb = vaux;
        this.ultimaI = 0;
        this.ultimaJ = 0;
        
    }

    /**
     * Función para almacenar en un vector auxiliar los numCandidatos unidades con mayor sumatoria flujo
     */
    void encuentraMayores() {
        LinkedList<Integer> ValoresFlujoAux = (LinkedList<Integer>) vectorFlujos.clone();

        int mayor;
        int pos = 0;
        for (int i = 0; i < conf.getCandidatosGreedyAleatorizada(); i++) {
            mayor = 0;
            for (int j = 0; j < tamTotal; j++) {
                if (ValoresFlujoAux.get(j) > mayor) {
                    mayor = ValoresFlujoAux.get(j);
                    pos = j;
                }
                vectorMayores[i] = pos;
                ValoresFlujoAux.set(pos, 0);
            }
        }
    }

    /**
     * Función para almacenar en un vector auxiliar los numCandidatos unidades con peor sumatoria de distancias
     */
    void encuentraConcentricos() {
        LinkedList<Integer> ValoresFlujoAux = (LinkedList<Integer>) vectorDistancias.clone();

        int menor;
        int pos = 0;
        
        for (int i = 0; i < conf.getCandidatosGreedyAleatorizada(); i++) {
            menor = 0;
            for (int j = 0; j < tamTotal; j++) {
                if (ValoresFlujoAux.get(j) < menor) {
                    menor = ValoresFlujoAux.get(j);
                    pos = j;
                }
                vectorConcentricas[i] = pos;
                ValoresFlujoAux.set(pos, 9999);
            }
        }
    }

    /**
     * @Brief Función para elegir aleatoriamente un candidato de los que tienen mayor sumatoria de flujo
     * @return Un Entero con la unidad candidata
     */
    int aleatorioGreedyF() {
        int num = aleatorio.Randint(0, conf.getCandidatosGreedyAleatorizada() - 1);
        //System.out.println("Numero de flujo: " + vectorConcentricas[num]);
        return vectorMayores[num];
    }

    /**
     * @Brief Función para elegir un candidato de los que tienen peor sumatoria de distancias
     * @return Un Entero con la unidad candidata
     */
    int aleatorioGreedyD() {
        int num = aleatorio.Randint(0, conf.getCandidatosGreedyAleatorizada() - 1);
        //System.out.println("Numero de distancias: " + vectorConcentricas[num]);
        return vectorConcentricas[num];
    }

    /**
     * @Brief Función para generar los 10 candidatos del LRC 
     * @param solucionInicial Lista Enlazada de enteros con la solución inicial del Greedy
     */
    void greedyAletorizadoLRC(LinkedList<Integer> solucionInicial) {
        LRC.clear();
        int contador = 0;
        int numF;
        int numD;
        LinkedList<Integer> solLRC = (LinkedList<Integer>) solucionInicial.clone();
        //Lazo greedy
        while (contador < conf.getTamañoaLRC()) {
            encuentraConcentricos();
            encuentraMayores();
            numF = aleatorioGreedyF();
            numD = aleatorioGreedyD();
            permutacion(solLRC, numF, numD);
            LinkedList<Integer> nuevaSol = (LinkedList<Integer>) solLRC.clone();
            LRC.add(nuevaSol);
            contador++;
        }
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

    void mostrarLRC() {
        int Auxcoste;
        for (int k = 0; k < conf.getTamañoaLRC(); k++) {
            Auxcoste = 0;
            for (int i = 0; i < tamTotal; i++) {
                for (int j = 0; j < tamTotal; j++) {
                    Auxcoste += MF[i][j] * MD[LRC.get(k).get(i)][LRC.get(k).get(j)];
                }
            }
            System.out.println("El coste de la sol " + k + " del LRC es: " + Auxcoste);
        }
    }

    /**
     * @Brief Getter del log
     * @return Un StringBuilder con las cadenas de texto insertadas en el log
     */
    public StringBuilder getLog() {
        return log;
    }

    /**
     * @Brief Función principal de nuestro algoritmo Primer Mejor DLB Iterativo
     */
    void algoritmo() throws InterruptedException {
        long inicio = System.currentTimeMillis();
        LinkedList<Integer> soluciones = (LinkedList<Integer>) solInicial.clone();
        greedyAletorizadoLRC(soluciones);
        mejorGlobal = (LinkedList<Integer>) actual.clone();
        
        int i = 0;
        while (i < LRC.size()) {
            busquedaTabú((LinkedList<Integer>) LRC.get(i).clone());
            if (coste(mejorGlobalTabu) < coste(mejorGlobal)) {
                mejorGlobal = (LinkedList<Integer>) mejorGlobalTabu.clone();
            }
            log.append("Arranque: ").append(i).append("\n");
            System.out.println("Arranque: "+i);
            log.append("Mejor global tabu ").append(mejorGlobalTabu).append("coste: ").append(coste(mejorGlobalTabu)).append("\n");
            System.out.println("Mejor global tabu"+mejorGlobalTabu+"coste: "+coste(mejorGlobalTabu));
            log.append("Mejor global ").append(mejorGlobal).append("coste: ").append(coste(mejorGlobal)).append("\n\n");
            System.out.println("Mejor global "+mejorGlobal+"coste: "+coste(mejorGlobal));
            i++;

        }
        long fin = System.currentTimeMillis();
        log.append("TIEMPO: ").append(fin - inicio).append(" ms\n");
    }

    /**
     * @Brief Función de búsqueda tabú
     * @param actualAleatorizado Lista Enlaza de enteros con el candidato LRC actual
     * @return Una Lista Enlazada de Enteros con el mejor Global Tabú
     */
    public LinkedList<Integer> busquedaTabú(LinkedList<Integer> actualAleatorizado) {
        actual = (LinkedList<Integer>) actualAleatorizado.clone();
        mejorGlobalTabu = (LinkedList<Integer>) actualAleatorizado.clone();
        int cont = 0;
        float iteracionesSinMejora = 0;
        boolean vecino;
        int num = aleatorio.Randint(0, tamTotal - 1);
        while (cont != numIteraciones) {
            vecino = busquedaVecino(num);
            //Si hay vecinos que mejoran
            if (vecino) {
                iteracionesSinMejora = 0;
               //System.out.println("hay vecinos:");
            
                if (coste(actual) < coste(mejorGlobalTabu)) {
                    mejorGlobalTabu = (LinkedList<Integer>) actual.clone();
                }
                //System.out.println("//////////vector actual " + actual.toString() + " coste" + coste(actual));
            //Si no hay vecinos que mejoran
            } else {
                actualizarMemCortoPlazo();
                iteracionesSinMejora++;
                //System.out.println("No hay vecinos");
                //reseteo dlb
                for (int i = 0; i < tamTotal; i++) {
                    dlb[i] = 0;
                }
                //mostrarMemCortoPlazo();
                //System.out.println("no hay vecinos, iteraciones sin mejora:" + iteracionesSinMejora);
                //Si llegamos al límite de iteraciones sin mejora
                if (iteracionesSinMejora == conf.getPorcentajeIteracionesOscilacion() * conf.getIteraciones()) {
                    
                    iteracionesSinMejora = 0;
                    //System.out.println("///////////////////////OSCILACION ESTRATEGICA/////////////////////");
                    //Calculamos un numero de elementos dividiendo entre la oscilación estrategia
                    int numElementos = Math.round(1 / conf.getProbOscilacionEstr());
                    //System.out.println("num elementos oscilacion" + numElementos);
                    int rand = (int) aleatorio.Randint(1, numElementos);
                    //System.out.println("random " + rand);
                    //Calculamos un punto de corte en funcion de ls oscilación
                    if (rand < numElementos * conf.getProbOscilacionEstr()) {
                        //System.out.println("exporacion rand= " + rand + "<" + numElementos * conf.getProbOscilacionEstr());
                        Par p = exploracion();
                        permutacion((LinkedList<Integer>) actual, p.getI(), p.getJ());
                    } else {
                        //System.out.println("explotacion rand= " + rand + ">" + numElementos * conf.getProbOscilacionEstr());
                        Par p = explotacion();
                        permutacion((LinkedList<Integer>) actual, p.getI(), p.getJ());
                    }
                //Si llegamos al límite de iteraciones sin mejora
                } else {
                    //Y si hay algun mejor peor vecino
                    if (!mejoresPeoresVecinos.isEmpty()) {
                        //System.out.println("Elegimos mejor peor");
                       
                        //mostrarMejoresPeoresVecinos();
                        //System.out.println("mejor peor " + obtenerMejorPeorVecino().getVector() + " coste:" + coste(obtenerMejorPeorVecino().getVector()));
                        actual = (LinkedList<Integer>) obtenerMejorPeorVecino().getVector().clone();
                        
                        //mostrarMemCortoPlazo();
                        memCortoPlazo.add(new Par(ultimaI, ultimaJ, (LinkedList<Integer>) actual.clone()));
                        memLargoPlazo[ultimaI][ultimaJ]++;
                        
                        if(memCortoPlazo.size()==4){
                            memCortoPlazo.removeFirst();
                        }
                        actualizarMemCortoPlazo();
                        
                        if (coste(actual) < coste(mejorGlobalTabu)) {
                            mejorGlobalTabu = (LinkedList<Integer>) actual.clone();
                        }
                        //System.out.println("//////////vector actual " + actual.toString() + " coste" + coste(actual));
                    }
                }
            }
            cont++;
        }
        return mejorGlobalTabu; //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @Brief Función para decrementar la tenencia tabú de los elementos de la Memoria a Corto Plazo
     */
    void actualizarMemCortoPlazo(){
        for(int i=0;i<memCortoPlazo.size();i++){
            memCortoPlazo.get(i).decrementarTenencia();
        }
        for(int i=0;i<memCortoPlazo.size();i++){
            if(memCortoPlazo.get(i).getTenenciaTabu()==-1){
                memCortoPlazo.remove(i);
                i--;
            }
        }
    }

    /**
     * @Brief Función para explorar un movimiento no usado
     * @return El Par con el movimiento
     */
    Par exploracion() {
        int randomI = aleatorio.Randint(0, tamTotal - 1);
        int randomJ = aleatorio.Randint(0, tamTotal - 1);
        while (memLargoPlazo[randomI][randomJ] != 0) {
            randomI = aleatorio.Randint(0, tamTotal - 1);
            randomJ = aleatorio.Randint(0, tamTotal - 1);
        }

        Par p = new Par(randomI, randomJ, null);
        return p;
    }

    /**
     * @Brief Función para explotar el movimiento más usado
     * @return El Par con el movimiento
     */
    Par explotacion() {
        int valorPosicionMasIterada = -1;
        int posIItMayor = 0;
        int posJItMayor = 0;
        for (int i = 0; i < tamTotal; i++) {
            for (int j = 0; j < tamTotal; j++) {
                if (memLargoPlazo[i][j] > valorPosicionMasIterada) {
                    valorPosicionMasIterada = memLargoPlazo[i][i];
                    posIItMayor = i;
                    posJItMayor = j;
                }
            }
        }
        Par p = new Par(posIItMayor, posJItMayor, null);
        return p;
    }

    /**
     * @Brief Función que comprueba si un movimiento está en la Memoria a Corto Plazo
     * @param unI Entero con la posición 1 de la permutación
     * @param unJ Entero con la posición 2 de la permutación
     * @return Un boolean que indica si es tabú
     */
    boolean esTabu(int unI, int unJ) {
        boolean tabu = false;
        for (int i = 0; i < memCortoPlazo.size(); i++) {
            //comprobamos (i,j) y (j,i)
            if (memCortoPlazo.get(i).getI() == unI && memCortoPlazo.get(i).getJ() == unJ) {
                tabu = true;
            }

            if (memCortoPlazo.get(i).getI() == unJ && memCortoPlazo.get(i).getJ() == unI) {
                tabu = true;
            }

        }
        return tabu;
    }

    /**
     * @Brief Función para obtener el mejor peor vecino
     * @return 
     */
    Par obtenerMejorPeorVecino() {
        int pos = mejoresPeoresVecinos.size() - 1;
        while (esTabu(mejoresPeoresVecinos.get(pos).getI(), mejoresPeoresVecinos.get(pos).getJ())) {
            pos--;
        }
        return mejoresPeoresVecinos.get(pos);
    }

    /**
     * @Brief Función para explorar todas las unidades y encontrar mejorPrimerVecinos
     * @param num Entero con la unidad de la que partimos
     * @return Boolean que indica si hemos encontrado vecino
     */
    boolean busquedaVecino(int num) {

        boolean improve_flag = false;
        boolean vecino = false;
        //System.out.println("Vecinos de "+actual.toString());
        LinkedList<Integer> mejorPeorAux = (LinkedList<Integer>) actual.clone();

        mejoresPeoresVecinos.clear();
        
        int mejorPeorCoste = 99999;
        LinkedList<Integer> vaux = actual;
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
                        
                        if (!esTabu(i, j)) {
                            
                            vecino = true;
                            ultimaI = i;
                            ultimaJ = j;
                            //mostrarMemCortoPlazo();
                            memCortoPlazo.add(new Par(ultimaI, ultimaJ, (LinkedList<Integer>) actual.clone()));
                            memLargoPlazo[ultimaI][ultimaJ]++;
                            
                            if(memCortoPlazo.size()==4){
                                memCortoPlazo.removeFirst();
                            }
                            actualizarMemCortoPlazo();
            
                            actual=(LinkedList<Integer>) vaux.clone();
                        }

                    } else {

                        permutacion(mejorPeorAux, i, j);
                        //solo guardamos un mejorPeorVecinoNoTabu
                        if (coste(mejorPeorAux) < mejorPeorCoste) {
                            //System.out.println("mejorespeores"+mejorPeorAux+" coste "+coste(mejorPeorAux));
                            mejorPeorCoste = coste(mejorPeorAux);

                            

                            if (!esTabu(i, j)) {
                                
                                ultimaI = i;
                                ultimaJ = j;
                                Par p = new Par(i, j, (LinkedList<Integer>) vaux.clone());
                                mejoresPeoresVecinos.add(p);
                            }
                        }

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

        //System.out.println("Solucion parcial: " + coste(mejorVecino));
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
        if (completo) {
            //System.out.println("Espacio completo");
        }
        return completo;
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
     * @Brief función para calcular la factorización parte 2
     * @param r Entero con la i
     * @param s Entero con la j
     * @return Entero con la factorización
     */
    int factorizacion(int r, int s) {
        int valor = 0;
        valor += MF[r][r] * (MD[actual.get(s)][actual.get(s)] - MD[actual.get(r)][actual.get(r)])
                + MF[s][s] * (MD[actual.get(r)][actual.get(r)] - MD[actual.get(s)][actual.get(s)])
                + MF[r][s] * (MD[actual.get(s)][actual.get(r)] - MD[actual.get(r)][actual.get(s)])
                + MF[s][r] * (MD[actual.get(r)][actual.get(s)] - MD[actual.get(s)][actual.get(r)]);

        for (int k = 0; k < tamTotal; k++) {
            if (r != k && s != k) {
                valor += funcion(k, r, s);
            }
        }
        return valor;
    }

    /**
     * @Brief Función para calcular la factorización parte 1
     * @param k Entero con el valor desde 0 hasta el tam máximo
     * @param r Entero con la i
     * @param s Entero con la j
     * @return Un entero con la instacia k
     */
    int funcion(int k, int r, int s) {
        int valor = MF[r][k] * (MD[actual.get(s)][actual.get(k)] - MD[actual.get(r)][actual.get(k)])
                + MF[s][k] * (MD[actual.get(r)][actual.get(k)] - MD[actual.get(s)][actual.get(k)])
                + MF[k][r] * (MD[actual.get(k)][actual.get(s)] - MD[actual.get(k)][actual.get(r)])
                + MF[k][s] * (MD[actual.get(k)][actual.get(r)] - MD[actual.get(k)][actual.get(s)]);

        return valor;
    }

    /**
     * @Brief Función para mostrar la memoria a corto plazo
     */
    void mostrarMemCortoPlazo() {
        System.out.println("Memoria corto plazo");
        for (int i = 0; i < memCortoPlazo.size(); i++) {
            System.out.println(memCortoPlazo.get(i).getI() + " " + memCortoPlazo.get(i).getJ() + " | "+memCortoPlazo.get(i).getTenenciaTabu());
        }
        System.out.println("");
    }

    /**
     * @Brief Función para mostrar la memoria a Largo Plazo
     */
    void mostrarMemLargoPlazo() {
        System.out.println("Memoria largo plazo");
        for (int i = 0; i < tamTotal; i++) {
            for (int j = 0; j < tamTotal; j++) {
                System.out.print(memLargoPlazo[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * @Brief Función para mostrar los mejores peores vecinos
     */
    void mostrarMejoresPeoresVecinos() {
        //mostrarMemCortoPlazo();
        System.out.println("Mejores Peores Vecinos");
        for (int i = 0; i < mejoresPeoresVecinos.size(); i++) {
            System.out.println(mejoresPeoresVecinos.get(i).getI() + " "
                    + mejoresPeoresVecinos.get(i).getJ() + " "
                    + mejoresPeoresVecinos.get(i).getVector().toString() + " coste: "
                    + coste(mejoresPeoresVecinos.get(i).getVector())
                    + " tabu " + esTabu(mejoresPeoresVecinos.get(i).getI(), mejoresPeoresVecinos.get(i).getJ()));
        }
    }
   
}
