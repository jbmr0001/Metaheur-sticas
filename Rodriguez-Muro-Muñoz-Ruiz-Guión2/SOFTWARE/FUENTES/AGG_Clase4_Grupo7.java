/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mhpr2;

import java.util.LinkedList;

/**
 *
 * @author Grupo 7
 */
public class AGG_Clase4_Grupo7 {

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
     * Lista Enlazda de Enteros para almacenar la Solución inicial Greedy
     */
    LinkedList<Integer> solInicial = new LinkedList<>();

    /**
     * Fichero de cofiguración
     */
    private final Configuracion conf;

    /**
     * Vector auxiliar para almacenar las cinco unidades con mayores sumatoria
     * de flujos
     */
    private int[] vectorMayores = new int[0];

    /**
     * Vector auxiliar para almacenar las cinco unidades con peor sumatoria de
     * distancias
     */
    private int[] vectorConcentricas = new int[0];

    /**
     * Lista Enlazada de Listas Enlazadas de enteros para almacenar la Lista
     * Restringida de Candidatos
     */
    private final LinkedList<LinkedList<Integer>> LRC = new LinkedList<>();

    /**
     * MetaRandom para crear aleatorios con nuestra semilla
     */
    private final MetaRandom aleatorio;

    private final int tamañoPoblacion;

    private LinkedList<LinkedList<Integer>> poblacion;

    private final LinkedList<LinkedList<Integer>> elegidosOperadorSeleccion;

    /*Guardamos el elite de cada generación*/
    private LinkedList<Integer> elite;

    /* Probabilidad de cruzar*/
    private float ProbCruce;

    /*Número de evaluaciones*/
    int numEvaluaciones;
    
    /*Variable para almacenar la semilla*/
    long semilla;

    public AGG_Clase4_Grupo7(int tam, int[][] MFlujo, int MDistancia[][], LinkedList<Integer> solucionInicial, Configuracion c, Long semilla) {
        this.tamTotal = tam;
        this.MF = MFlujo.clone();
        this.MD = MDistancia.clone();
        this.solInicial = solucionInicial;
        this.conf = c;
        this.tamañoPoblacion = c.getTamañoPoblacion();
        this.log = new StringBuilder();
        this.aleatorio = new MetaRandom();
        this.aleatorio.Set_random(semilla);
        calculaSumatoriaDistancia();
        calculaSumatoriaFlujo();
        this.vectorConcentricas = new int[conf.getCandidatosGreedyAleatorizada()];
        this.vectorMayores = new int[conf.getCandidatosGreedyAleatorizada()];
        //Inicialización
        LinkedList<Integer> ValorNulo = new LinkedList<>();
        this.ProbCruce = c.getProbCruceGeneracional() * 100;

        for (int i = 0; i < tamTotal; i++) {
            LRC.add(ValorNulo);
        }

        poblacion = new LinkedList<>();
        elegidosOperadorSeleccion = new LinkedList<>();
        elite = new LinkedList<>();
        numEvaluaciones = 0;
        
        this.semilla = semilla;
    }

    /**
     * Función para almacenar en un vector auxiliar los numCandidatos unidades
     * con mayor sumatoria flujo
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
     * Función para almacenar en un vector auxiliar los numCandidatos unidades
     * con peor sumatoria de distancias
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
     * @Brief Función para elegir aleatoriamente un candidato de los que tienen
     * mayor sumatoria de flujo
     * @return Un Entero con la unidad candidata
     */
    int aleatorioGreedyF() {
        int num = aleatorio.Randint(0, conf.getCandidatosGreedyAleatorizada() - 1);
        //System.out.println("Numero de flujo: " + vectorConcentricas[num]);
        return vectorMayores[num];
    }

    /**
     * @Brief Función para elegir un candidato de los que tienen peor sumatoria
     * de distancias
     * @return Un Entero con la unidad candidata
     */
    int aleatorioGreedyD() {
        int num = aleatorio.Randint(0, conf.getCandidatosGreedyAleatorizada() - 1);
        //System.out.println("Numero de distancias: " + vectorConcentricas[num]);
        return vectorConcentricas[num];
    }

