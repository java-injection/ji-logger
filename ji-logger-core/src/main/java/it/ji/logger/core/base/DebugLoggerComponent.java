package it.ji.logger.core.base;

import it.ji.logger.core.Level;
import it.ji.logger.core.LoggerComponent;

public class DebugLoggerComponent implements LoggerComponent {

    private String destination;

    public DebugLoggerComponent(String destination) {
        this.destination = destination;
    }

    @Override
    public void log(Level level, String message) {
        System.out.println("[" + destination + "] [" + level + "] " + message);
    }
}
