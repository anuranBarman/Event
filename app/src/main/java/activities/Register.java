package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import anuranbarman.com.event.R;

/**
 * Created by Anuran on 5/18/2017.
 */

public class Register extends AppCompatActivity {
    EditText name,email,password;
    Button btnRegister,btnCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name=(EditText)findViewById(R.id.nameET);
        email=(EditText)findViewById(R.id.emailET);
        password=(EditText)findViewById(R.id.passET);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnCancel=(Button)findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(name.getText().toString(),email.getText().toString(),password.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void register(final String s, final String s1, final String s2) {
        RequestQueue r= Volley.newRequestQueue(Register.this);
        StringRequest sr=new StringRequest(Request.Method.POST, "http://anuranbarman.com/zersey_event/register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j=new JSONObject(response);
                    int success=j.optInt("success");
                    if(success==1){
                        Toast.makeText(Register.this,"You have successfully registered with us.Please login to continue",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(Register.this,"Something went wrong while registering.Please try again",Toast.LENGTH_LONG).show();
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
                map.put("name",s);
                map.put("email",s1);
                map.put("password",s2);
                return map;
            }
        };

        r.add(sr);
    }
}
