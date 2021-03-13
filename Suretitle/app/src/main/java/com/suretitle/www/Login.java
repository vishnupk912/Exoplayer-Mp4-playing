package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import helper.ApiInterface;
import helper.AppSignatureHelper;
import helper.CheckNetwork;

public class Login extends AppCompatActivity {

    LinearLayout loginbt,mainlay;
    String app_signature="",mobilenumber;
    EditText mobilenumberet;
    Dialog dialog,dialog_alert;
    CountryCodePicker countryCodePicker;
    String block_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        countryCodePicker=findViewById(R.id.ccp);
        loginbt=findViewById(R.id.loginbtid);
        mobilenumberet=findViewById(R.id.etid);
        mainlay=findViewById(R.id.mainlay);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));





        loginbt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mobilenumber=mobilenumberet.getText().toString();

                if(!CheckNetwork.isInternetAvailable(Login.this))
                {
                    prompt("Network Error","There was a error connecting.Please check your internet .");

                }

                 if(mobilenumber.length()<10)
                {
                    prompt("Invalid Mobile Number","Inorder to continue with SureTitle,please enter a valid mobile number");

                }
                else
                {
                  login(countryCodePicker.getSelectedCountryCode()+mobilenumber);
                }

            }
        });
    }

    public  void login(final String mobilenumber)
    {
        dialog.show();
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(Login.this);

        if(appSignatureHelper.getAppSignatures().get(0) !=null)
        {
            app_signature=appSignatureHelper.getAppSignatures().get(0);
        }

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");

                    if(status.contains("success"))
                    {
                        is_blocked(mobilenumber);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog.dismiss();
                prompt("Server Error","The server encountered an internal error and was unable to complete your request");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("app_signature",app_signature);
                hashMap.put("phone",mobilenumber);
                hashMap.put("key",ApiInterface.key);

                Log.d("key",app_signature);
                return  hashMap;

            }
        };

        requestQueue.add(stringRequest);
    }



    public  void prompt(final  String headerdata,final  String subheaderdata)
    {
        dialog_alert = new Dialog(this);
        dialog_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_alert.setContentView(R.layout.alertbox);
        dialog_alert.setCancelable(false);
        dialog_alert.setCanceledOnTouchOutside(false);
        dialog_alert.setCancelable(false);
        dialog_alert.setCanceledOnTouchOutside(false);
        dialog_alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout cancellay,oklay;
        TextView headertv, subheadertv,canceltv,oktv;

        cancellay=dialog_alert.findViewById(R.id.cancellayid);
        oklay=dialog_alert.findViewById(R.id.oklayid);
        headertv=dialog_alert.findViewById(R.id.headerid);
        subheadertv=dialog_alert.findViewById(R.id.sheaderid);
        canceltv=dialog_alert.findViewById(R.id.canceltvid);
        oktv=dialog_alert.findViewById(R.id.oktvid);

        headertv.setText(headerdata);
        subheadertv.setText(subheaderdata);

        cancellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_alert.dismiss();
            }
        });
        oklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_alert.dismiss();
            }
        });

        dialog_alert.show();

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);

    }

    public void is_blocked(final  String mobilenumber)
    {
        dialog.show();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.is_blocked, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {

                         block_status=jsonObject.getString("block_status");
                        if(block_status.contains("0"))
                        {
                            Intent intent=new Intent(Login.this,OtpPage.class);
                            intent.putExtra("mobilenumber",mobilenumber);
                            startActivity(intent);
                        }
                        else
                        {
                            prompt("Permission Denied","SureTitle has denied your permission to access this account.");

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("key",ApiInterface.key);
                hashMap.put("phone",mobilenumber);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }


}