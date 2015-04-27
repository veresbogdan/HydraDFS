package hydradfs.utils;

import org.slf4j.Logger;

public class Util {

    public static void logException(Logger logger, Exception e){
        logger.debug(e.getMessage());

        StackTraceElement[] stackTrace = e.getStackTrace();

        for (StackTraceElement aStackTrace : stackTrace) {
            logger.debug(aStackTrace.toString());
        }
    }
}
