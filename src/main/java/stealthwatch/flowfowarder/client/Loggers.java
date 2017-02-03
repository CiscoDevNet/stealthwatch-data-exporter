package stealthwatch.flowfowarder.client;

import org.apache.log4j.Logger;

public interface Loggers {

    Logger system = Logger.getLogger("system");
    Logger message = Logger.getLogger("message");

}
