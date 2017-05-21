package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import anuranbarman.com.event.R;

/**
 * Created by Anuran on 5/19/2017.
 */

public class SpinnerCustomAdapter extends BaseAdapter {
    List<String> myData;
    Context context;

    public SpinnerCustomAdapter(List<String> myData, Context context) {
        this.myData = myData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.cat_spinner_single_item,parent,false);
        TextView name=(TextView)view.findViewById(R.id.catName);
        name.setText(myData.get(position));
        return view;
    }
}
