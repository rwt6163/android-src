package gukbab1216.com.youthwelfare129.data;

import android.content.Context;
import android.content.res.TypedArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import gukbab1216.com.youthwelfare129.R;
import gukbab1216.com.youthwelfare129.model.Welfare;
import gukbab1216.com.youthwelfare129.utils.Tools;

@SuppressWarnings("ResourceType")
public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }


    /**
     * Generate dummy data welfare ranking list
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Welfare> getWelfareData(Context ctx) {
        List<Welfare> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);

        for (int i = 0; i < drw_arr.length(); i++) {
            Welfare obj = new Welfare();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.title = name_arr[i];
            obj.explanation = Tools.getEmailFromName(obj.title);
            obj.imageDrw = ctx.getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
