package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import anuranbarman.com.event.R;
import models.CommentDataModel;

/**
 * Created by Anuran on 5/21/2017.
 */

public class CustomCommentAdapter extends BaseAdapter {
    List<CommentDataModel> myData;
    Context context;

    public CustomCommentAdapter(List<CommentDataModel> myData, Context context) {
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
        View view=inflater.inflate(R.layout.comment_single_row,parent,false);
        TextView name,comment;
        name=(TextView)view.findViewById(R.id.commenter);
        comment=(TextView)view.findViewById(R.id.comment);
        name.setText(myData.get(position).getName());
        comment.setText(myData.get(position).getComment());
        return view;
    }
}
