package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.CustomCommentAdapter;
import anuranbarman.com.event.R;
import models.CommentDataModel;
import utils.SharedPrefManager;

/**
 * Created by Anuran on 5/21/2017.
 */

public class SingleEvent extends AppCompatActivity {
    TextView eventName,eventDesc,eventHost;
    Button btnLike,btnSignup,btnComment;
    EditText commentET;
    VideoView videoView;
    String event_id;
    ListView commentList;
    CustomCommentAdapter adapter;
    List<CommentDataModel> data=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event);
        final Bundle bundle=getIntent().getExtras();
        eventName=(TextView)findViewById(R.id.eventName);
        eventDesc=(TextView)findViewById(R.id.eventDesc);
        eventHost=(TextView)findViewById(R.id.hostName);
        btnLike=(Button)findViewById(R.id.btnLike);
        btnComment=(Button)findViewById(R.id.btnComment);
        btnSignup=(Button)findViewById(R.id.btnSignUp);
        commentET=(EditText)findViewById(R.id.commentET);
        commentList=(ListView)findViewById(R.id.comentList);
        videoView=(VideoView)findViewById(R.id.eventVideo);
        event_id=bundle.getString("event_id");
        eventName.setText(bundle.getString("event_name"));
        eventDesc.setText(bundle.getString("event_desc"));
        eventHost.setText("hosted by "+bundle.getString("event_host")+" in "+bundle.getString("event_category"));

        adapter=new CustomCommentAdapter(data,this);
        commentList.setAdapter(adapter);


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentET.getText().equals("")){
                    Toast.makeText(SingleEvent.this,"You must write your comment before posting",Toast.LENGTH_LONG).show();
                    return;
                }
                comment(event_id,new SharedPrefManager(SingleEvent.this).getID(),commentET.getText().toString());
            }
        });


        MediaController media=new MediaController(SingleEvent.this);
        media.setAnchorView(videoView);
        videoView.setMediaController(media);
        videoView.setVideoURI(Uri.parse("http://www.anuranbarman.com/zersey_event/uploads/"+bundle.getString("event_video")));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(!bundle.getString("event_video").equals("null"))
                    videoView.start();
            }
        });



        if(bundle.getString("event_like").equals("1")){
            btnLike.setEnabled(false);
            btnLike.setBackgroundResource(R.drawable.red_heart);
        }else{
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like(event_id,new SharedPrefManager(SingleEvent.this).getID());
                }
            });
        }
        if(bundle.getString("event_signup").equals("1")){
            btnSignup.setEnabled(false);
            btnSignup.setBackgroundColor(Color.GRAY);
            btnSignup.setText("Signed Up");
        }else{
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp(event_id,new SharedPrefManager(SingleEvent.this).getID());
                }
            });
        }

        getComments(event_id);
    }

    private void getComments(final String event_id) {
        RequestQueue r=Volley.newRequestQueue(SingleEvent.this);
        StringRequest sr=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/getAllComments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject j=jsonArray.optJSONObject(i);
                        String id,commenter,comment;
                        id=j.optString("id");
                        commenter=j.optString("name");
                        comment=j.optString("comment");
                        CommentDataModel c=new CommentDataModel(id,commenter,comment);
                        data.add(c);
                    }

                    adapter.notifyDataSetChanged();
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
                map.put("event_id",event_id);
                return map;
            }
        };
        r.add(sr);
    }

    private void comment(final String event_id, final String user_id, final String comment) {
        RequestQueue r= Volley.newRequestQueue(SingleEvent.this);
        StringRequest s=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/comment.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json=new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        Toast.makeText(SingleEvent.this,"You have successfully commented on the event",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(SingleEvent.this,"Something went wrong while commenting.Please try again.",Toast.LENGTH_LONG).show();
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
                map.put("user_id",user_id);
                map.put("event_id",event_id);
                map.put("comment",comment);
                return map;
            }
        };
        r.add(s);
    }


    private void like(final String id, final String id1) {
        RequestQueue r= Volley.newRequestQueue(SingleEvent.this);
        StringRequest s=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/like.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json=new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        Toast.makeText(SingleEvent.this,"You have successfully liked the event",Toast.LENGTH_LONG).show();
                        btnLike.setBackgroundResource(R.drawable.red_heart);
                        btnLike.setEnabled(false);
                    }else{
                        Toast.makeText(SingleEvent.this,"Something went wrong while liking the event.Please try again.",Toast.LENGTH_LONG).show();
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

    private void signUp(final String id, final String id1) {
        RequestQueue r= Volley.newRequestQueue(SingleEvent.this);
        StringRequest s=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/signUp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json=new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        Toast.makeText(SingleEvent.this,"You have successfully signed up for the event",Toast.LENGTH_LONG).show();
                        btnSignup.setEnabled(false);
                        btnSignup.setBackgroundColor(Color.GRAY);
                        btnSignup.setText("Signed Up");
                    }else{
                        Toast.makeText(SingleEvent.this,"Something went wrong while signing up.Please try again.",Toast.LENGTH_LONG).show();
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
