package com.suretitle.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.ApiInterface;
import helper.CheckNetwork;
import helper.SessionHandler;
import services.SMSListener;

public class OtpPage extends AppCompatActivity {

    SessionHandler sessionHandler;
    LinearLayout verifyotpbt,backid;
    EditText etOTP1,etOTP2 ,etOTP3,etOTP4;
    String mobilenumber;
    Dialog dialog,dialog_alert;
    String refreshedToken;
    TextView numbertvid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_otp_page);

        sessionHandler=new SessionHandler(this);

        numbertvid=findViewById(R.id.numbertvid);


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful())
                {
                    return;
                }

                if( task.getResult()!=null)
                {
                    refreshedToken = task.getResult().getToken();
                    Log.d("aaaaaaaaaa token1",refreshedToken);
                }
            }
        });

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        verifyotpbt=findViewById(R.id.btid);
        backid=findViewById(R.id.backid);
        etOTP1=findViewById(R.id.et1);
        etOTP2=findViewById(R.id.et2);
        etOTP3=findViewById(R.id.et3);
        etOTP4=findViewById(R.id.et4);


        Intent intent=getIntent();
        mobilenumber=intent.getStringExtra("mobilenumber");
        etOTP1.requestFocus();

        numbertvid.setText("+"+mobilenumber);

        final StringBuilder sb = new StringBuilder();



        etOTP1.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub


                if (sb.length() == 0 & etOTP1.length() == 1) {
                    sb.append(s);
                    etOTP1.clearFocus();
                    etOTP2.requestFocus();
                    etOTP2.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {
                    sb.deleteCharAt(0);

                }

            }

            public void afterTextChanged(Editable s) {


            }
        });

        etOTP2.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etOTP2.length() == 1) {
                    sb.append(s);
                    etOTP2.clearFocus();
                    etOTP3.requestFocus();
                    etOTP3.setCursorVisible(true);

                } else {
                    if (etOTP2.length() > 0) {
                        etOTP2.setText("");
                    } else if (etOTP2.length() == 0) {
                        etOTP2.clearFocus();
                        etOTP1.requestFocus();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sb.length() == 1) {

                    sb.deleteCharAt(0);

                }

            }

            public void afterTextChanged(Editable s) {


            }
        });

        etOTP3.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etOTP3.length() == 1) {
                    sb.append(s);
                    etOTP3.clearFocus();
                    etOTP4.requestFocus();
                    etOTP4.setCursorVisible(true);

                } else {
                    if (etOTP3.length() > 0) {
                        etOTP3.setText("");
                    } else if (etOTP3.length() == 0) {
                        etOTP3.clearFocus();
                        etOTP2.requestFocus();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {

                    sb.deleteCharAt(0);
                }

            }

            public void afterTextChanged(Editable s) {


            }
        });

        etOTP4.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & etOTP4.length() == 1) {
                    sb.append(s);

                } else {
                    if (etOTP4.length() > 0) {
                        etOTP4.setText("");
                    } else if (etOTP4.length() == 0) {
                        etOTP4.clearFocus();
                        etOTP3.requestFocus();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (sb.length() == 1) {

                    sb.deleteCharAt(0);
                }

            }

            public void afterTextChanged(Editable s) {


            }
        });


        backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        verifyotpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String otp1=etOTP1.getText().toString()+etOTP2.getText().toString()+etOTP3.getText().toString()+etOTP4.getText().toString();
