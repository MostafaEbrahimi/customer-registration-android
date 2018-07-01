package ir.meandme.customerregistration;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import io.objectbox.BoxStore;


/**
 * Created by Mostafa on 10/28/2017. :)
 */

public class MainApplication extends Application {
    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(MainApplication.this).build();
        Hawk.init(getApplicationContext()).build();

    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
