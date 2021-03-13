package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.ApiInterface;
import helper.SessionHandler;
import model.CountryModel;
import model.StateModel;

public class Register extends AppCompatActivity {

    SessionHandler sessionHandler;
    LinearLayout registerbt,backid;
    Spinner country_sp, state_sp,gender_sp;
    String country_id,state_id;
    Dialog dialog, dialog_alert;

    EditText etname,etnumber,etemail,etprofession,etorganization,etaddress;
    String name="",mobilenumber="",email="",address="",profession="",organization="",gender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register2);
        registerbt = findViewById(R.id.btid);
        backid=findViewById(R.id.backid);
        sessionHandler=new SessionHandler(this);

        country_sp = findViewById(R.id.countrysp);
        state_sp = findViewById(R.id.statesp);
        gender_sp=findViewById(R.id.gendersp);

        etname=findViewById(R.id.etname);
        etnumber=findViewById(R.id.etnumber);
        etemail=findViewById(R.id.etemail);
        etprofession=findViewById(R.id.etprofession);
        etorganization=findViewById(R.id.etorganization);
        etaddress=findViewById(R.id.etaddress);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Intent intent=getIntent();
        mobilenumber=intent.getStringExtra("mobilenumber");
        etnumber.setText(mobilenumber);


        backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Register.this,Login.class);
                startActivity(intent1);
            }
        });

        gender_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                gender= adapterView.getItemAtPosition(i).toString();
                if(gender.contains("Select Your gender"))
                {
                    gender="";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {



            }


        });

        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=etname.getText().toString();
                mobilenumber=etnumber.getText().toString();
                email=etemail.getText().toString();
                address=etaddress.getText().toString();
                profession=etprofession.getText().toString();
                organization=etorganization.getText().toString();

                if(name.isEmpty())
                {
                    prompt("Invalid name","Please enter a valid name");
                }
                else if(email.isEmpty())
                {
                    prompt("Invalid Email","Please enter a valid email");
                }
                else if(address.isEmpty())
                {
                    prompt("Invalid Address","Please enter a valid address");
                }
                else if(gender.isEmpty())
                {
                    prompt("Invalid Gender","Please enter a valid gender");
                }
                else
                    {
                        register();
                    }
            }
        });

        country();
        sessionHandler.createbackpayment("0");
    }

    public void country() {
        dialog.show();
        final ArrayList<CountryModel> countryModelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiInterface.country, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String response = jsonObject.getString("status");
                    if (response.contains("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryModel countryModel = new CountryModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            country_id = jsonObject1.getString("country_id");
                            String country_name = jsonObject1.getString("country_name");

                            countryModel.setId(country_id);
                            countryModel.setCountryname(country_name);
                            countryModelArrayList.add(countryModel);

                        }
                        final CountryAdapter spinnerAdapter = new CountryAdapter(Register.this, countryModelArrayList);
                        country_sp.setAdapter(spinnerAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    public class CountryAdapter extends BaseAdapter {


        private LayoutInflater inflater;
        Context context;
        private ArrayList<CountryModel> countryModelArrayList;

        public CountryAdapter(Context context, ArrayList<CountryModel> customertypeModelArrayList) {
            this.context = context;
            this.countryModelArrayList = customertypeModelArrayList;
        }

        @Override
        public int getCount() {
            return countryModelArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class Holder {
            private TextView name;
            private LinearLayout linearLayout;
        }


        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            View myView = null;
            try {
                final Holder holder;
                myView = convertView;

                if (myView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    myView = inflater.inflate(R.layout.listspinner, null);

                    holder = new Holder();
                    holder.name = (TextView) myView.findViewById(R.id.tvid);
                    holder.linearLayout = myView.findViewById(R.id.layid);
                    myView.setTag(holder);
                } else {
                    holder = (Holder) myView.getTag();
                }



                holder.name.setText(countryModelArrayList.get(i).getCountryname());

                country_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        country_id = countryModelArrayList.get(i).getId();

                        state(country_id);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return myView;
        }


    }

    public void state(final String countryid)
    {
        dialog.show();
        final ArrayList<StateModel> stateModelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.state, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String response = jsonObject.getString("status");
                    if (response.contains("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            StateModel stateModel = new StateModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            state_id = jsonObject1.getString("state_id");
                            String state_name = jsonObject1.getString("state_name");

                            stateModel.setStateid(state_id);
                            stateModel.setStatename(state_name);

                            stateModelArrayList.add(stateModel);
                        }
                        final StateAdapter stateAdapter = new StateAdapter(Register.this, stateModelArrayList);
                        state_sp.setAdapter(stateAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("country_id",countryid);
                hashMap.put("state_id","");
                hashMap.put("key",ApiInterface.key);
                return  hashMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    public class StateAdapter extends BaseAdapter {


        private LayoutInflater inflater;
        Context context;
        private ArrayList<StateModel> stateModelArrayList;

        public StateAdapter(Context context, ArrayList<StateModel> stateModelArrayList) {
            this.context = context;
            this.stateModelArrayList = stateModelArrayList;
        }

        @Override
        public int getCount() {
            return stateModelArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class Holder {
            private TextView roomname;
            private LinearLayout linearLayout;
        }


        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            View myView = null;
            try {
                final Holder holder;
                myView = convertView;

                if (myView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    myView = inflater.inflate(R.layout.listspinner, null);

                    holder = new Holder();
                    holder.roomname = (TextView) myView.findViewById(R.id.tvid);
                    holder.linearLayout = myView.findViewById(R.id.layid);
                    myView.setTag(holder);
                }
                else
                    {
                    holder = (Holder) myView.getTag();
                }


                holder.roomname.setText(stateModelArrayList.get(i).getStatename());


                state_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
                    {
                        state_id = stateModelArrayList.get(i).getStateid();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return myView;
        }
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

    public void register()
    {
      final   ArrayList<HashMap<String, String>> list = new ArrayList<>();

        dialog.show();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.editprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {

                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");

                    if(status.contains("success"))
                    {

                        Intent intent=new Intent(Register.this,DashBoard.class);
                        HashMap<String, String> news = new HashMap<>();
                        news.put("mobile", mobilenumber);
//                        news.put("customer_id", customer_id);
                        news.put("customer_name", name);
                        news.put("country_id", country_id);
                        news.put("state_id", state_id);
//                        news.put("country_name", country_name);
//                        news.put("state_name", state_name);
                        news.put("email", email);
                        news.put("profession", profession);
                        news.put("organisation", organization);
                        news.put("gender", gender);
                        news.put("place", address);


                        list.add(news);

                        Gson gson = new Gson();
                        List<HashMap<String, String>> textList = new ArrayList<>();
                        textList.addAll(list);
                        String jsonText = gson.toJson(textList);
                        sessionHandler.createLoginSession(jsonText);
                        startActivity(intent);


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
                hashMap.put("name",name);
                hashMap.put("email",email);
                hashMap.put("profession",profession);
                hashMap.put("organization",organization);
                hashMap.put("gender",gender);
                hashMap.put("country_id",country_id);
                hashMap.put("state_id",state_id);
                hashMap.put("address",address);



                return  hashMap;

            }
        };
        requestQueue.add(stringRequest);
    }
}