    /**
     * @Brief Función para generar los 10 candidatos del LRC
     * @param solucionInicial Lista Enlazada de enteros con la solución inicial
     * del Greedy
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
     * @Brief Operador 2-opt que realiza un permutación en el vector en las
     * posiciones dadas
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
     * @Brief Función para crear la población inicial con tamLRC invividuos
     * generados en el greedy aleatorizado y los restantes hasta el tamaño total
     * aleatorios
     */
    void creaPoblacion() {
        LinkedList<Integer> soluciones = (LinkedList<Integer>) solInicial.clone();
        greedyAletorizadoLRC(soluciones);
        long costeMax = 1999999999;

        for (int i = 0; i < tamañoPoblacion; i++) {

            //Individuos LRC
            if (i < conf.getTamañoLRC()) {
                poblacion.add(LRC.get(i));
                //System.out.println("Individuo LRC " + i + ": " + poblacion.get(i).toString() + " Coste:" + coste(poblacion.get(i)));
                //Inidividuos aleatorios
            } else {
                poblacion.add(generarInividuoAleatorio());
                //System.out.println("Individuo Aleatorio " + i + ": " + poblacion.get(i).toString() + " Coste:" + coste(poblacion.get(i)));
            }
            numEvaluaciones++;
            if (coste(poblacion.get(i)) < costeMax) {
                //System.out.println(coste(poblacion.get(i))+"<"+coste(mejor));
                //System.out.println(poblacion.get(i).toString());
                elite = (LinkedList<Integer>) poblacion.get(i).clone();
                costeMax = coste(poblacion.get(i));
            }
        }
    }

    /**
     * @Brief Función para mostrar la población junto con su índice
     */
    void muestraPoblacion() {
        for (int i = 0; i < tamañoPoblacion; i++) {
            System.out.println("Inidviduo:" + i + " " + poblacion.get(i).toString());
        }
        System.out.println();
    }

    void algoritmo() {
        System.out.println("///////////"+conf.getCruceGeneracional()+"-SEMILLA:"+this.semilla+"//////////");
        long inicio = System.currentTimeMillis();
        creaPoblacion();
        int pos = 0;
        int generacion = 0;
        LinkedList<Integer> EliteAux = new LinkedList<>();

        while (numEvaluaciones < conf.getNumEvaluaciones()) {
            //Seleccionamos los individuos que pasaran a la siguiente generación
            torneoSeleccion(tamañoPoblacion, 2);
            //System.out.println("Tam operador seleccion: " + elegidosOperadorSeleccion.size());
            cruce();
            //Mutamos a la población entera
            for(int i = 0; i < tamañoPoblacion; i++){
                mutaciones((LinkedList<Integer>) elegidosOperadorSeleccion.get(i).clone(),i);
            }
            //System.out.println("Acaba las mutaciones");
            //Comprobamos si el elite sigue en la población
            if(!contieneElite(elegidosOperadorSeleccion)){
                //System.out.println("No contiene elite");
                pos = buscarPeor(elegidosOperadorSeleccion);
                //System.out.println("Coste del peor: " + coste(elegidosOperadorSeleccion.get(pos)) + " posición " + pos);
                elegidosOperadorSeleccion.set(pos, elite);
            }
            //System.out.println("Buscamos el elite");
            //Volvemos a buscar el elite
            EliteAux = buscaElite(elegidosOperadorSeleccion);
            if(coste(EliteAux) < coste(elite)){
                elite = (LinkedList<Integer>) EliteAux.clone();
                log.append("EN GENERACIÓN: ").append(generacion).append(" COSTE: ").append(coste(elite)).append(elite.toString()).append("\n");
            }
            //System.out.println("Nuevo elite: " + coste(elite));
            //Actualizamos la población
            poblacion = (LinkedList<LinkedList<Integer>>) elegidosOperadorSeleccion.clone();
            
            //System.out.println("Elite: " + elite.toString() + " coste: " + coste(elite));
            elegidosOperadorSeleccion.clear();
            numEvaluaciones += tamañoPoblacion;
            generacion++;
            
        }

        System.out.println("Elite: " + coste(elite));
        long fin = System.currentTimeMillis();
        log.append("TIEMPO: ").append(fin - inicio).append(" ms\n");
    }

