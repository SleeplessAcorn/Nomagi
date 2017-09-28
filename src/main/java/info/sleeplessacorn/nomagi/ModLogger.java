package info.sleeplessacorn.nomagi;

import org.apache.logging.log4j.LogManager;

public final class ModLogger {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Nomagi.NAME);

    public static void info(boolean global, String msg, Object... vars) {
        if (global || Nomagi.isDevEnv()) LOGGER.info(msg, vars);
    }

    public static void warn(boolean global, String msg, Object... vars) {
        if (global || Nomagi.isDevEnv()) LOGGER.warn(msg, vars);
    }

    public static void error(boolean global, String msg, Object... vars) {
        if (global || Nomagi.isDevEnv()) LOGGER.error(msg, vars);
    }

    public static void debug(String msg, Object... vars) {
        if (Nomagi.isDevEnv()) {
            LOGGER.info("[DEBUG] " + msg, vars);
        }
    }

}
