package org.gtq.vhubs.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

/**
 * Created by guo on 2016/5/15.
 */
public class SystemUtil {
    public static int getStatuBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
