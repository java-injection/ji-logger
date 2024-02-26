package it.ji.logger.core;

import it.ji.logger.annotations.LoggerCore;
import it.ji.logger.core.base.DebugLoggerComponent;

import java.util.HashSet;
import java.util.Set;

@LoggerCore
public class Logger {
    private static boolean _verbose = true;

    private Level level;
    private String message;
    private Set<LoggerComponent> logDestinations;
    private boolean alwaysLog = false;


    private Logger(Level level, String message) {
        System.out.println("<<<<<<<<<<<<<<< costruttore di logger >>>>>>>>>>>>>>>");
        this.level = level;
        this.message = message;
        this.logDestinations = new HashSet<>();
    }



    public static boolean isVerbose() {
        return _verbose;
    }

    public static void setVerbose(boolean verbose) {
        _verbose = verbose;
    }

    public static LoggerBuilder info(String message) {
        return new LoggerBuilder(Level.INFO, message);
    }

    public static LoggerBuilder debug(String message) {
        return new LoggerBuilder(Level.DEBUG, message);
    }

    public static LoggerBuilder warn(String message) {
        return new LoggerBuilder(Level.WARN, message);
    }

    public static LoggerBuilder error(String message) {
        return new LoggerBuilder(Level.ERROR, message);
    }

    public static LoggerBuilder fatal(String message) {
        return new LoggerBuilder(Level.FATAL, message);
    }

    public static class LoggerBuilder {
        private Level level;
        private String message;
        private Logger logger;
        private boolean complete;
        private boolean alwaysLog = false;


        private static LoggerComponent consoleLogger = new DebugLoggerComponent("Console");
        private static LoggerComponent fileLogger = new DebugLoggerComponent("File");
        private static LoggerComponent databaseLogger = new DebugLoggerComponent("Database");

        public static void setConsoleLogger(LoggerComponent consoleLogger) {
            System.out.println(" WARNING <<<<<<<<<<<<<<< setConsoleLogger >>>>>>>>>>>>>>>");
            LoggerBuilder.consoleLogger = consoleLogger;
        }

        public static void setFileLogger(LoggerComponent fileLogger) {
            LoggerBuilder.fileLogger = fileLogger;
        }

        public static void setDatabaseLogger(LoggerComponent databaseLogger) {
            LoggerBuilder.databaseLogger = databaseLogger;
        }


        public LoggerBuilder() {
        }

        private LoggerBuilder(Level level, String message) {
            this.level = level;
            this.message = message;
            this.logger = new Logger(level, message);
        }

        public LoggerBuilder toConsole() {
            if (!logger.logDestinations.add(LoggerBuilder.consoleLogger)) {
                throw new IllegalArgumentException("ConsoleLogger already added.");
            }
            return this;
        }

        public LoggerBuilder toFile() {
            if (!logger.logDestinations.add(LoggerBuilder.fileLogger)) {
                throw new IllegalArgumentException("FileLogger already added.");
            }
            return this;
        }

        public LoggerBuilder ignoreVerbose() {
            alwaysLog = true;
            return this;
        }

        public LoggerBuilder onVerbose() {
            alwaysLog = false;
            return this;
        }

        public LoggerBuilder toDatabase() {
            if (!logger.logDestinations.add(LoggerBuilder.databaseLogger)) {
                throw new IllegalArgumentException("DatabaseLogger already added.");
            }
            return this;
        }

        public LoggerBuilder broadcastTo(LoggerComponent... destinations) {
            for (LoggerComponent destination : destinations) {
                if (!logger.logDestinations.add(destination)) {
                    throw new IllegalArgumentException("Destination already added.");
                }
            }
            return this;
        }

        public LoggerBuilder broadcast() {
            return broadcastTo(LoggerBuilder.consoleLogger, LoggerBuilder.fileLogger, LoggerBuilder.databaseLogger);
        }

        public void log() {
            if (complete) {
                throw new IllegalStateException("Log already completed.");
            }
            if(logger.logDestinations.isEmpty()) {
                throw new IllegalStateException("No log destinations set.");
            }
            if(_verbose || alwaysLog) {
                logger.logDestinations.forEach(
                        destination -> destination.log(logger.level, logger.message)
                );
            }
            complete = true;
            logger.logDestinations.clear();
        }
    }

    public static void main(String[] args) {
        Logger.setVerbose(true);

        Logger.info("Hello world!")
                .broadcastTo(new DebugLoggerComponent("Console"), new DebugLoggerComponent("File"))
                .log();


    }
}
