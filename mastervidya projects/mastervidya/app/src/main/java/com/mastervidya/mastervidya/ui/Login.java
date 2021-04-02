package com.mastervidya.mastervidya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.AppSignatureHelper;
import com.mastervidya.mastervidya.helper.CheckNetwork;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.services.SMSListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    LinearLayout lay_signin;
    EditText mobilenumberet;
    String mobilenumber,app_signature;
    RequestQueue requestQueue;
    LinearLayout lay_main;
    Dialog dialog_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        lay_signin=findViewById(R.id.lay_signin);
        lay_main=findViewById(R.id.lay_main);
        mobilenumberet=findViewById(R.id.mobilenumberet);

        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();

        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        lay_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                mobilenumber=mobilenumberet.getText().toString();

                if(mobilenumber.length()<10)
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please enter a valid mobile number.",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();

                }
                else if(!CheckNetwork.isInternetAvailable(Login.this))
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Check your internet connectivity.",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else
                {
                    login(mobilenumber);
                }




            }
        });
    }

    public void login(final  String phone)
    {
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(Login.this);


        if(appSignatureHelper.getAppSignatures().get(0) !=null)
        {
            app_signature=appSignatureHelper.getAppSignatures().get(0);
        }


        dialog_progress.show();
        JSONObject json = new JSONObject();
        try
        {
            json.put("phone",phone);
            json.put("key",Url.key);
            json.put("app_signature",app_signature);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.login, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject)
                    {
                        dialog_progress.dismiss();
                        try {
                            String response=jsonObject.getString("status");
                            if(response.contains("success"))
                            {
                            Intent intent=new Intent(Login.this,OtpPage.class);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                            }
                            else
                            {
                                Snackbar snackbar=Snackbar.make(lay_main,"Some server error occurred .",Snackbar.LENGTH_LONG);
                                View sbView= snackbar.getView();
                                sbView.setBackgroundResource(R.color.colorprimarydark);
                                snackbar.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog_progress.dismiss();
                Snackbar snackbar=Snackbar.make(lay_main,"Some server error occurred .",Snackbar.LENGTH_LONG);
                View sbView= snackbar.getView();
                sbView.setBackgroundResource(R.color.colorprimarydark);
                snackbar.show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed()
    {
        ActivityCompat.finishAffinity(this);
    }


}