    void cruce() {
        int random;
        int a = 0;
        int b = 1;
        random = aleatorio.Randint(0, 100);
        //System.out.println("Prob de cruce: " + ProbCruce + " Random escogido: " + random);
        if (random < ProbCruce) {
            //System.out.println("Hay cruce");
            switch (conf.getCruceGeneracional()){
                      case "OX2":
                          //System.out.println("OX2");
                          for(int i = 0; i < tamañoPoblacion/2; i++){
                              //System.out.println("a: " + a + " b: " + b);
                              cruceOX2((LinkedList<Integer>) elegidosOperadorSeleccion.get(a).clone(), (LinkedList<Integer>) elegidosOperadorSeleccion.get(b).clone());
                              a += 2;
                              b += 2;
                          }
                          break;
                      case "PMX":
                         for(int i = 0; i < tamañoPoblacion/2; i++){
                              //System.out.println("a: " + a + " b: " + b);
                              crucePMX((LinkedList<Integer>) elegidosOperadorSeleccion.get(a).clone(), (LinkedList<Integer>) elegidosOperadorSeleccion.get(b).clone());
                              a += 2;
                              b += 2;
                          }
                         break;
                      default:
                          System.out.println("Cruce desconocido debe ser (OX,PMX)");
                          break;    
                  }
        }

    }

    void mutaciones(LinkedList<Integer> v,int pos){
        LinkedList<Integer> posMuta=new LinkedList<>();
        //System.out.println("MUTAMOS"+v.toString());
        //Calculamos un numero de elementos
        //System.out.println(v.toString());
        for(int i=0;i<tamTotal;i++){
            int numElementos = Math.round(1 / conf.getConstanteParaFactorMutacionGen()*tamTotal);  
            //System.out.println("Numero elementos: " + numElementos);
            int rand = (int) aleatorio.Randint(1, numElementos);
            //Calculamos un punto de corte en funcion de la probabilidad de cruce
            //En caso de encontrarse en el rango mutamos
            if (rand < numElementos * conf.getConstanteParaFactorMutacionGen()*tamTotal){
                posMuta.add(i);
            }
            //Solo si hay al menos dos individuos mutables
            if(posMuta.size()==2){
               //System.out.println(posMuta.toString());
                
                permutacion(v,posMuta.removeFirst(),posMuta.removeFirst());
                
                //System.out.println();
            }
            
        }
        //System.out.println(v.toString());
        elegidosOperadorSeleccion.set(pos, (LinkedList<Integer>) v.clone());
       
    }

    void torneoSeleccion(int veces, int k) {
        int posRandom;
        LinkedList<LinkedList<Integer>> poblacionAux = (LinkedList<LinkedList<Integer>>) poblacion.clone();
        LinkedList<Integer> individuoMenorCoste = new LinkedList<>();
        LinkedList<Integer> individuo = new LinkedList<>();
        long menorCoste;
        for (int i = 0; i < veces; i++) {
            menorCoste = 1999999999;
            for (int j = 0; j < k; j++) {
                posRandom = aleatorio.Randint(0, poblacionAux.size() - 1);
                individuo = (LinkedList<Integer>) poblacionAux.get(posRandom).clone();
                if (coste(individuo) < menorCoste) {
                    individuoMenorCoste = (LinkedList<Integer>) individuo.clone();
                    menorCoste = coste(individuoMenorCoste);
                }
            }
            elegidosOperadorSeleccion.add((LinkedList<Integer>) individuoMenorCoste.clone());
        }
    }

    LinkedList<Integer> generarInividuoAleatorio() {
        boolean posicionRellenada;
        LinkedList<Integer> aux = new LinkedList<>();
        int random;
        for (int i = 0; i < tamTotal; i++) {
            posicionRellenada = false;
            while (!posicionRellenada) {
                random = aleatorio.Randint(0, tamTotal - 1);
                if (!contiene(random, aux)) {
                    posicionRellenada = true;
                    aux.add(random);
                }
            }
        }
        return aux;
    }

