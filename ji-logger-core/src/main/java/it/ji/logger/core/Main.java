package it.ji.logger.core;


public class Main {
    public static void main(String[] args) {

        System.out.println("JI Logger Core");
        //OK
        Logger.info("OK Test info")
                .toConsole()
                .log();
    }
}