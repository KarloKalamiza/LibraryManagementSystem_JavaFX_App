package projekt.projekt.ApplicationData;

import java.io.Serializable;

public class ApplicationMetaData implements Serializable {
    private ClientData clientOneData;
    private ClientData clientTwoData;

    public ApplicationMetaData() {
    }

    public ApplicationMetaData(ClientData clientOneData, ClientData clientTwoData) {
        this.clientOneData = clientOneData;
        this.clientTwoData = clientTwoData;
    }

    public ClientData getClientOneData() {
        return clientOneData;
    }

    public void setClientOneData(ClientData clientOneData) {
        this.clientOneData = clientOneData;
    }

    public ClientData getClientTwoData() {
        return clientTwoData;
    }

    public void setClientTwoData(ClientData clientTwoData) {
        this.clientTwoData = clientTwoData;
    }
}
