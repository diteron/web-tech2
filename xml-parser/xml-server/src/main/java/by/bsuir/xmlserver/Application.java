package by.bsuir.xmlserver;

import by.bsuir.xmlserver.communication.ClientsManager;

public class Application {
    public static void main(String[] args) {
        ClientsManager clientsManager = new ClientsManager();
        clientsManager.startProcessing();
    }
}
