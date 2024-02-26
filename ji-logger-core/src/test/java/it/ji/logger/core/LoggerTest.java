package it.ji.logger.core;

import it.ji.logger.core.base.DebugLoggerComponent;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoggerTest {

    @Mock
    private DebugLoggerComponent mockLoggerComponent;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        //MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void info() throws Exception {
        LoggerComponent consoleLogger = mock(LoggerComponent.class);
        Logger.LoggerBuilder builder = Logger.info("Test message");
        Field loggerField = builder.getClass().getDeclaredField("logger");
        loggerField.setAccessible(true);
        Logger logger = (Logger) loggerField.get(builder);

        Field logDestinationsField = Logger.class.getDeclaredField("logDestinations");
        logDestinationsField.setAccessible(true);
        Set<LoggerComponent> logDestinations = (Set<LoggerComponent>) logDestinationsField.get(logger);

        logDestinations.add(consoleLogger);

        builder.log();

        verify(consoleLogger).log(Level.INFO, "Test message");
    }

    @Test
    public void testInfoOutput() {
        // Chiamare il metodo info() per eseguire la log normalmente
//        Logger.info("Test message").toConsole().log();
//
//        // Verificare che l'output della log corrisponda all'output previsto
//        String expectedOutput = "[Console] [INFO] Test message";
//        String actualOutput = outContent.toString().trim().replace("\r\n", "");
//
//        // Mostra l'output effettivo e l'output previsto nel caso il test fallisca
//        if (!expectedOutput.equals(actualOutput)) {
//            fail("Expected output: " + expectedOutput + "\nActual output: [" + actualOutput+ "]");
//        }
    }

    @Test
    void debug() {
    }

    @Test
    void warn() {
    }

    @Test
    void error() {
    }

    @Test
    void fatal() {
    }

    @Test
    void main() {
    }
}