package com.mastervidya.mastervidya.Addclass;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.MonthsModel;
import com.mastervidya.mastervidya.model.SubModel;
import com.mastervidya.mastervidya.ui.AddClass;
import com.mastervidya.mastervidya.ui.TransactionActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PayForclassFragment extends Fragment implements PaymentResultListener {


    SessionHandler sessionHandler;
    Dialog dialog_progress, dialog_month;

    View view;
    String classid, classname, board;
    String id, name, phone, avatar_image;
    RequestQueue requestQueue;
    LinearLayout lay_main;
    RecyclerView recyclerView_sub;
    TextView boardtv, classtv, totalamounttv;
    LinearLayout yearlay, monthlay;

    TextView tv11, tv12, tv13, tv21, tv22, tv23, tv31, tv32, checkoutlay;
    RecyclerView recyclerView_months;

    String plantype = "";
    String monthly_normal, monthly_offer, yearly_normal, yearly_offer, agentid = "", transid = "";
    LinearLayout agentlay, onlinelay;
    int count = 0;
    Double totalamount = 0.0;
    String payment_type = "", subscription_type = "";
    ArrayList<String> arrayList_month = new ArrayList<>();
    ArrayList<String> arrayList_year = new ArrayList<>();


    ArrayList<String> arrayList_month1 = new ArrayList<>();
    ArrayList<String> arrayList_year1 = new ArrayList<>();


    EditText etchild;
    String childname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.laypayment, container, false);


        sessionHandler = new SessionHandler(getActivity());
        recyclerView_sub = view.findViewById(R.id.rvsub);
        lay_main = view.findViewById(R.id.lay_main);
        boardtv = view.findViewById(R.id.boardtv);
        classtv = view.findViewById(R.id.classtv);
        requestQueue = RequestQueueSingleton.getInstance(getActivity())
                .getRequestQueue();

        etchild = view.findViewById(R.id.etchild);
        yearlay = view.findViewById(R.id.yearlay);
        monthlay = view.findViewById(R.id.monthlay);
        agentlay = view.findViewById(R.id.agentlay);
        onlinelay = view.findViewById(R.id.onlinelay);
        checkoutlay = view.findViewById(R.id.checkoutlay);
        tv11 = view.findViewById(R.id.tv11);
        tv12 = view.findViewById(R.id.tv12);
        tv13 = view.findViewById(R.id.tv13);
        tv21 = view.findViewById(R.id.tv21);
        tv22 = view.findViewById(R.id.tv22);
        tv23 = view.findViewById(R.id.tv23);
        tv31 = view.findViewById(R.id.tv31);
        tv32 = view.findViewById(R.id.tv32);

        tv13.setPaintFlags(tv13.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tv23.setPaintFlags(tv23.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        totalamounttv = view.findViewById(R.id.totalamounttv);

        dialog_progress = new Dialog(getActivity());
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_month = new Dialog(getActivity());
        dialog_month.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_month.setContentView(R.layout.monthyearlay);
        dialog_month.setCancelable(false);
        dialog_month.setCanceledOnTouchOutside(false);
        dialog_month.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        recyclerView_months = dialog_month.findViewById(R.id.monthrv);
        LinearLayout selectmonthlay = dialog_month.findViewById(R.id.selectmonthid);
        ImageView close = dialog_month.findViewById(R.id.closeid);
        getcustomerdetails();


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList_month.clear();
                arrayList_year.clear();
                totalamount = 0.0;
                totalamounttv.setText("Rs " + totalamount);
                dialog_month.dismiss();
                getmonths();
                plantype = "";
            }
        });


        Bundle arguments = getArguments();
        if (arguments != null) {
            classid = getArguments().getString("classid");
            classname = getArguments().getString("classname");
            board = getArguments().getString("board");
        }
        boardtv.setText(board);
        classtv.setText(classname);
        subjects();
        getmonths();


        agentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onlinelay.setBackgroundResource(R.drawable.etshape);
                agentlay.setBackgroundResource(R.drawable.buttonshape1);
                tv31.setTextColor(getResources().getColor(R.color.white));
                tv32.setTextColor(getResources().getColor(R.color.black));
                payment_type = "2";


            }
        });


        onlinelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agentlay.setBackgroundResource(R.drawable.etshape);
                onlinelay.setBackgroundResource(R.drawable.buttonshape1);
                tv31.setTextColor(getResources().getColor(R.color.black));
                tv32.setTextColor(getResources().getColor(R.color.white));
                payment_type = "1";
            }
        });

        checkoutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childname = etchild.getText().toString();
                Toast.makeText(getActivity(), plantype, Toast.LENGTH_SHORT).show();
                if (childname.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(lay_main, "Enter child name.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                } else if (plantype.equals("")) {
                    Snackbar snackbar = Snackbar.make(lay_main, "Please Select you plan.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                } else if (payment_type.equals("")) {
                    Snackbar snackbar = Snackbar.make(lay_main, "Please Select you payment mode.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                } else if (payment_type.contains("1")) {
                    startPayment(totalamount);
                } else if (payment_type.contains("2")) {
                    agentverfication();
                }
            }
        });


        selectmonthlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList_month.isEmpty()) {

                    totalamount = 0.0;
                    Snackbar snackbar = Snackbar.make(lay_main, "Please Select the months.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();

                    tv11.setTextColor(getResources().getColor(R.color.colorprimarydark));
                    tv12.setTextColor(getResources().getColor(R.color.black));
                    tv13.setTextColor(getResources().getColor(R.color.dgrey2));

//                tv21.setTextColor(getResources().getColor(R.color.white));
//                tv22.setTextColor(getResources().getColor(R.color.white));
//                tv23.setTextColor(getResources().getColor(R.color.white));

                    yearlay.setBackgroundResource(R.drawable.etshape);
//                monthlay.setBackgroundResource(R.drawable.buttonshape1);

                    plantype = "";
                } else {
                    dialog_month.dismiss();
                    tv11.setTextColor(getResources().getColor(R.color.colorprimarydark));
                    tv12.setTextColor(getResources().getColor(R.color.black));
                    tv13.setTextColor(getResources().getColor(R.color.dgrey2));

                    tv21.setTextColor(getResources().getColor(R.color.white));
                    tv22.setTextColor(getResources().getColor(R.color.white));
                    tv23.setTextColor(getResources().getColor(R.color.white));

                    yearlay.setBackgroundResource(R.drawable.etshape);
                    monthlay.setBackgroundResource(R.drawable.buttonshape1);
                    totalamount = Double.parseDouble(monthly_offer) * count;
                    totalamounttv.setText("Rs " + totalamount);
                }


            }
        });


        yearlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantype = "2";
                tv11.setTextColor(getResources().getColor(R.color.white));
                tv12.setTextColor(getResources().getColor(R.color.white));
                tv13.setTextColor(getResources().getColor(R.color.white));

                tv21.setTextColor(getResources().getColor(R.color.colorprimarydark));
                tv22.setTextColor(getResources().getColor(R.color.black));
                tv23.setTextColor(getResources().getColor(R.color.dgrey2));

                yearlay.setBackgroundResource(R.drawable.buttonshape1);
                monthlay.setBackgroundResource(R.drawable.etshape);
                totalamount = Double.parseDouble(yearly_offer);

                totalamounttv.setText("Rs " + yearly_offer);


            }
        });

        monthlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                plantype = "1";
                dialog_month.show();

                tv11.setTextColor(getResources().getColor(R.color.colorprimarydark));
                tv12.setTextColor(getResources().getColor(R.color.black));
                tv13.setTextColor(getResources().getColor(R.color.dgrey2));

