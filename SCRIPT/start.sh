#!/bin/bash
# Script di avvio dell'applicazione, i parametri
# da specificare sono:
#  --distributed | -d = [T,F] default T
#  --propagation | -p = [Q,M,R,B] default R
#  --cellside | -cs = double default 250
#  --mapdimension | -md = int default 1300
#  -H = int default 7
#  --algthres | -at = double default 0.5
#  --trainthres | -tt = double default 0.04
#  --trainingfile | -tf = file
#  --simulationfile | -sf = file
REGEX_NUMBER='^[0-9]+([.][0-9]+)?$'
REGEX_DISTRIBUTED='[T|F]'
REGEX_PROPAGATION='[Q,M,R,B]'

while [[ $# > 1 ]]
do
key=$1
case $key in
    -d|--distributed)
    distributed=$2
    shift
    ;;
    -p|--propagation)
    propagation=$2
    shift
    ;;
    -cs|--cellside)
    cellside=$2
    shift
    ;;
    -md|--mapdimension)
    mapdimension=$2
    shift
    ;;
    -H)
    H=$2
    shift
    ;;
    -at|--algthres)
    at=$2
    shift
    ;;
    -tt|--trainthres)
    tt=$2
    shift
    ;;
    --trainingfile|-tf)
    tf=$2
    shift
    ;;
    --simulationfile|-sf)
    sf=$2
    shift
    ;;
    *)
    echo "Unknown option $key"
    echo "Parameter to specifie "
    ;;
esac
shift
done

#IMPOSTO VALORI DI DEFAULT SE NECESSARIO
#controllo se tutti i parametri sono stati settati correttamente
#controllo se sono stati specificati i file e la dimensione della mappa
if [ -z ${tf+x} ]; then echo "Specify training file"; exit -1; fi
if [ -z ${sf+x} ]; then echo "Specify simulation file"; exit -1; fi
if [ -z ${mapdimension+x} ];
then
  echo "Specify map dimension "; exit -1;
elif ! [[ $mapdimension =~ $REGEX_NUMBER ]]; then
    echo "Map dimension must be a number";
    exit -1;
fi

#controllo approccio
if [ -z ${distributed+x} ];
then
  echo "Approach not specified, default DISTRIBUTED"; distributed="T";
elif ! [[ $distributed =~ $REGEX_DISTRIBUTED ]]; then
  echo "Distributed parameter must be T or F";
  exit -1;
fi
#controllo propagation
if [ -z ${propagation+x} ];
  then echo "Propagation not specified, default RESILIENT"; propagation="R";
elif ! [[ $propagation =~ $REGEX_PROPAGATION ]]; then
  echo "Propagation parameter must be a value between B(backpropagation),R(resilient),Q(quick),M(manhattan)";
  exit -1;
fi
#controllo cellside
if [ -z ${cellside+x} ];
then
  echo "Cell side not specified, default 120" ;cellside=120;
elif ! [[ $cellside =~ $REGEX_NUMBER ]]; then
  echo "Cell side must be a number";
  exit -1;
fi
#controllo H
if [ -z ${H+x} ];
then
  echo "Visited cells not specified, default 8" ;H=7;
elif ! [[ $H =~ $REGEX_NUMBER ]]; then
  echo "Cell side must be a number";
  exit -1;
fi

#controllo alghtorim threshold
if [ -z ${at+x} ];
then
  echo "Alghorithm not specified, default 0.3" ;at=0.3;
elif ! [[ $at =~ $REGEX_NUMBER ]]; then
  echo "Alghorithm threshold must be a number";
  exit -1;
fi

#controllo training threshold
if [ -z ${tt+x} ];
then
  echo "Training Threshold not specified, default 0.04"; tt=0.04;
elif ! [[ $tt =~ $REGEX_NUMBER ]]; then
  echo "Training Threshold must be a number";
  exit -1;
fi



arrayArgs=()
arrayArgs+=($distributed)
arrayArgs+=($propagation)
arrayArgs+=($cellside)
arrayArgs+=($at)
arrayArgs+=($tt)
arrayArgs+=($mapdimension)
arrayArgs+=($tf)
arrayArgs+=($sf)
arrayArgs+=($H)

java -jar nn.jar ${arrayArgs[@]}
exit 0
