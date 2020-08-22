package com.sclimin.sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sclimin.mykit.app.Activity;
import com.sclimin.mykit.app.Item;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private final ArrayList<Item> myItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecyclerView crc = findViewById(R.id.recycler_view);
//
//        for (int i = 0; i < 100; i++) {
//            if (i % 2 == 0) {
//                myItems.add(new MyItem());
//            }
//            else {
//                myItems.add(new MyItem2());
//            }
//        }
//
//        crc.setAdapter(new RecyclerAdapter(myItems));
    }

    @Override
    protected void onConfigurationWindow(Window window) {
        super.onConfigurationWindow(window);

        WindowManager.LayoutParams params = window.getAttributes();
        int flags = params.flags;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        params.flags = flags;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int suv = window.getDecorView().getSystemUiVisibility();
            suv |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(suv);
        }

        window.setAttributes(params);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
}
