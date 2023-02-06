/*
package projekt.projekt.thread;

import projekt.projekt.ApplicationData.ApplicationMetaData;
import projekt.projekt.clientModels.ClientModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread implements Runnable{

    private ClientModel cliendModel;

    public ClientThread(ClientModel cliendModel){

        this.cliendModel = cliendModel;
    }

    @Override
    public void run() {
        System.out.println("Client thread in run method");

        try (Socket serverSocket = new Socket("localhost", 59001)) {
            System.out.println("Client 1 Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                System.out.println("Client thread in while loop");

                Socket clientSocket = serverSocket.accept();
                System.err.println("GameServer connected from PORT: " + clientSocket.getPort());

                try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){
                    ClientModel applicationMetaData = (ClientModel) ois.readObject();

                    System.out.println("Metadata received! " + applicationMetaData);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
*/
