package gurpreetsk.me.selfiegeek;

import android.app.Application;

import com.kinvey.android.Client;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class KinveyApplication extends Application {

    private Client mKinveyClient = new Client.Builder(getApplicationContext()).build();

    public Client getmKinveyClient() {
        return mKinveyClient;
    }

}
