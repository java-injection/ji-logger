package it.ji.logger.core;

@FunctionalInterface
public interface LoggerComponent {
    void log(Level level, String message);

}
