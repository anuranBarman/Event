package activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.SpinnerCustomAdapter;
import anuranbarman.com.event.R;
import models.EventDataModelDownload;
import utils.SharedPrefManager;
import utils.Upload;

/**
 * Created by Anuran on 5/18/2017.
 */

public class EventCreate extends AppCompatActivity {
    EditText titleET,descET;
    Spinner catSpinner;
    Button btnCreate;
    List<String> cats=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);
        titleET=(EditText)findViewById(R.id.titleET);
        descET=(EditText)findViewById(R.id.descET);
        catSpinner=(Spinner)findViewById(R.id.categorySpinner);

        cats.add("Category A");
        cats.add("Category B");
        cats.add("Category C");
        cats.add("Category D");
        cats.add("Category E");
        SpinnerCustomAdapter spinnerCustomAdapter=new SpinnerCustomAdapter(cats,this);
        catSpinner.setAdapter(spinnerCustomAdapter);

        /*// Checking Download Service
        String string=new SharedPrefManager(EventCreate.this).getDownloadEvent();
        Type type=new TypeToken<List<EventDataModelDownload>>(){}.getType();
        List<EventDataModelDownload> data=new Gson().fromJson(string,type);
        Toast.makeText(EventCreate.this, data.get(0).title, Toast.LENGTH_SHORT).show();*/



        btnCreate=(Button)findViewById(R.id.btnUpload);

        int permission= ActivityCompat.checkSelfPermission(EventCreate.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(EventCreate.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }



        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleET.getText().toString().equals("") || descET.getText().toString().equals(""))
                {
                    Toast.makeText(EventCreate.this,"You must provie Title and Description of the Event",Toast.LENGTH_LONG).show();
                    return;
                }
               createEvent(titleET.getText().toString(),descET.getText().toString(),catSpinner.getSelectedItem().toString(),new SharedPrefManager(EventCreate.this).getID());
            }
        });

    }

    private void createEvent(final String s, final String s1, final String selectedItem, final String s2) {
        final ProgressDialog p=new ProgressDialog(EventCreate.this);
        p.setTitle("Please wait");
        p.setMessage("We are creating the event for you.Please have patience.....");
        p.show();
        RequestQueue req= Volley.newRequestQueue(EventCreate.this);
        StringRequest stringReq=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/event_create.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json=new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        p.dismiss();
                        Intent intent=new Intent(EventCreate.this,UploadMedia.class);
                        startActivity(intent);
                        finish();
                    }else{
                        p.dismiss();
                        Toast.makeText(EventCreate.this,"Something went wrong while creating your event.Please try again",Toast.LENGTH_LONG).show();
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
                map.put("title",s);
                map.put("description",s1);
                map.put("category",selectedItem);
                map.put("hostID",s2);
                return map;
            }
        };
        req.add(stringReq);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length>0){
                if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                    finish();
                }
            }else{
                finish();
            }
        }
    }


}
