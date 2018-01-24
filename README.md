Data Exporter Client
====================

This project is the Java reference implementation of a Data Exporter Client.

The Flow Forwarder Docker Container on the Flow Collector listens to Flow
events from the Engine via ZeroMQ. It sends stitched, de-duplicated flow records 
over a web socket to registered clients.

The flow records are sent as a java.io.ByteBuffer containing protobuf representations
of external flows.

Dependencies
------------

* Java 8 JDK
* [Java Cryptography Extension](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
* Maven 3.3.9
* Protobuf Lib

This project requires the Stealthwatch `lib/protos-2.3.0.jar` to parse the external
flow records.  Future versions of the Data Exporter client as new fields are available.

Setup Instructions
------------------

Build the Data Exporter Application

    mvn clean package

The Flow Collector requires clients to connect over a secure web socket connection.
This means you need to:
 
1. Create Data Exporter keys.
2. Create a Certificate Authority
3. Sign the Data Exporter public key by the Certificate Authority.
4. Create a Trust Store with the Flow Collector's certificate.
5. Provision the Flow Collector with the Certificate Authority's certificate.

The `bin/generate-certs-and-keystore` script accomplishes steps 1-4.  All created certs
appear in the `./certs` directory.

    
Import `certs/data-exporter-certificate-authority.crt` into the Flow Collector via the 
`Configuration -> Certificate Authority Certificates` menu.

Running the application
-----------------------

The `bin/log-flows` script runs the application using the certificates created above.
It requires the Flow Collector host name or IP address:

    bin/log-flows 10.0.37.14

Once the Data Exporter is running the `./log` directory is created, and two log files are created:
    * `system.log` holds operational and error messages.
    * `messages.log` holds all flow record messages from the flow collectors(s).
