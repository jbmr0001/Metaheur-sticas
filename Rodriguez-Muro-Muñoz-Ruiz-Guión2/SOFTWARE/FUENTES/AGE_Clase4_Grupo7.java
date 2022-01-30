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
public class AGE_Clase4_Grupo7 {

    /**
     * Número total de unidades
     */
    private int tamTotal;

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

    /**
     * Lista Enlazada con la solución en la que nos encontramos
     */
    private LinkedList<Integer> actual = new LinkedList<>();
    
    /**
     * Entero con el tamaño de la población
     */
    private final int tamañoPoblacion;

    /**
     * Lista Enlazada de Listas Enlazadas de Enteros para almacenar la ponlación de vectores solución
     */
    private final LinkedList<LinkedList<Integer>> poblacion;
  
    /**
     * Lista Enlazada de Enteros para almacenar el vector solución con menor coste
     */
    private LinkedList<Integer> mejor;

    /**
     * Lista Enlazada de Listas Enlazadas de Enteros para almacenar los vectores elegidos en el Torneo de Selección
     */
    private final LinkedList<LinkedList<Integer>> elegidosOperadorSeleccion;
    
    /**
     * Lista Enlazada de Listas Enlazadas de Enterios para almacenar los vectores elegidos en el Torneo de Reemplazo
     */
    private final LinkedList<LinkedList<Integer>> elegidosReemplazo;
    
    /**
     * Lista Enlazada de Enteros para almacenar las posiciones de la población a reemplazar
     */
    private final LinkedList<Integer> posicionesReemplazar;
    
    /**
     * Entero para guardar el número de evaluaciones máximas de nuestro algoritmo
     */
    private int numEvaluaciones;
    
    /**
     * Long para almacenar la semilla actual de la ejecución
     */
    private final Long semilla;

    /**
     * Entero con la k del torneo de selección, número de individuos que serán elegidos aleatoriamente
     */
    private final int kTorneoSeleccion;
    
    /**
     * Entero con el número de veces que se realiza el torneo de selección
     */
    private final int vecesTorneoSelecion;
    
    /**
     * Entero con la k del torneo de reemplazo, número de individuos que serán elegidos aletoriamente
     */
    private final int kTorneoReemplazo;
    
    /**
     * Entero con el número de veces que se realiza el torneo de reemplazo
     */
    private final int vecesTorneoReemplazo;
    
    /**
     * Entero con el número de generaciones
     */
    private int numGeneraciones;

    public AGE_Clase4_Grupo7(int tam, int[][] MFlujo, int MDistancia[][], LinkedList<Integer> solucionInicial, Configuracion c,Long semilla) {
        this.tamTotal = tam;
        this.MF = MFlujo.clone();
        this.MD = MDistancia.clone();
        this.solInicial = solucionInicial;
        this.conf = c;
        this.tamañoPoblacion = c.getTamañoPoblacion();
        this.log = new StringBuilder();
        this.aleatorio = new MetaRandom();
        this.aleatorio.Set_random(semilla);
        this.actual = (LinkedList<Integer>) solucionInicial.clone();
        calculaSumatoriaDistancia();
        calculaSumatoriaFlujo();
        this.vectorConcentricas = new int[conf.getCandidatosGreedyAleatorizada()];
        this.vectorMayores = new int[conf.getCandidatosGreedyAleatorizada()];
        //Inicialización
        LinkedList<Integer> ValorNulo = new LinkedList<>();

        for (int i = 0; i < tamTotal; i++) {
            LRC.add(ValorNulo);
        }
        this.poblacion= new LinkedList<>();
        this.elegidosOperadorSeleccion = new LinkedList<>();
        this.elegidosReemplazo = new LinkedList<>();
        this.posicionesReemplazar=new LinkedList<>();
        this.semilla=semilla;
        this.kTorneoSeleccion=3;
        this.vecesTorneoSelecion=2;
        this.kTorneoReemplazo=4;
        this.vecesTorneoReemplazo=2;
        this.mejor=new LinkedList<>();
        this.numEvaluaciones=0;
        this.numGeneraciones=0;
    }

