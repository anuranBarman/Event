package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import anuranbarman.com.event.MainActivity;
import anuranbarman.com.event.R;
import utils.SharedPrefManager;

/**
 * Created by Anuran on 5/18/2017.
 */

public class Login extends AppCompatActivity {
    EditText emailET,passET;
    TextView regLinkTV;
    Button btnLogin,btnCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        emailET=(EditText)findViewById(R.id.emailET);
        passET=(EditText)findViewById(R.id.passET);
        regLinkTV=(TextView)findViewById(R.id.regLinkTV);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        regLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailET.getText().equals("")||passET.getText().equals("")){
                    Toast.makeText(Login.this,"You must fill up the fields to continue",Toast.LENGTH_LONG).show();
                    return;
                }
                loginCheck(emailET.getText().toString().trim(),passET.getText().toString().trim());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(new SharedPrefManager(Login.this).getLogin()){
            Intent intent=new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void loginCheck(final String email, final String password) {
        final ProgressDialog p=new ProgressDialog(Login.this);
        p.setMessage("We are trying to log you in......");
        p.setTitle("Please wait");
        p.show();
        RequestQueue requestQueue=Volley.newRequestQueue(Login.this);
        StringRequest stringReq=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json= null;
                try {
                    json = new JSONObject(response);
                    int success=json.optInt("success");
                    if(success==1){
                        JSONObject jsonObject=json.optJSONObject("0");
                        String id=jsonObject.optString("id");
                        String name=jsonObject.optString("name");
                        String email=jsonObject.optString("email");
                        new SharedPrefManager(Login.this).setID(id);
                        new SharedPrefManager(Login.this).setName(name);
                        new SharedPrefManager(Login.this).setEmail(email);
                        new SharedPrefManager(Login.this).setLogin(true);
                        p.dismiss();
                        Intent intent=new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        p.dismiss();
                        Toast.makeText(Login.this,"Wrong credentials.Please provide the right ones",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    p.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };

        requestQueue.add(stringReq);
    }
}