//                tv21.setTextColor(getResources().getColor(R.color.white));
//                tv22.setTextColor(getResources().getColor(R.color.white));
//                tv23.setTextColor(getResources().getColor(R.color.white));

                yearlay.setBackgroundResource(R.drawable.etshape);
//                monthlay.setBackgroundResource(R.drawable.buttonshape1);


            }
        });
        return view;
    }

    public void getcustomerdetails() {
        Gson gson = new Gson();
        HashMap<String, String> user = sessionHandler.getLoginSession();
        String json = user.get(sessionHandler.KEY_LOGIN);
        ArrayList alist = gson.fromJson(json, ArrayList.class);
        JSONArray jsonArrA = new JSONArray(alist);
        try {

            JSONObject userdata = jsonArrA.getJSONObject(0);
            id = userdata.getString("id");
            name = userdata.getString("name");
            phone = userdata.getString("phone");
            avatar_image = userdata.getString("avatar_image");

        } catch (Exception e) {

        }

    }

    private void subjects() {
        ArrayList<SubModel> subModelArrayList = new ArrayList<>();

        JSONObject json = new JSONObject();
        try {
            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("class_id", classid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog_progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.subjects, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        dialog_progress.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.contains("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SubModel subModel = new SubModel();

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String sub_id = jsonObject1.getString("id");
                                    String class_id = jsonObject1.getString("class_id");
                                    String subject = jsonObject1.getString("subject");

                                    subModel.setName(subject);

                                    subModelArrayList.add(subModel);


                                }
                            } else if (status.contains("invalid api key")) {
                                Dialog dialog;
                                dialog = new Dialog(getActivity());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.alertdialog);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();
                                LinearLayout linearLayout = dialog.findViewById(R.id.okid);


                                linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sessionHandler.logoutUser();
                                    }
                                });

                            }

                            SubjectAdapter adapter = new SubjectAdapter(getActivity(), subModelArrayList);
                            recyclerView_sub.setHasFixedSize(true);
                            recyclerView_sub.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
                            recyclerView_sub.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog_progress.dismiss();
                Snackbar snackbar = Snackbar.make(lay_main, "Some server error occurred .", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundResource(R.color.colorprimarydark);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("TAG", " payment successfull " + s.toString());


        Snackbar snackbar = Snackbar
                .make(lay_main, "Payment successfully done! ", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorprimarydark);
        snackbar.show();

        Intent intent = new Intent(getActivity(), TransactionActivity.class);
        intent.putExtra("key", sessionHandler.getuniquekey());
        intent.putExtra("id", id);
        intent.putExtra("studentname", childname);
        intent.putExtra("class_id", classid);
        intent.putExtra("payment_type", payment_type);
        intent.putExtra("agent_id", agentid);
        intent.putExtra("amount", String.valueOf(totalamount));
        intent.putExtra("transaction_id", "");
        intent.putExtra("subscription_type", plantype);
        intent.putStringArrayListExtra("arrayList_month", arrayList_month);
        intent.putStringArrayListExtra("arrayList_year", arrayList_year);
        intent.putStringArrayListExtra("arrayList_month1", arrayList_month1);
        intent.putStringArrayListExtra("arrayList_year1", arrayList_year1);
        startActivity(intent);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            Snackbar snackbar = Snackbar
                    .make(lay_main, "Payment error please try again", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundResource(R.color.colorprimarydark);
            snackbar.show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);


        }
    }


    public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
        Context context;
        ArrayList<SubModel> subModelArrayList = new ArrayList<>();

        public SubjectAdapter(Context context, ArrayList<SubModel> subModelArrayList) {
            this.context = context;
            this.subModelArrayList = subModelArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listitem = layoutInflater.inflate(R.layout.laysub, parent, false);
            ViewHolder viewHolder = new ViewHolder(listitem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String subname = subModelArrayList.get(position).getName();

            holder.subnametv.setText(position + 1 + ". " + subname);


        }

        @Override
        public int getItemCount() {
            return subModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subnametv;
            LinearLayout lay;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                subnametv = itemView.findViewById(R.id.name);
                lay = itemView.findViewById(R.id.lay);
            }
        }
    }

    public void getmonths() {
        ArrayList<MonthsModel> monthsModelArrayList = new ArrayList<>();

        JSONObject json = new JSONObject();
        try {
            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("class_id", classid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog_progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.month_package_list, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        dialog_progress.dismiss();
                        try {
                            String response = jsonObject.getString("status");
                            if (response.contains("success")) {

                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                JSONArray jsonArray = jsonObject1.getJSONArray("packages");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject11 = jsonArray.getJSONObject(i);

                                        String id = jsonObject11.getString("id");
                                        monthly_normal = jsonObject11.getString("monthly_normal");
                                        monthly_offer = jsonObject11.getString("monthly_offer");
                                        yearly_normal = jsonObject11.getString("yearly_normal");
                                        yearly_offer = jsonObject11.getString("yearly_offer");
                                        String class_id = jsonObject11.getString("class_id");

                                        tv12.setText("Rs " + yearly_offer);
                                        tv13.setText("Rs " + yearly_normal);
                                        tv22.setText("Rs " + monthly_offer);
                                        tv23.setText("Rs " + monthly_normal);

                                        tv13.setPaintFlags(tv13.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        tv23.setPaintFlags(tv23.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                                    }
                                }
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("months");
                                {
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        MonthsModel monthsModel = new MonthsModel();

                                        JSONObject jsonObject11 = jsonArray1.getJSONObject(i);
                                        String month = jsonObject11.getString("month");
                                        String year = jsonObject11.getString("year");
                                        String status = jsonObject11.getString("status");

                                        monthsModel.setMonth(month);
                                        monthsModel.setYear(year);
                                        monthsModel.setStatus(status);
                                        monthsModelArrayList.add(monthsModel);

                                        arrayList_month1.add(month);
                                        arrayList_year1.add(year);

                                    }

                                    Log.d("arraylist", arrayList_month1.toString());

                                }

                            }

                            MonthsAdapter adapter = new MonthsAdapter(getActivity(), monthsModelArrayList);
                            recyclerView_months.setHasFixedSize(true);
                            recyclerView_months.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
                            recyclerView_months.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog_progress.dismiss();
                Snackbar snackbar = Snackbar.make(lay_main, "Some server error occurred .", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundResource(R.color.colorprimarydark);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.ViewHolder> {
        Context context;
        ArrayList<MonthsModel> subModelArrayList = new ArrayList<>();

        public MonthsAdapter(Context context, ArrayList<MonthsModel> subModelArrayList) {
            this.context = context;
            this.subModelArrayList = subModelArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listitem = layoutInflater.inflate(R.layout.laymonth, parent, false);
            ViewHolder viewHolder = new ViewHolder(listitem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String month = subModelArrayList.get(position).getMonth();
            String year = subModelArrayList.get(position).getYear();
            String status = subModelArrayList.get(position).getStatus();


            if (subModelArrayList.get(position).isSelected()) {
                holder.lay.setBackgroundResource(R.drawable.etshape);

            }

            holder.tv1.setText(month);
            holder.tv2.setText(year);

            if (status.contains("1")) {
                holder.lay.setBackgroundResource(R.drawable.buttonshape1);
                holder.tv1.setTextColor(getResources().getColor(R.color.white));
                holder.tv2.setTextColor(getResources().getColor(R.color.white));
                holder.lay.setClickable(false);
            } else {
                holder.lay.setClickable(true);

                holder.lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (subModelArrayList.get(position).isSelected()) {
                            holder.lay.setBackgroundResource(R.drawable.etshape);
                            subModelArrayList.get(position).setSelected(false);
                            holder.tv1.setTextColor(getResources().getColor(R.color.colorprimarydark));
                            holder.tv2.setTextColor(getResources().getColor(R.color.black));
                            count--;
                            if (arrayList_month.contains(month) && arrayList_year.contains(year)) {
                                arrayList_month.remove(month);
                                arrayList_year.remove(year);
                            }

                        } else {
                            holder.lay.setBackgroundResource(R.drawable.buttonshape1);
                            subModelArrayList.get(position).setSelected(true);
                            holder.tv1.setTextColor(getResources().getColor(R.color.white));
                            holder.tv2.setTextColor(getResources().getColor(R.color.white));
                            count++;
                            arrayList_month.add(month);
                            arrayList_year.add(year);
                        }

                    }
                });

            }


        }

        @Override
        public int getItemCount() {
            return subModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv1, tv2;
            LinearLayout lay;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                lay = itemView.findViewById(R.id.lay);
            }
        }
    }


    public void agentverfication() {
        Dialog otp_agent;
        otp_agent = new Dialog(getActivity());
        otp_agent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otp_agent.setContentView(R.layout.agentotplay);
        otp_agent.setCancelable(true);
        otp_agent.setCanceledOnTouchOutside(true);
        otp_agent.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        otp_agent.show();
        LinearLayout submitlay, verifyotplay;
        EditText etagentcode, etotpcode;

        etagentcode = otp_agent.findViewById(R.id.et);
        etotpcode = otp_agent.findViewById(R.id.et1);
        submitlay = otp_agent.findViewById(R.id.submitlay);
        verifyotplay = otp_agent.findViewById(R.id.verfiyotplay);

        submitlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etagentcode.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(lay_main, "Please enter a Agent Code.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                } else {
                    agentid = etagentcode.getText().toString();
                    agentotpsend(etagentcode.getText().toString(), etagentcode, etotpcode, submitlay, verifyotplay);

                }
            }
        });

        verifyotplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etotpcode.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(lay_main, "Please enter OTP.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                } else {
                    agent_otpverify(etagentcode.getText().toString(), etagentcode, etotpcode, submitlay, verifyotplay, etotpcode.getText().toString());

                }
            }
        });


    }

    public void agentotpsend(final String agent_id, final EditText etagentcode, final EditText etotpcode,
                             final LinearLayout submitlay, final LinearLayout verifyotplay) {

        JSONObject json = new JSONObject();
        try {
            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("agent_id", agent_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.agent_otpsend, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status");
                    if (status.contains("success")) {
                        etagentcode.setVisibility(View.GONE);
                        etotpcode.setVisibility(View.VISIBLE);
                        submitlay.setVisibility(View.GONE);
                        verifyotplay.setVisibility(View.VISIBLE);
                    } else {
                        etagentcode.setVisibility(View.VISIBLE);
                        etotpcode.setVisibility(View.GONE);
                        submitlay.setVisibility(View.VISIBLE);
                        verifyotplay.setVisibility(View.GONE);
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

        requestQueue.add(jsonObjectRequest);

    }


    public void agent_otpverify(final String agent_id, final EditText etagentcode, final EditText etotpcode,
                                final LinearLayout submitlay, final LinearLayout verifyotplay, final String otp) {

        JSONObject json = new JSONObject();
        try {
            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("agent_id", agent_id);
            json.put("otp", otp);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.agent_otpverify, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status");
                    if (status.contains("verified")) {
                        Intent intent = new Intent(getActivity(), TransactionActivity.class);
                        intent.putExtra("key", sessionHandler.getuniquekey());
                        intent.putExtra("id", id);
                        intent.putExtra("studentname", childname);
                        intent.putExtra("class_id", classid);
                        intent.putExtra("payment_type", payment_type);
                        intent.putExtra("agent_id", agentid);
                        intent.putExtra("amount", String.valueOf(totalamount));
                        intent.putExtra("transaction_id", "");
                        intent.putExtra("subscription_type", plantype);
                        intent.putStringArrayListExtra("arrayList_month", arrayList_month);
                        intent.putStringArrayListExtra("arrayList_year", arrayList_year);
                        intent.putStringArrayListExtra("arrayList_month1", arrayList_month1);
                        intent.putStringArrayListExtra("arrayList_year1", arrayList_year1);

                        startActivity(intent);
//                        subscribe();
                    } else {
                        etagentcode.setVisibility(View.GONE);
                        etotpcode.setVisibility(View.VISIBLE);
                        submitlay.setVisibility(View.GONE);
                        verifyotplay.setVisibility(View.VISIBLE);
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

        requestQueue.add(jsonObjectRequest);

    }


    public void subscribe() {
        JSONObject json = new JSONObject();
        try {

            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("studentname", childname);
            json.put("class_id", classid);
            json.put("payment_type", payment_type);
            json.put("agent_id", agentid);
            json.put("amount", totalamount);
            json.put("transaction_id", transid);
            json.put("subscription_type", plantype);
            JSONArray array = new JSONArray();

            if (plantype.contains("1")) {
                for (int i = 0; i < arrayList_month.size(); i++) {
                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("month", arrayList_month.get(i));
                        obj.put("year", arrayList_year.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    array.put(obj);
                }
            } else if (plantype.contains("2")) {
                for (int i = 0; i < arrayList_month1.size(); i++) {
                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("month", arrayList_month1.get(i));
                        obj.put("year", arrayList_year1.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    array.put(obj);
                }
            }


            json.put("packages", array);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.subscribe, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status");
                    if (status.contains("verified")) {
                        Toast.makeText(getActivity(), "success congrats ", Toast.LENGTH_SHORT).show();
                    } else {

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

        requestQueue.add(jsonObjectRequest);
    }

    public void startPayment(final Double plancost)
    {


//        final Fragment fragment = getActivity();


        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "MasterVidya");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = String.valueOf(plancost);
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
//            preFill.put("email", );
            preFill.put("contact", phone);
            options.put("prefill", preFill);
            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }




}
