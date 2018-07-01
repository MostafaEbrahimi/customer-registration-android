package ir.meandme.persianviews;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

/**
 * Created by Mostafa on 7/20/2017. :)
 */

public class ConfigAndURLs {

    public static Typeface getFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/Shabnam-FD.ttf");
    }

    public static void showToast(Context context, String data) {
        MyToast myToast = new MyToast(context, data, 1);
        myToast.setDuration(Toast.LENGTH_LONG);
        myToast.show();
    }
}
