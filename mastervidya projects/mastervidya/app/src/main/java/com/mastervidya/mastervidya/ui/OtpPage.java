package com.mastervidya.mastervidya.ui;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.AppSignatureHelper;
import com.mastervidya.mastervidya.helper.CheckNetwork;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.services.SMSListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtpPage extends AppCompatActivity {

    LinearLayout etlay,dotlay;
    LinearLayout layout_otp;
    EditText etOTP1,etOTP2 ,etOTP3,etOTP4;
    String otp,phone;
    Dialog dialog_progress;
    RequestQueue requestQueue;
    LinearLayout lay_main;
    SessionHandler sessionHandler;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);


        define();

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        etOTP1.requestFocus();
        tv1=findViewById(R.id.tv1);
        tv1.setText("We'll text you on "+phone);


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


        layout_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otp1=etOTP1.getText().toString()+etOTP2.getText().toString()+etOTP3.getText().toString()+etOTP4.getText().toString();

                if(otp1.isEmpty() || otp1.length()<4)
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please enter a valid OTP.",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else if(!CheckNetwork.isInternetAvailable(OtpPage.this))
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Check your internet connectivity.",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else
                {
                    verfymethod(phone,Integer.parseInt(otp1));
                }





            }
        });

        dotlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etlay.setVisibility(View.VISIBLE);
                dotlay.setVisibility(View.GONE);
            }
        });
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
                @Override public void onFailure(@NonNull Exception e) {
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

                        etlay.setVisibility(View.VISIBLE);
                        dotlay.setVisibility(View.GONE);

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


    public void verfymethod(final  String phone,final  int otp1)
    {
        final ArrayList<HashMap<String,String>>list=new ArrayList<>();
        dialog_progress.show();
        JSONObject json = new JSONObject();

        try
        {
            json.put("phone",phone);
            json.put("key", Url.key);
            json.put("push_token", "sdf");
            json.put("otp", otp1);





        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.otpverify, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject)
                    {

                        dialog_progress.dismiss();
                        try {
                            String response=jsonObject.getString("status");


                            if(response.contains("verified"))
                            {
                                String user=jsonObject.getString("user");
                                String unique_key=jsonObject.getString("unique_key");
                                sessionHandler.setuniquekey(unique_key);
                                if(user.contains("new"))
                                {
                                    JSONObject jsonObject1=jsonObject.getJSONObject("userdata");
                                    String id=jsonObject1.getString("id");
                                    Intent intent=new Intent(OtpPage.this,Signup.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                }
                                else
                                {
                                    JSONObject jsonObject1=jsonObject.getJSONObject("userdata");
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");
                                    String address=jsonObject1.getString("address");
                                    String phone=jsonObject1.getString("phone");

                                    String avatar_image=Url.base_avatar+jsonObject1.getString("avatar")+".png";

                                    HashMap<String, String> news = new HashMap<>();
                                    news.put("id", id);
                                    news.put("name", name);
                                    news.put("phone", phone);
                                    news.put("avatar_image", avatar_image);
                                    list.add(news);


                                    Gson gson = new Gson();
                                    List<HashMap<String, String>> textList = new ArrayList<>();
                                    textList.addAll(list);
                                    String jsonText = gson.toJson(textList);
                                    sessionHandler.createLoginSession(jsonText);

                                    Intent intent=new Intent(OtpPage.this,Homepage.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                Snackbar snackbar=Snackbar.make(lay_main,"Please enter a valid OTP.",Snackbar.LENGTH_LONG);
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

            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    public void define()
    {
        sessionHandler=new SessionHandler(this);
        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();


        etOTP1=findViewById(R.id.et1);
        etOTP2=findViewById(R.id.et2);
        etOTP3=findViewById(R.id.et3);
        etOTP4=findViewById(R.id.et4);

        etlay=findViewById(R.id.etlay);
        dotlay=findViewById(R.id.dotlay);
        layout_otp=findViewById(R.id.layout_otp);
        lay_main=findViewById(R.id.lay_main);
    }

}