    /**
     * @Brief Función para almacenar en un vector auxiliar los numCandidatos unidades
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
     * @Brief Función para almacenar en un vector auxiliar los numCandidatos unidades
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
        vector.set(i, b);
        vector.set(j, a);
    }
    
    /**
     * @Brief Función para crear la población inicial con tamLRC invividuos generados en el greedy aleatorizado y los restantes hasta el tamaño total aleatorios
     */
    void creaPoblacion(){
        LinkedList<Integer> soluciones = (LinkedList<Integer>) solInicial.clone();
        greedyAletorizadoLRC(soluciones);
        long costeMax=1999999999;
                    
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
            //Actualizamos las evaluaciones
            numEvaluaciones++;
            if(coste(poblacion.get(i))<costeMax){
                mejor=(LinkedList<Integer>) poblacion.get(i).clone();
                costeMax=coste(poblacion.get(i));
            }
        }
    }
    
    /**
     * @Brief Función para mostrar la población junto con su índice
     */
    void muestraPoblacion(){
        for (int i = 0; i < tamañoPoblacion; i++) {
            System.out.println("Inidviduo:"+i+" "+poblacion.get(i).toString());
        }
        System.out.println();
    }
    
    /**
     * @Brief Método principal de nuestro algoritmo
     */
    void algoritmo() {
        System.out.println("///////////"+conf.getCruceEstacionario()+"-SEMILLA:"+this.semilla+"//////////");
        long inicio = System.currentTimeMillis();
        creaPoblacion();
        
        while (numEvaluaciones < conf.getNumEvaluaciones()) {
            //Incrementamos las generaciones
            numGeneraciones++;
            torneoSeleccion(this.vecesTorneoSelecion, this.kTorneoSeleccion);
            
            cruce();
            torneoReemplazo(this.vecesTorneoReemplazo, this.kTorneoReemplazo);
            
            for(int i=0;i<this.vecesTorneoReemplazo;i++){
                //Llamamos las mutaciones desde este bucle para reducit complejidad
                mutaciones((LinkedList<Integer>) elegidosOperadorSeleccion.get(i).clone(),i);
                
                numEvaluaciones++;
                if(coste(elegidosOperadorSeleccion.get(i))<coste(elegidosReemplazo.get(i))){
                    
                    poblacion.set(posicionesReemplazar.get(i), elegidosOperadorSeleccion.get(i));
                    //System.out.println("reemplaza:"+coste(mejor));
                    //Actualizamos el mejor
                    if(coste(elegidosOperadorSeleccion.get(i))<coste(mejor)){
                        mejor=(LinkedList<Integer>) elegidosOperadorSeleccion.get(i).clone();
                        //System.out.println("EN GENERACIÓN:"+numGeneraciones+" COSTE:"+coste(mejor)+" "+mejor.toString());
                        log.append("EN GENERACIÓN:").append(numGeneraciones).append(" COSTE:").append(coste(mejor)).append(" ").append(mejor.toString()).append("\n");
                    }  
                }
            }
            elegidosOperadorSeleccion.clear();
            elegidosReemplazo.clear();
            posicionesReemplazar.clear();
        }
        //muestraPoblacion();
        System.out.println("MEJOR:"+coste(mejor)+ mejor.toString());
        long fin = System.currentTimeMillis();
        log.append("\nTIEMPO: ").append(fin - inicio).append(" ms\n");
        
    }
    
    /**
     * @Brief Función para determinar si hacemos cruce según un aleatorio en base a la probabilidad y qué tipo de cruce según el fichero de configuración
     */
    void cruce(){
        //Calculamos un numero de elementos
        int numElementos = Math.round((1 / conf.getProbCruceEstacionario()))*10; 
        //System.out.println(numElementos);
        int rand = (int) aleatorio.Randint(1, numElementos);
        //Calculamos un punto de corte en funcion de la probabilidad de cruce
        //En caso de encontrarnos en el rango cruzamos
        if (rand < numElementos * conf.getProbCruceEstacionario()){
                  //System.out.println(rand+"<"+numElementos * conf.getProbCruceEstacionario());
                 
                  switch (conf.getCruceEstacionario()){
                      case "OX":
                          //System.out.println("OX");
                          cruceOX((LinkedList<Integer>) elegidosOperadorSeleccion.get(0).clone(), (LinkedList<Integer>) elegidosOperadorSeleccion.get(1).clone());
                          break;
                      case "PMX":
                          //System.out.println("PMX");
                          crucePMX((LinkedList<Integer>) elegidosOperadorSeleccion.get(0).clone(), (LinkedList<Integer>) elegidosOperadorSeleccion.get(1).clone());
                          break;
                      default:
                          System.out.println("Cruce desconocido debe ser (OX,PMX, OX2)");
                          break;      
                  }
                  
        }
    }
    
    /**
     * @Brief Función para aplicar las mutaciones (permutaciones 2-opt) por cada par de posiciones del vector marcadas como mutables
     * @param v Lista Enlazada con el vector solución a mutar
     */
    void mutaciones(LinkedList<Integer> v,int pos){
        LinkedList<Integer> posMuta=new LinkedList<>();
        //Calculamos un numero de elementos
        //System.out.println(v.toString());
        for(int i=0;i<tamTotal;i++){
            int numElementos = Math.round(1 /conf.getConstanteParaFactorMutacionGen()*tamTotal);  
            //System.out.println(numElementos);
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
    
    /**
     * @Brief Torneo de reemplazo de nuestro algoritmo
     * @param veces Número de veces
     * @param k Numero de elementos aleatorios elegidos entre los que elegimos el peor 
     */
    void torneoReemplazo(int veces, int k){
            int posRandom;
            int posMayor=0;
            LinkedList<Integer> individuoMayorCoste = new LinkedList<>();
            LinkedList<LinkedList<Integer>> poblacionAux =(LinkedList<LinkedList<Integer>>) poblacion.clone();
            long mayorCoste;
            LinkedList<Integer> individuo;
            for (int i = 0; i < veces; i++) {
                mayorCoste = -1999999999;
                for (int j = 0; j < k; j++) {
                    //Calculamos el aleatorio
                    posRandom = aleatorio.Randint(0, poblacionAux.size()-1); 
                    if(posRandom>=poblacionAux.size()){
                        posRandom=poblacionAux.size()-1;
                    }
                    //Obtenemos el individuo de la población y lo comparamos con el mayor
                    individuo=(LinkedList<Integer>) poblacionAux.get(posRandom).clone();
                    if (coste(individuo) > mayorCoste) {
                        individuoMayorCoste = (LinkedList<Integer>) individuo.clone();
                        mayorCoste = coste(individuoMayorCoste);
                        posMayor=poblacion.indexOf(individuoMayorCoste);
                        poblacionAux.remove(posRandom);//lo borramos de la auxiliar para evitar repetidos
                    }
                }
                elegidosReemplazo.add(individuoMayorCoste);
                posicionesReemplazar.add(posMayor);  
            } 
    }
    
    /**
     * @Brief Torneo de selección de nuestro algoritmo
     * @param veces Número de veces
     * @param k Numero de elementos aleatorios elegidos entre los que elegimos el mejor
     */
    void torneoSeleccion(int veces, int k){
            int posRandom;
            LinkedList<LinkedList<Integer>> poblacionAux =(LinkedList<LinkedList<Integer>>) poblacion.clone();
            LinkedList<Integer> individuoMenorCoste = new LinkedList<>();
            LinkedList<Integer> individuo;
            long menorCoste;
            for (int i = 0; i < veces; i++) {
                menorCoste = 1999999999;
                for (int j = 0; j < k; j++) {
                    //Calculamos el aleatorio
                    posRandom = aleatorio.Randint(0, poblacionAux.size()-1); 
                    if(posRandom>=poblacionAux.size()){
                        posRandom=poblacionAux.size()-1;
                    }
                    individuo=(LinkedList<Integer>) poblacionAux.get(posRandom).clone();
                    //Obtenemos el individuo de la población y lo comparamos con el menor
                    if (coste(individuo) < menorCoste ) {
                        individuoMenorCoste = (LinkedList<Integer>) individuo.clone();
                        menorCoste = coste(individuoMenorCoste);
                        poblacionAux.remove(posRandom);//lo borramos de la auxiliar para evitar repetidos
                    } 
                }
                
                elegidosOperadorSeleccion.add((LinkedList<Integer>) individuoMenorCoste.clone()); 
            }     
    }
    
    /**
     * @Brief Función para generar un vector solución aleatorio factible
     * @return Lista Enlazada con el vector generado
     */
    LinkedList<Integer> generarInividuoAleatorio() {
        boolean posicionRellenada;
        LinkedList<Integer> aux = new LinkedList<>();
        int random;
        for (int i = 0; i < tamTotal; i++) {
            posicionRellenada = false;
            //Mientras no se haya llenado esa posición generamos un random
            while (!posicionRellenada) {
                random = aleatorio.Randint(0, tamTotal - 1);
                if (!contiene(random, aux)) {//Comprobamos la factibilidad
                    posicionRellenada = true;
                    aux.add(random);
                }
            }
        }
        return aux;
    }

    /**
     * Función para comprobar si un elemento está contenido en un vector 
     * @param elem Elemento a comprobar
     * @param list Vector en el que realizar la comprobación
     * @return Un boolean que indica si está contenido o no
     */
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
     * @Brief Funcion para comprobar si una permutación mejora en función de la
     * factorizacón
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
        //System.out.println();
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
        //System.out.println();
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
     * @brief Operador de cruce OX
     * @param escogido1 Lista Enlazada con el primer padre
     * @param escogido2 Lista Enlazada con el segundo padre
     */
    private void cruceOX(LinkedList<Integer> esc1, LinkedList<Integer> esc2) {
        //System.out.println("/////////////////////////OX1///////////////////////////////");
        LinkedList<Integer> auxPadre1 = (LinkedList<Integer>) esc1.clone();
        LinkedList<Integer> auxPadre2 = (LinkedList<Integer>) esc2.clone();

        //System.out.println("Primer padre: " + esc1.toString());
        //System.out.println("Segundo padre: " + auxPadre2.toString());
       //System.out.println();
        //Realizamos el proceso de cruce
        int x = aleatorio.Randint(1, tamTotal - 3); //Marca el primer corte
        int y = aleatorio.Randint(x, tamTotal - 2); //Marca el segundo corte
        boolean esta;

        //System.out.println("Elementos del corte: " + x + " Valor y: " + y);
        realizarCorte(esc1, x, y);
        realizarCorte(esc2, x, y);
        //System.out.println();
        //System.out.println("Observamos el corte del primero: " + esc1.toString());
        //System.out.println("Observamos el corte del segundo: " + esc2.toString());

        int i = y + 1;
        int j = y + 1;
        while (i != x) {
            esta = contiene(auxPadre2.get(j), esc1);
            if (!esta) {
                esc1.set(i, auxPadre2.get(j));
                i++;
            }
            j++;
            if (j == tamTotal) {
                j = 0;
            }
            if (i == tamTotal) {
                i = 0;
            }
        }

        i = y + 1;
        j = y + 1;
        while (i != x) {
            esta = contiene(auxPadre1.get(j), esc2);
            if (!esta) {
                esc2.set(i, auxPadre1.get(j));
                i++;
            }
            j++;
            if (j == tamTotal) {
                j = 0;
            }
            if (i == tamTotal) {
                i = 0;
            }
        }
        elegidosOperadorSeleccion.clear();
        elegidosOperadorSeleccion.add(esc1); 
        elegidosOperadorSeleccion.add(esc2);
        //System.out.println("Hijo1: " + esc1.toString());
        //System.out.println("Hijo2: " + esc2.toString());
    }

    /**
     * @Brief Función para indicar las posiciones cortadas en un vector
     * @param list Lista Enlazada con el vector a cortar
     * @param x Entero con el punto inicial de corte
     * @param y Entero con el punto final de corte
     */
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
            corte1.add(auxPadre1.get(i));
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
        elegidosOperadorSeleccion.clear();
        elegidosOperadorSeleccion.add(hijo1);
        elegidosOperadorSeleccion.add(hijo2);
        //System.out.println("Primer hijo: " + hijo1.toString());
        //System.out.println("Segundo hijo: " + hijo2.toString());
    }

    /**
     * @brief Método auxiliar para encontrar la posicion de un elemento
     * @param list Lista en la que se quiere buscar
     * @param elemento Elemento a buscar en la lista
     * @return Devuelve la posicion de un elemento
     */
    int posElemento(LinkedList<Integer> list, int elemento) {
        for (int i = 0; i < tamTotal; i++) {
            if (list.get(i) == elemento) {
                return i;
            }
        }
        return 0;
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
}
