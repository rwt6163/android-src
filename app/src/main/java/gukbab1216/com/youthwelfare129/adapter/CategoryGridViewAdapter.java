package gukbab1216.com.youthwelfare129.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gukbab1216.com.youthwelfare129.R;


public class CategoryGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewInt;

    public CategoryGridViewAdapter(Context mContext, String[] gridViewString, int[] gridViewImage) {
        this.mContext = mContext;
        this.gridViewString = gridViewString;
        this.gridViewInt = gridViewImage;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridViewAndroid = inflater.inflate(R.layout.item_category_gridview, null);

            TextView textViewAndroid = gridViewAndroid.findViewById(R.id.category_gridView_text);
            ImageView imageViewAndroid = gridViewAndroid.findViewById(R.id.category_gridView_image);

            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewInt[i]);

        } else {
            gridViewAndroid = convertView;
        }

        return gridViewAndroid;

    }
}
