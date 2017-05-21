package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.Events;
import activities.SingleEvent;
import anuranbarman.com.event.MainActivity;
import anuranbarman.com.event.R;
import models.EventDataModel;
import utils.SharedPrefManager;

/**
 * Created by Anuran on 5/19/2017.
 */

public class CustomEventListAdapter extends BaseAdapter {
    List<EventDataModel> myData;
    Context context;

    public CustomEventListAdapter(List<EventDataModel> myData, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.event_list_single_row,parent,false);
        TextView eventName=(TextView)view.findViewById(R.id.eventName);
        TextView eventhost=(TextView)view.findViewById(R.id.hostName);
        final VideoView video=(VideoView)view.findViewById(R.id.eventVideo);
        Button btnSignUp=(Button)view.findViewById(R.id.btnSignUp);
        Button btnLiked=(Button)view.findViewById(R.id.btnLike);
        if(myData.get(position).isSignedUpOrNot().equals("1")){
            btnSignUp.setText("Already Signed Up");
            btnSignUp.setEnabled(false);
        }else{
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp(myData.get(position).getId(),new SharedPrefManager(context).getID());
                }
            });
        }
        if(myData.get(position).getLiked().equals("1")){
            btnLiked.setBackgroundResource(R.drawable.red_heart);
            btnLiked.setEnabled(false);
        }else{
            btnLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like(myData.get(position).getId(),new SharedPrefManager(context).getID());
                }
            });
        }
        eventName.setText(myData.get(position).getTitle());
        eventhost.setText("hosted by "+myData.get(position).getHostID()+" in "+myData.get(position).getCategory());
        MediaController media=new MediaController(context);
        media.setAnchorView(video);
        video.setMediaController(media);
        video.setVideoURI(Uri.parse("http://www.anuranbarman.com/zersey_event/uploads/"+myData.get(position).getFilename()));
        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(!myData.get(position).getFilename().equals("null") || myData.get(position).getFilename() !=null)
                    video.start();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("event_name",myData.get(position).getTitle());
                bundle.putString("event_video",myData.get(position).getFilename());
                bundle.putString("event_host",myData.get(position).getHostID());
                bundle.putString("event_category",myData.get(position).getCategory());
                bundle.putString("event_id",myData.get(position).getId());
                bundle.putString("event_desc",myData.get(position).getDescription());
                bundle.putString("event_like",myData.get(position).getLiked());
                bundle.putString("event_signup",myData.get(position).isSignedUpOrNot());
                Intent intent=new Intent(context, SingleEvent.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

    private void signUp(final String id, final String id1) {
        RequestQueue r= Volley.newRequestQueue(context);
        StringRequest s=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/signUp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json=new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        Toast.makeText(context,"You have successfully signed up for the event",Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager=((MainActivity)context).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame,new Events()).commit();
                    }else{
                        Toast.makeText(context,"Something went wrong while signing up.Please try again.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("user_id",id1);
                map.put("event_id",id);
                return map;
            }
        };
        r.add(s);
    }

    private void like(final String id, final String id1) {
        RequestQueue r= Volley.newRequestQueue(context);
        StringRequest s=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/like.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json=new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        Toast.makeText(context,"You have successfully liked the event",Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager=((MainActivity)context).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame,new Events()).commit();
                    }else{
                        Toast.makeText(context,"Something went wrong while liking the event.Please try again.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("user_id",id1);
                map.put("event_id",id);
                return map;
            }
        };
        r.add(s);
    }
}
