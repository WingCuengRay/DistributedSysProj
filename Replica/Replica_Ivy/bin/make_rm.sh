#/bin/bash

./killBg.sh Replica
./killBg.sh Room
javac -g -cp . ./RoomResrvSys/*.java -d ../bin/
javac -g -cp . ./tools/*.java -d ../bin/
cd ../bin
rm -f *.log
java tools.ReplicaManager Replica_3 &
cd ../src