//                if(!CheckNetwork.isInternetAvailable(OtpPage.this))
//                {
//                    prompt("Network Error","There was a error connecting.Please check your internet .");
//
//                }
                 if(otp1.isEmpty())
                {
                    prompt("Invalid OTP","Please enter the OTP");

                }
                else
                {
                    verifyotp(otp1);
                }

            }
        });

        setAutoRead();
    }
    public void verifyotp(final String otp)
    {
        final   ArrayList<HashMap<String, String>> list = new ArrayList<>();
        final   ArrayList<HashMap<String, String>> listuserdetails = new ArrayList<>();

        dialog.show();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.otpverify, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {

                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    String user=jsonObject.getString("user");

                    Log.d("user",user);

                    if(status.contains("verified"))
                    {
                        if(user.contains("new"))
                        {
                            Intent intent=new Intent(OtpPage.this,Register.class);
                            intent.putExtra("mobilenumber",mobilenumber);
                            startActivity(intent);

                        }
                        else  if(user.contains("Existing"))
                        {
                            JSONObject jsonObject1=jsonObject.getJSONObject("userdata");
                            String customer_id=jsonObject1.getString("customer_id");
                            String customer_name=jsonObject1.getString("customer_name");
                            String country_id=jsonObject1.getString("country_id");
                            String state_id=jsonObject1.getString("state_id");
                            String country_name=jsonObject1.getString("country_name");
                            String state_name=jsonObject1.getString("state_name");
                            String email=jsonObject1.getString("email");
                            String profession=jsonObject1.getString("profession");
                            String organisation=jsonObject1.getString("organisation");
                            String gender=jsonObject1.getString("gender");
                            String place=jsonObject1.getString("place");

                            Intent intent=new Intent(OtpPage.this,DashBoard.class);
                            intent.putExtra("mobilenumber",mobilenumber);
                            startActivity(intent);


                            HashMap<String, String> news = new HashMap<>();
                            news.put("mobile", mobilenumber);
//                            news.put("customer_id", customer_id);
                            news.put("customer_name", customer_name);
                            news.put("country_id", country_id);
                            news.put("state_id", state_id);
//                            news.put("country_name", country_name);
//                            news.put("state_name", state_name);
                            news.put("email", email);
                            news.put("profession", profession);
                            news.put("organisation", organisation);
                            news.put("gender", gender);
                            news.put("place", place);

                            list.add(news);

                            Gson gson = new Gson();
                            List<HashMap<String, String>> textList = new ArrayList<>();
                            textList.addAll(list);
                            String jsonText = gson.toJson(textList);
                            sessionHandler.createLoginSession(jsonText);

                        }

                    }
                    else
                    {
                        prompt("Invalid OTP","Please enter a valid  OTP");
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("phone",mobilenumber);
                hashMap.put("key",ApiInterface.key);
                hashMap.put("otp",otp);
                hashMap.put("push_token",refreshedToken);

                Log.d("phone",mobilenumber);
                Log.d("key",ApiInterface.key);
                Log.d("otp",otp);
                Log.d("push_token",refreshedToken);

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
    private void setAutoRead()
    {


        try {
            SmsRetrieverClient mClient = SmsRetriever.getClient(this);
            Task<Void> mTask = mClient.startSmsRetriever();
            mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override public void onSuccess(Void aVoid)
                {

                }
            });
            mTask.addOnFailureListener(new OnFailureListener() {
                @Override public void onFailure(@NonNull Exception e)
                {
                    Log.d("extractedOTP","extractedOTP"+e);
                }
            });

            final SMSListener smsListener=new SMSListener();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            registerReceiver(smsListener, intentFilter);

            smsListener.setOnOtpListeners(new SMSListener.OTPListener()
            {
                @Override
                public void onOTPReceived(String extractedOTP) {
                    try {



                        char otp1 = extractedOTP.charAt(0);
                        char otp2 = extractedOTP.charAt(1);
                        char otp3 = extractedOTP.charAt(2);
                        char otp4 = extractedOTP.charAt(3);


                        etOTP1.setText(String.valueOf(otp1));
                        etOTP2.setText(String.valueOf(otp2));
                        etOTP3.setText(String.valueOf(otp3));
                        etOTP4.setText(String.valueOf(otp4));

                        etOTP1.setEnabled(true);
                        etOTP2.setEnabled(true);
                        etOTP3.setEnabled(true);
                        etOTP4.setEnabled(true);


                    }
                    catch (Exception ignored)
                    {

                    }
                }
            });

        } catch (Exception e) {
        }
    }
}