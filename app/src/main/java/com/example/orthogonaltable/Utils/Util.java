package com.example.orthogonaltable.Utils;

import android.content.Context;


public class Util {

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density);
    }

}
