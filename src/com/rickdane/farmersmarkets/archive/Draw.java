package com.rickdane.farmersmarkets.archive;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Draw extends Activity {

    ViewContainer viewContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        viewContainer = new ViewContainer(this);
        setContentView(viewContainer);


        viewContainer.requestFocus();

    }


}
