package com.laurum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public final class Utilities {

    public enum ToastType {ERROR, INFO, WARNING}

    public static void sendToast(Context context, Integer msg, ToastType type){
        Toast toast = Toast.makeText(context,context.getString(msg), Toast.LENGTH_SHORT);
        View view = toast.getView();

        switch (type){
            case ERROR:
                //view.setBackgroundResource(R.drawable.splash_image);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#191919"));
                break;
            default:
                break;
        }
        toast.show();
    }
}
