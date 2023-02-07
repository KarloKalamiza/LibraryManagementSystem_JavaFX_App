package projekt.projekt.thread;

import projekt.projekt.Controllers.MainScreenUserController;
import projekt.projekt.HelloController;
import projekt.projekt.clientModels.ClientModel;
import projekt.projekt.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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


            //System.out.println("Player 1 thread started");

            PrintWriter oos = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner ois = new Scanner(clientSocket.getInputStream());
//            oos.writeObject(new ClientModel(clientSocket.getLocalPort(),
//                    clientSocket.getLocalAddress().getHostName(),
//                    HelloController.playerUsername));

            System.out.println("PlayerData Object sent to server");

           // ClientModel currentApplicationMetaData = (ClientModel) ois.readObject();

           // System.out.println("Player ONE port:: " + currentApplicationMetaData.getPort());

            String returnMessage = null;

            while (ois.hasNext()) {
                returnMessage = ois.next();

                //System.out.println("Waiting for data...");

                if (returnMessage.startsWith("Check")) {
                   // System.out.println("Provjera");
                    oos.println("Loan=" + Server.IS_LOANED);

                    if (Server.IS_LOANED) {
                        System.out.println("Knjiga je posudena");

                    }
                }
                else if (returnMessage.startsWith("Refresh")) {
                    System.out.println("Osvjezavanje tablice s knjigama");
                    controller.refreshTable();
                    System.out.println("Ispis");
                }else if(returnMessage.startsWith("Update")){
                    System.out.println("Postavljenje IS_LOANED na false");
                    Server.IS_LOANED = false;
                }


            //System.out.println("Sending message to server");
            //  oos.writeObject("Loan="+ Server.IS_LOANED);


        }

            System.out.println("While loop finished");
    } catch(IOException e)

    {
        e.printStackTrace();
    }
}
}
