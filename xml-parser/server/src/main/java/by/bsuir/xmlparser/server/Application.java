package by.bsuir.xmlparser.server;

import by.bsuir.xmlparser.server.communication.ClientsManager;

public class Application {
    public static void main(String[] args) {
        ClientsManager clientsManager = new ClientsManager();
        clientsManager.startProcessing();
    }
}
