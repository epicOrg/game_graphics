package game.net.communication;

import org.json.JSONObject;

import game.data.UserData;
import game.net.connection_encryption.ConnectionEncrypter;
import game.net.connection_encryption.SymmetricKeyGenerator;
import game.net.fieldsnames.EncryptFields;
import game.net.fieldsnames.ServicesFields;

/**
 * @author Micieli
 * @date 2015/05/29
 */

public class EncryptInitializer {

    public void initConnection() {

        //ConnectionEncrypter.init(new SymmetricKeyGenerator());

        RequestMaker requestMaker = UserData.DATA.getRequestMaker();

        JSONObject request = requestMaker.getNewRequest(new JSONd(ServicesFields.SERVICE, ServicesFields.ENCRYPT),
                new JSONd(ServicesFields.SERVICE_TYPE, EncryptFields.PUBLIC_KEY_REQUEST));

        try {
            ServerCommunicationThread.getInstance().send(request);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
