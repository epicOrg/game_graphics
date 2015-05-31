package game.net.communication;

import org.json.JSONObject;

import game.data.UserData;
import game.net.fieldsnames.EncryptFields;
import game.net.fieldsnames.ServicesFields;

/**
 * Created by Luca on 29/05/2015.
 */
public class EncryptInitializer {

    public void initConnection(){

        RequestMaker requestMaker = UserData.DATA.getRequestMaker();

        JSONObject request = requestMaker.getNewRequest(new JSONd(ServicesFields.SERVICE, ServicesFields.ENCRYPT), new JSONd(ServicesFields.SERVICE_TYPE, EncryptFields.PUBLIC_KEY_REQUEST));


        try {
            ServerCommunicationThread.getInstance().send(request);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
