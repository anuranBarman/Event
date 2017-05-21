package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.EventDataModel;
import models.EventDataModelDownload;
import utils.SharedPrefManager;

/**
 * Created by Anuran on 5/21/2017.
 */

public class DownloadEvents extends Service {

    List<EventDataModelDownload> data=new ArrayList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getEvents();
        return START_STICKY;
    }

    private void getEvents() {
        RequestQueue r= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/getAllEvents.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo=jsonArray.optJSONObject(i);
                        String id=jo.optString("id");
                        String title=jo.optString("title");
                        String desc=jo.optString("description");
                        String cat=jo.optString("category");
                        String file_name=jo.optString("file_name");
                        String host_name=jo.optString("host_id");
                        EventDataModelDownload e=new EventDataModelDownload(id,title,desc,cat,file_name,host_name);
                        data.add(e);
                    }

                    Gson gson=new Gson();
                    String downloadString=gson.toJson(data);
                    new SharedPrefManager(DownloadEvents.this).setDownloadEvent(downloadString);

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
                map.put("user_id",new SharedPrefManager(DownloadEvents.this).getID());
                return map;
            }
        };
        r.add(sr);
    }
}
