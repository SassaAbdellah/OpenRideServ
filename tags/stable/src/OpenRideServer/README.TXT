
Welcome to the jORide.

To build the project, proced as follows

* You must have apache ant in version 1.7.1 or newer.
  Ant can be obtained from ant.apache.org

* You must have a jdk 1.6.22 or better. 
  The jdk can be obtained from java.oracle.com

* Get a google maps api key for javascript maps api v3 

* copy sample file 'local.properties.sample'
  to 'local.properties'

* in local.properties, add the 'google.maps.key' property
  to hold Your Google Maps api key

* In OpenRideServer project, run 

  ant -f build.headless.xml 

  this will build an ear named "OpenRideServer.ear" in 
  the OpenRideServer/dist directory



