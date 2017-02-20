package com.dislab.leocai.spacesyncandroidui.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leocai on 17-2-16.
 */
public class MyDataUtils {
    public static String getTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_HH_mm_ss");
        return sdf.format(new Date());
    }
}
