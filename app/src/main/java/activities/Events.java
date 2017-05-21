package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import adapters.CustomEventListAdapter;
import anuranbarman.com.event.R;
import models.EventDataModel;
import utils.SharedPrefManager;

/**
 * Created by Anuran on 5/19/2017.
 */

public class Events extends Fragment {
    List<EventDataModel> data=new ArrayList<>();
    ListView eventList;
    CustomEventListAdapter customEventListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.events,container,false);
        eventList=(ListView)view.findViewById(R.id.eventList);
        customEventListAdapter=new CustomEventListAdapter(data,getActivity());
        eventList.setAdapter(customEventListAdapter);
        getEvents();
        return view;
    }

    private void getEvents() {
        RequestQueue r= Volley.newRequestQueue(getActivity());
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
                        String signedUpOrNot=jo.optString("signed_up");
                        String liked=jo.optString("liked");
                        EventDataModel e=new EventDataModel(id,title,desc,cat,file_name,host_name,signedUpOrNot,liked);
                        data.add(e);
                    }
                    customEventListAdapter.notifyDataSetChanged();
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
                map.put("user_id",new SharedPrefManager(getActivity()).getID());
                return map;
            }
        };
        r.add(sr);
    }


}
