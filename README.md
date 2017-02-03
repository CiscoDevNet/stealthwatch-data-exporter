Flow Fowarder Client
====================

This project is a Java reference implementation of a Flow Fowarder Client.

The Flow Fowarder Docker Container on the Flow Collector listens to Flow 
events from the Engine via ZeroMQ. It sends stitched, de-duplicated flow records 
over a web socket to registered clients.

The flow records are sent as a java.io.ByteBuffer containing protobuf representations
of external flows.

Dependencies
------------

* Java 8 JDK
* Maven 3.3.9

Setup Instructions
------------------
   
    $ mvn package
    $ cd target
    $ java -cp flow-fowarder-client-0.1.0-jar-with-dependencies.jar stealwatch.flowfowarder.client.Main flow-collector-ip...

The `log` directory will contain two log files:  

* `system.log` holds operational and error messages.
* `messages.log` holds all flow record messages from the flow collectors(s).    

Protobuf Lib
------------

This project requires the Stealthwatch `lib/protos-2.3.0.jar` to parse the external 
flow records.  Future versions Stealthwatch will update as new fields are available.  
 
