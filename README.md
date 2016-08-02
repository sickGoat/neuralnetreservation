# Introduzione
Il software puo essere lanciato attraverso lo script presente nella cartella
SCRIPT oppure attraverso l'interfaccia grafica presente nella cartella GUI

## Script Bash
Per lanciare lo script occorre specificare dei parametri, che sono

    * -d, --distributed [T|F]         serve a specificare se si vuole l'approccio
                                      distribuito, **default T**

    * -p, --propagation [Q|M|R|B]     serve a specificare il tipo di addestramento,
                                      **default R (Resilient)**

    * -cs, --cellside Double          serve a specificare la dimensione del lato della
                                      cella, **default 120**

    * -md, --mapdimension Double      serve a specificare la dimensione della mappa,
                                      ovvero la massima coordinata X o Y della mappa,si
                                      assume quindi che la porzione sia quadrata.

    * -H Int                          serve a specificare il numero di chiamate ricorsive
                                      dell'algoritmo, **default 7**

    * -at, --alghtres                 serve a specificare la soglia di predizione
                                      dell'algoritmo, **default 0.5.**

    * -tt, --trainthres               serve a specificare la soglia di errore per
                                      il training della rete neurale, **default 0,04**

    * -tf, --trainingfile             serve a specifiare il file col quale eseguire il
                                      training.

    * -sf, --simulationfile           serve a specificare il file col quale eseguire
                                      la simulazione.

I parametri -tf -sf -md sono obbligatori.

## GUI

Per eseguire la GUI occorre sccegliere il uno trai due jar sulla base dell'ambiente di esecuzione:

  1. _neuralnetworkwin.jar_ per ambienti windows
  2. _neuralnetwork.jar_ per ambienti \*nix

### Requirements

Per eseguire la GUI occorre aver installato sumo, la versione 0.25 sulla propria macchina.
Nel caso di ambienti windows occrre averlo installato in **C://sumo**.
Il software è scaricabile al link:
https://sourceforge.net/projects/sumo/files/sumo/version%200.25.0/

### File di training e simulazione
I file devono essere in formato tcl, generabili con il software [C4r]

[C4r]:http://www.grc.upv.es/Software/default.html
