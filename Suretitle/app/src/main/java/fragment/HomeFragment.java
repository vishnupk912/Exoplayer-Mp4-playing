package fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suretitle.www.PaymentPage;
import com.suretitle.www.QuestionCatogery;
import com.suretitle.www.QuizDetails;
import com.suretitle.www.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helper.ApiInterface;
import helper.SessionHandler;
import model.CountryModel;
import model.PropertyModel;


public class HomeFragment extends Fragment {

    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender;
    SessionHandler sessionHandler;
    RecyclerView recyclerView;
    View view;
    Dialog dialog,dialog_alert;
    LinearLayout laymain;
    ImageView callid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_home2, container, false);

        callid=view.findViewById(R.id.callid);
        laymain=view.findViewById(R.id.laymain);
        sessionHandler=new SessionHandler(getActivity());
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));





        getcustomerdetails();
        propertymethod();

//        subscriptionplans();


        callid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String phone = "+919821080661";
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//                startActivity(intent);

                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "SureTitle.in@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SureTitle Review & queries");
//                    intent.putExtra(Intent.EXTRA_TEXT, "Respected Sir,");
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    //TODO smth
                }
            }
        });
        return  view;
    }


    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{


        Context context;
        private ArrayList<PropertyModel> listdata;

        public MyListAdapter( Context context, ArrayList<PropertyModel> listdata1)
        {
            this.listdata = listdata;
            this.context = context;
            this.listdata = listdata1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int i) {

            String imgurl=listdata.get(i).getImage();
            final String count=listdata.get(i).getCount();
            final String propertyid=listdata.get(i).getProperty_id();
            final String propertyname=listdata.get(i).getProperty_name();

            if(count.contains("0"))
            {
                holder.imageView_key.setVisibility(View.VISIBLE);
                holder.relativeLayout.setClickable(false);
            }
            else
            {
                holder.imageView_key.setVisibility(View.GONE);
                holder.relativeLayout.setClickable(true);
                holder.viewid.setBackgroundResource(R.drawable.round2);
            }

            if(imgurl!=null && imgurl.length()>0)
            {
                Picasso.get().load(imgurl).fit().error(R.drawable.logosure).into(holder.imageView);

            }
            else
            {
                Picasso.get().load(R.drawable.logosure).fit().error(R.drawable.logosure).into(holder.imageView);
            }

            holder.textView.setText(listdata.get(i).getProperty_name());
            holder.relativeLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(count.contains("0"))
                    {
                        Snackbar snackbar = Snackbar
                                .make(laymain, "Coming soon", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();
                    }

                    else
                    {
                        Intent intent=new Intent(getActivity(), QuizDetails.class);
                        intent.putExtra("propertyid",propertyid);
                        intent.putExtra("propertyname",propertyname);
                        getActivity().overridePendingTransition(R.anim.slide_in1,R.anim.slide_out1);
                       startActivity(intent);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public LinearLayout relativeLayout;
            public  ImageView imageView_key;
            View viewid;
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
                this.textView = (TextView) itemView.findViewById(R.id.textView);
                relativeLayout = (LinearLayout)itemView.findViewById(R.id.relativeLayout);
                imageView_key=itemView.findViewById(R.id.keyid);
                viewid=itemView.findViewById(R.id.viewid);
            }
        }
    }


    public void propertymethod()
    {
        dialog.show();
        final ArrayList<PropertyModel> propertyModelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.property_type, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String response=jsonObject1.getString("status");
                    if(response.contains("success"))
                    {
                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            PropertyModel propertyModel=new PropertyModel();
                            JSONObject jsonObject11=jsonArray.getJSONObject(i);
                            String property_type_id=jsonObject11.getString("property_type_id");
                            String property_type=jsonObject11.getString("property_type");
                            String filename=jsonObject11.getString("filename");
                            String count=jsonObject11.getString("count");

                            propertyModel.setProperty_id(property_type_id);
                            propertyModel.setProperty_name(property_type);
                            propertyModel.setImage(filename);
                            propertyModel.setCount(count);
                            propertyModelArrayList.add(propertyModel);



                            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvid);
                            MyListAdapter adapter = new MyListAdapter(getActivity(),propertyModelArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                            recyclerView.setAdapter(adapter);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put("country_id",country_id);
                hashMap.put("state_id",state_id);
                hashMap.put("key",ApiInterface.key);

                return  hashMap;
            }
        };


        requestQueue.add(stringRequest);


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

        } catch (JSONException e) {

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
//                        if(flag.contains("1")||flag.contains("2"))
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
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//                            Intent intent=new Intent(getActivity(),PaymentPage.class);
//                            startActivity(intent);
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