    boolean contiene(int elem, LinkedList<Integer> list) {
        //System.out.println("Elemento a buscar: " + elem);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == elem) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Brief Getter del log
     * @return Un StringBuilder con las cadenas de texto insertadas en el log
     */
    public StringBuilder getLog() {
        return log;
    }

    /**
     * @Brief Funcion para calcular el coste de la solución
     * @param Vsolucion Lista Enlazada con el vector del que queremos calcular
     * el coste
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
     * @brief operador de cruce OX2
     * @param escogido1 primer descendiente
     * @param escogido2 segundo descendiente
     */
    private void cruceOX2(LinkedList<Integer> escogido1, LinkedList<Integer> escogido2) {
        //System.out.println("/////////////////////////OX2///////////////////////////////");
        LinkedList<Integer> auxPadre1 = (LinkedList<Integer>) escogido1.clone();
        LinkedList<Integer> auxPadre2 = (LinkedList<Integer>) escogido2.clone();

        //System.out.println("Primer padre: " + auxPadre1.toString());
        //System.out.println("Segundo padre: " + auxPadre2.toString());
        LinkedList<Integer> hijo1 = (LinkedList<Integer>) escogido1.clone();
        LinkedList<Integer> hijo2 = (LinkedList<Integer>) escogido2.clone();
        LinkedList<Boolean> booleanos = new LinkedList<>();

        //posiciones aleatorias
        for (int i = 0; i < tamTotal; i++) {
            int random = aleatorio.Randint(0, 1);
            if (random == 0) {
                booleanos.add(Boolean.FALSE);
            } else {
                booleanos.add(Boolean.TRUE);
            }

        }

        LinkedList<Integer> individuosTrue1 = new LinkedList<>();
        LinkedList<Integer> individuosTrue2 = new LinkedList<>();
        //si una posicion esta a true buscamos el elemento que hay en esa posicion en el padre y la desactivamos la posicion en la que se encuentre ese elemento en el hijo
        for (int i = 0; i < tamTotal; i++) {
            if (booleanos.get(i) == true) {
                hijo1.set(auxPadre2.get(i), -1);
                individuosTrue1.add(auxPadre2.get(i));

                hijo2.set(auxPadre1.get(i), -1);
                individuosTrue2.add(auxPadre1.get(i));
            }
        }
        //System.out.println("Primer hijo modif: " + hijo1.toString());
        //System.out.println("Segundo hijo modif: " + hijo2.toString());
        //System.out.println();
        //System.out.println("Individuos para hijo1 " + individuosTrue1.toString());
        //System.out.println("Individuos para hijo2 " + individuosTrue2.toString());
        for (int i = 0; i < tamTotal; i++) {
            if (hijo1.get(i) == -1) {
                hijo1.set(i, individuosTrue1.removeFirst());
            }

            if (hijo2.get(i) == -1) {
                hijo2.set(i, individuosTrue2.removeFirst());
            }
        }

        //System.out.println();
        escogido1 = hijo1;
        escogido2 = hijo2;
        //System.out.println("Primer hijo: " + hijo1.toString());
        //System.out.println("Segundo hijo: " + hijo2.toString());

    }

    public void realizarCorte(LinkedList<Integer> list, int x, int y) {
        for (int i = y + 1; i != x; i++) {
            list.set(i, 999999);
            if (i == tamTotal - 1) {
                i = -1;
            }
        }
    }

    /**
     * @brief Operador de cruce PMX
     * @param escogido1 Lista Enlazada con el primer padre
     * @param escogido2 Lista Enlazada con el segundo padre
     */
    private void crucePMX(LinkedList<Integer> escogido1, LinkedList<Integer> escogido2) {
        //System.out.println("////////////////////////PMX/////////////////////////////");
        LinkedList<Integer> auxPadre1 = (LinkedList<Integer>) escogido1.clone();
        LinkedList<Integer> auxPadre2 = (LinkedList<Integer>) escogido2.clone();
        //System.out.println("Primer padre: " + escogido1.toString());
        //System.out.println("Segundo padre: " + escogido2.toString());
        //los clonamos a la inversa ya que solo mantendremos lasposiciones dentro del rango
        LinkedList<Integer> hijo1 = (LinkedList<Integer>) escogido2.clone();
        LinkedList<Integer> hijo2 = (LinkedList<Integer>) escogido1.clone();
        //Realizamos el proceso de cruce
        int x = aleatorio.Randint(0, tamTotal - 3); //Marca el primer corte
        int y = aleatorio.Randint(x, tamTotal - 2); //Marca el segundo corte

        //System.out.println("\nElementos del corte: valor x: " + x + " valor y: " + y);
        realizarCorte(escogido1, x, y);
        realizarCorte(escogido2, x, y);

        LinkedList<Integer> corte1 = new LinkedList<>();
        LinkedList<Integer> corte2 = new LinkedList<>();

        for (int i = 0; i < tamTotal; i++) {
            if (i < x || i > y) {
                hijo1.set(i, -1);
                hijo2.set(i, -1);
            }
        }
        //Copiamos los elementos del corte
        for (int i = x; i <= y; i++) {
            escogido1.set(i, auxPadre2.get(i));
            corte1.add(auxPadre1.get(i));
            escogido2.set(i, auxPadre1.get(i));
            corte2.add(auxPadre2.get(i));
        }
        //System.out.println();
        //System.out.println("Primer corte: " + corte1.toString());
        //System.out.println("Segundo corte: " + corte2.toString());

        int mapeo[] = new int[tamTotal];
        for (int i = 0; i < tamTotal; i++) {
            mapeo[i] = 0;
        }
        for (int i = 0; i < corte1.size(); i++) {
            mapeo[corte2.get(i)] = corte1.get(i);
        }
        /*
        for (int i = 0; i < tamTotal; i++) {
            if (mapeo[i] != 0) {
                System.out.println(i + " <-> " + mapeo[i] + " ");
            }
        }*/
        //System.out.println();
        //System.out.println("Primer hijo con corte: " + hijo1.toString());
        //System.out.println("Segundo hijo con corte: " + hijo2.toString());

        //hijo1
        //System.out.println("Hijo1");
        for (int i = 0; i < auxPadre1.size(); i++) {
            if (hijo1.get(i) == -1) {
                //System.out.println("Hijo1:llenando elem " + i);
                if (contiene(auxPadre1.get(i), hijo1)) {
                    boolean encontrado = false;
                    int elem = auxPadre1.get(i);
                    //System.out.println(hijo1.toString());
                    //System.out.println("Esta contenido " + elem);
                    while (!encontrado) {
                        if (!contiene(elem, hijo1)) {
                            encontrado = true;
                        } else {
                            //System.out.println("Se pilla aquí");
                            //System.out.println(elem + " -> " + mapeo[elem]);
                            elem = mapeo[elem];
                        }
                    }
                    hijo1.set(i, elem);
                } else {
                    //System.out.println("no contenido " + auxPadre1.get(i));
                    hijo1.set(i, auxPadre1.get(i));
                }
            }
        }
        for (int i = 0; i < corte1.size(); i++) {
            mapeo[corte1.get(i)] = corte2.get(i);
        }
        //hijo2
        //System.out.println("Hijo2");
        for (int i = 0; i < auxPadre2.size(); i++) {
            if (hijo2.get(i) == -1) {
                //System.out.println("Hijo2: llenando elem " + i);
                if (contiene(auxPadre2.get(i), hijo2)) {
                    //System.out.println("Esta contenido " + auxPadre2.get(i));
                    boolean encontrado = false;
                    int elem = auxPadre2.get(i);
                    while (!encontrado) {
                        if (!contiene(elem, hijo2)) {
                            encontrado = true;
                        } else {
                            //System.out.println(elem + " -> " + mapeo[elem]);
                            elem = mapeo[elem];
                        }
                    }
                    hijo2.set(i, elem);
                } else {
                    //System.out.println("no contenido " + auxPadre2.get(i));
                    hijo2.set(i, auxPadre2.get(i));
                }
            }
        }
        escogido1 = hijo1;
        escogido2 = hijo2;
        //System.out.println("Primer hijo: " + hijo1.toString());
        //System.out.println("Segundo hijo: " + hijo2.toString());
    }

    LinkedList<Integer> buscaElite(LinkedList<LinkedList<Integer>> generacion) {
        LinkedList<Integer> aux;
        //Copiamos el primer de los elementos de la generación
        aux = (LinkedList<Integer>) generacion.get(0).clone();
        for (int i = 1; i < tamañoPoblacion; i++) {
            if (coste(aux) > coste(generacion.get(i))) {
                aux = (LinkedList<Integer>) generacion.get(i).clone();
            }
        }
        return aux;
    }

    boolean contieneElite(LinkedList<LinkedList<Integer>> generacion) {
        int fitnessElite = coste(elite);
        //System.out.println("Valor elite: " + fitnessElite);
        for (int i = 0; i < tamañoPoblacion; i++) {
            if (coste(generacion.get(i)) == fitnessElite) {
                return true;
            }
        }
        return false;
    }

    int buscarPeor(LinkedList<LinkedList<Integer>> poblacion) {
        int peorCoste = 0;
        int pos = 0;
        for (int i = 0; i < tamañoPoblacion; i++) {
            if (peorCoste < coste(poblacion.get(i))) {
                pos = i;
                peorCoste = coste(poblacion.get(i));
            }
        }
        return pos;
    }

    /**
     * @brief Método auxiliar para encontrar la posicion de un elemento
     * @param list, lista en la que se quiere buscar
     * @param elemento, elemento a buscar en la lista
     * @return devuelve la posicion de un elemento
     */
    int posElemento(LinkedList<Integer> list, int elemento) {
        for (int i = 0; i < tamTotal; i++) {
            if (list.get(i) == elemento) {
                return i;
            }
        }
        return 0;
    }
}