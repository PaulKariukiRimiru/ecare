package com.example.mike.ecareapp.Fragments.SecondaryPages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Custom.Constants;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class AppointmentBookingFragment extends DialogFragment implements View.OnClickListener {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private int nYear;
    private int nMonth;
    private int nDay;
    private int nHour;
    private int nMinute;

    private String patName;
    private OnFragmentInteractionListener mListener;
    private String mydate;
    private Time mytime;

    public AppointmentBookingFragment() {
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    // TODO: Rename and change types of parameters
    private String docId;
    private String patId;
    private String docName;
    private String hospital;

    Spinner services;
    Button date;
    Button time;
    Button confirm;



    // TODO: Rename and change types and number of parameters
    public static AppointmentBookingFragment newInstance(String docId, String docName, String hospital) {
        AppointmentBookingFragment fragment = new AppointmentBookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, docId);
        args.putString(ARG_PARAM2, docName);
        args.putString(ARG_PARAM3, hospital);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            docId = getArguments().getString(ARG_PARAM1);
            docName = getArguments().getString(ARG_PARAM2);
            hospital = getArguments().getString(ARG_PARAM3);

            patId = mListener.getId();
            patName = mListener.getName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_booking, container, false);

        TextView thospital = (TextView) view.findViewById(R.id.tvHospital);
        TextView tdoctor = (TextView) view.findViewById(R.id.tvDoctor);
        TextInputEditText edname = (TextInputEditText) view.findViewById(R.id.editName);

        thospital.setText(hospital);
        tdoctor.setText(docName);
        edname.setText(patName);

        services = (Spinner) view.findViewById(R.id.spinnerServices);

        date = (Button) view.findViewById(R.id.btndate);
        time = (Button) view.findViewById(R.id.btntime);
        confirm = (Button) view.findViewById(R.id.btnconfirm);

        date.setOnClickListener(this);
        time.setOnClickListener(this);
        confirm.setOnClickListener(this);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Get Current Time
        final Calendar c2 = Calendar.getInstance();
        mHour = c2.get(Calendar.HOUR_OF_DAY);
        mMinute = c2.get(Calendar.MINUTE);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppointmentBookingFragment.OnFragmentInteractionListener) {
            mListener = (AppointmentBookingFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btndate:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog  = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nYear = year;
                        nMonth = month;
                        nDay = dayOfMonth;
                        date.setText(String.valueOf(nDay)+"/"+String.valueOf(nMonth)+"/"+String.valueOf(nYear));

                       mydate = String.valueOf(nDay)+"/"+String.valueOf(nMonth)+"/"+String.valueOf(nYear);
                        Log.d("date", mydate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();


                break;
            case R.id.btntime:
                // Get Current Time
                final Calendar c2 = Calendar.getInstance();
                mHour = c2.get(Calendar.HOUR_OF_DAY);
                mMinute = c2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        nHour = hourOfDay;
                        nMinute = minute;
                        time.setText(String.valueOf(nHour)+":"+String.valueOf(nMinute));

                        mytime = new Time(mHour, mMinute,00);
                        Log.d("time", mytime.toString());
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();


                break;
            case R.id.btnconfirm:
                final AppiontmentItem appiontmentItem = new AppiontmentItem();
                appiontmentItem.setStatus(String.valueOf(0));
                appiontmentItem.setDoc_id(docId);
                appiontmentItem.setPat_id(patId);
                appiontmentItem.setHospital(hospital);
                appiontmentItem.setTreatment(services.getSelectedItem().toString());

                Log.d("appointment", appiontmentItem.getPat_id()+appiontmentItem.getDoc_id()+appiontmentItem.getHospital()+ mydate.toString()+ mytime.toString()+ appiontmentItem.getTreatment()+ appiontmentItem.getStatus());

                StringRequest request = new StringRequest(Request.Method.POST, Constants.SHEDULE_REGISTER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RegisterUser","response");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean stat = jsonObject.getBoolean("error");
                            if (!stat){
                                Toast.makeText(getContext(),"Succesfully sent appointment wait for confirmation",Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("RegisterUser",error.getMessage());
                        Toast.makeText(getContext(),"error "+error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("shed_userId", appiontmentItem.getPat_id());
                        params.put("shed_docId", appiontmentItem.getDoc_id());
                        params.put("shed_hospital", appiontmentItem.getHospital());
                        params.put("shed_date", mydate);
                        params.put("shed_time", mytime.toString());
                        params.put("shed_treatment", appiontmentItem.getTreatment());
                        params.put("shed_status", appiontmentItem.getStatus());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(request);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        String getName();
        String getId();
        void setRange(long id);
    }
}
