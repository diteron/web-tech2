package by.bsuir.xmlparser.server;

import by.bsuir.xmlparser.server.communication.ClientsProcessor;

public class Application {
    public static void main(String[] args) {
        ClientsProcessor clientsManager = new ClientsProcessor();
        clientsManager.start();
    }
}
