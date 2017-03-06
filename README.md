Data Exporter Client
====================

This project is a Java reference implementation of a Data Exporter Client.

The Flow Forwarder Docker Container on the Flow Collector listens to Flow
events from the Engine via ZeroMQ. It sends stitched, de-duplicated flow records 
over a web socket to registered clients.

The flow records are sent as a java.io.ByteBuffer containing protobuf representations
of external flows.

Dependencies
------------

* Java 8 JDK
* Maven 3.3.9
* Protobuf Lib

(This project requires the Stealthwatch `lib/protos-2.3.0.jar` to parse the external
flow records.  Future versions Stealthwatch will update as new fields are available.)

Setup Instructions
------------------

Build project:
    $ mvn clean package

Client Certificate:
    Server requires each client to connect over secure connection with use of client side certificates.
    In order to generate client side certificate user should do next steps:

    Generate client side certificate
        a. openssl genrsa -des3 -out ca.key 4096 (pw.ca as default pass)
        b. openssl req -new -x509 -days 365 -key ca.key -out ca.crt
        c. openssl genrsa -des3 -out client.key 4096 (pw.client as default pass)
        d. openssl req -new -key client.key -out client.csr
        e. openssl x509 -req -days 365 -in client.csr -CA ca.crt -CAkey ca.key -set_serial 01 -out client.crt
        f. openssl pkcs12 -export -clcerts -in client.crt -inkey client.key -out client.p12 (pw.client-export as default pass)

    Once such certificate is created(make sure you remember all passphrases used to create keys),
    you need to do few additional steps to make everything work correctly:
        a. Import server side certificate into local client`s truststore:
            keytool -import -alias server -file /path/to/server.crt -keystore truststore.p12 -storetype pkcs12 (pw.trust as default pass)
        b. Import CA certificate (ca.crt which was created in previous step) onto server:
            Configuration -> Certificate Authority Certificates - upload

    More information could be found here - https://stash0.lancope.ciscolabs.com/projects/IRIS/repos/flow-forwarder/browse/client
    Or alternatively user can generate same certificates with use of scripts provided here - https://stash0.lancope.ciscolabs.com/projects/NERF/repos/ise-setup/browse
    After all these step are done we are good to go and run client.

Run:
    Simple run command looks like this:

    $ java -jar target/flow-forwarder-client-0.1.0-jar-with-dependencies.jar flow-collector-ips-separated-by-space
    OR
    $ cd bin && ./write-flows flow-collector-ips-separated-by-space

    In such case client will search for certificates in ssl folder( ssl/client.p12 and ssl/truststore.p12) and use default
    passphrases to load them.

    In case when user used non default passphrases for certificates, they could be passed to client through command line
    params:
    $ java -jar target/flow-forwarder-client-0.1.0-jar-with-dependencies.jar -ksp keystorepassword -tsp truststorepassword flow-collector-ip...

    Also there is a possibility to provide custom certificate and truststore location through -ks and -ts params.

    Once client is running the `log` directory will be created, it will contain two log files:
    * `system.log` holds operational and error messages.
    * `messages.log` holds all flow record messages from the flow collectors(s).