package projekt.projekt.thread;

import projekt.projekt.Controllers.MainScreenUserController;
import projekt.projekt.HelloController;
import projekt.projekt.server.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private MainScreenUserController controller;

    public ClientThread(MainScreenUserController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try (Socket clientSocket = new Socket(HelloController.HOST, HelloController.PORT)) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            System.out.println("Player 1 thread is starting");

            PrintWriter oos = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner ois = new Scanner(clientSocket.getInputStream());

            System.out.println("PlayerData Object sent to server");

            String returnMessage = null;

            while (ois.hasNext()) {
                returnMessage = ois.next();

                if (returnMessage.startsWith("Check")) {
                    oos.println("Loan=" + Server.IS_LOANED);

                    if (Server.IS_LOANED) {
                        System.out.println("Knjiga je posudena");
                    }
                } else if (returnMessage.startsWith("Refresh")) {
                    System.out.println("Osvjezavanje tablice s knjigama");
                    controller.refreshTable();
                    System.out.println("Table refreshed");
                } else if (returnMessage.startsWith("Update")) {
                    System.out.println("Postavljenje IS_LOANED na false");
                    Server.IS_LOANED = false;
                }
            }

            System.out.println("While loop finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
