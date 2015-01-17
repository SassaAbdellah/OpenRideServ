#!/bin/sh
#
#
#
export CLASSPATH="${CLASSPATH}:../OpenRideServer-ejb/build/"
#
#
for i in $(ls ../OpenRideServer-ejb/libs/*.jar)
do
 export "CLASSPATH=${i}:${CLASSPATH}"
done
#
# 
java   de.fhg.fokus.openride.routing.graphhopper.routing.GraphHopperRoutingGraphCreator
