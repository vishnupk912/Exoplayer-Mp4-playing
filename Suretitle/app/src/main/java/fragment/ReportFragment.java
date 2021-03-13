package fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.suretitle.www.PaymentPage;
import com.suretitle.www.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helper.ApiInterface;
import helper.SessionHandler;
import model.ReportsModel;

public class ReportFragment extends Fragment {

    View view;
    Dialog dialog,dialog_alert;
    SessionHandler sessionHandler;
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;
    RecyclerView recyclerView;
    LinearLayout linearLayout,premiumlayid;
    TextView contenttv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view =inflater.inflate(R.layout.fragment_report2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvid);


        sessionHandler=new SessionHandler(getActivity());
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        linearLayout=view.findViewById(R.id.layid);
        premiumlayid=view.findViewById(R.id.premiumlayid);
        contenttv=view.findViewById(R.id.tvid);



        premiumlayid.setVisibility(View.GONE);
        premiumlayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionHandler.createbackpayment("1");
                Intent intent=new Intent(getActivity(),PaymentPage.class);
                startActivity(intent);
            }
        });
        getcustomerdetails();
        reports();
//        subscriptionplans();
        return  view;
    }

    public void reports()
    {

        dialog.show();
        final ArrayList<ReportsModel> reportsModelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.reports, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("s",s);
                dialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String response=jsonObject1.getString("status");
                    if(response.contains("success"))
                    {

                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            ReportsModel reportsModel=new ReportsModel();
                            JSONObject jsonObject11=jsonArray.getJSONObject(i);
                            String report_id=jsonObject11.getString("report_id");
                            String token=jsonObject11.getString("token");
                            String customer_phone=jsonObject11.getString("customer_phone");
                            String report_file_path=jsonObject11.getString("report_file_path");
                            String uploaded_time=jsonObject11.getString("uploaded_time");
                            String property=jsonObject11.getString("property");
                            String country=jsonObject11.getString("country");
                            String state=jsonObject11.getString("state");

                            reportsModel.setReport_id(report_id);
                            reportsModel.setToken(token);
                            reportsModel.setCustomer_phone(customer_phone);
                            reportsModel.setReport_file_path(report_file_path);
                            reportsModel.setUploaded_time(uploaded_time);
                            reportsModel.setCountry(country);
                            reportsModel.setProperty(property);
                            reportsModel.setState(state);
                            reportsModelArrayList.add(reportsModel);
                        }

                        if(reportsModelArrayList.isEmpty())
                        {
                            recyclerView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ReportsAdapter adapter = new ReportsAdapter(getActivity(),reportsModelArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.GONE);
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
                dialog.dismiss();
                prompt("Server Error","The server encountered an internal error and was unable to complete your request");

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("key",ApiInterface.key);
                hashMap.put("phone",mobilenumber);

                return  hashMap;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void getcustomerdetails()
    {
        Gson gson = new Gson();
        HashMap<String, String> user = sessionHandler.getLoginSession();
        String json = user.get(SessionHandler.KEY_LOGIN);
        ArrayList alist = gson.fromJson(json, ArrayList.class);

        JSONArray jsonArrA = new JSONArray(alist);
        try {
            JSONObject userdata = jsonArrA.getJSONObject(0);
            mobilenumber = userdata.getString("mobile");
            customer_name = userdata.getString("customer_name");
            country_id = userdata.getString("country_id");
            state_id = userdata.getString("state_id");
            email = userdata.getString("email");
            profession = userdata.getString("profession");
            organisation = userdata.getString("organisation");
            gender = userdata.getString("gender");
            place = userdata.getString("place");


        } catch (JSONException e) {

        }
    }
    public  void prompt(final  String headerdata,final  String subheaderdata)
    {
        dialog_alert = new Dialog(getActivity());
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


    class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder>
    {

        Context context;
        private ArrayList<ReportsModel> listdata;

        public ReportsAdapter(Context context, ArrayList<ReportsModel> listdata) {
            this.context = context;
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item3, parent, false);
            ReportsAdapter.ViewHolder viewHolder = new ReportsAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder holder, int i)
        {
            final String report_id=listdata.get(i).getReport_id();
            String report_phone=listdata.get(i).getCustomer_phone();
            final String filename=listdata.get(i).getReport_file_path();
            String time=listdata.get(i).getUploaded_time();

            final String property=listdata.get(i).getProperty();

            holder.reportname.setText("Report on : "+property);
            holder.reporttime.setText(time);

            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(filename));
                    startActivity(browserIntent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView reportname,reporttime;
            LinearLayout lay,completedlayid;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.imageid);
                reportname=itemView.findViewById(R.id.tv1);
                reporttime=itemView.findViewById(R.id.tv2);
                lay=itemView.findViewById(R.id.lay);
                completedlayid=itemView.findViewById(R.id.completedlayid);

            }
        }
    }

//    public void subscriptionplans()
//    {
//        dialog.show();
//
//        RequestQueue requestQueue;
//        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//        requestQueue = new RequestQueue(cache, network);
//        requestQueue.start();
//
//
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.subscription_status, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                dialog.dismiss();
//                try {
//                    JSONObject jsonObject=new JSONObject(s);
//                    String response=jsonObject.getString("status");
//                    if(response.contains("success")) {
//                        String flag = jsonObject.getString("flag");
//
//                        if(flag.contains("2"))
//                        {
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//                                String plan_name=jsonObject1.getString("plan_name");
//                                String sid=jsonObject1.getString("sid");
//                                String plan_id=jsonObject1.getString("plan_id");
//                                String price=jsonObject1.getString("price");
//                                String start=jsonObject1.getString("start");
//                                String end=jsonObject1.getString("end");
//
//
//                                contenttv.setText("No Legal reports found,Analyze your property and access the reports . ");
//                                premiumlayid.setVisibility(View.GONE);
//
//                            }
//                        }
//                        else if(flag.contains("1"))
//                        {
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                                String plan_name = jsonObject1.getString("plan_name");
//                                String sid = jsonObject1.getString("sid");
//                                String plan_id = jsonObject1.getString("plan_id");
//                                String price = jsonObject1.getString("price");
//                                String start = jsonObject1.getString("start");
//                                String end = jsonObject1.getString("end");
//
//
//                                premiumlayid.setVisibility(View.GONE);
//                                linearLayout.setVisibility(View.VISIBLE);
//                                premiumlayid.setVisibility(View.VISIBLE);
//                                contenttv.setText("No Legal reports found,Be a Premium  member to access the  Reports ");
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//                            Intent intent=new Intent(getActivity(), PaymentPage.class);
//                            startActivity(intent);
//
//                            linearLayout.setVisibility(View.VISIBLE);
//                            premiumlayid.setVisibility(View.VISIBLE);
//                            contenttv.setText("No Legal reports found,Be a Premium  member to access the  Reports ");
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> hashMap=new HashMap<>();
//                hashMap.put("key",ApiInterface.key);
//                hashMap.put("customer_id",mobilenumber);
//                return  hashMap;
//            }
//        };
//        requestQueue.add(stringRequest);
//
//
//    }
}