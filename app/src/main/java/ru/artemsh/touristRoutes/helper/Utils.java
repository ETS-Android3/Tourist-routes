package ru.artemsh.touristRoutes.helper;

import android.content.res.Resources;

class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

