package com.example.mike.ecareapp.Fragments.SecondaryPages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Custom.Constants;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RescheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RescheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RescheduleFragment extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button date;
    Button time;
    Button confirm;

    private OnFragmentInteractionListener mListener;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int nYear;
    private int nMonth;
    private int nDay;
    private int mHour;
    private int mMinute;
    private int nHour;
    private int nMinute;

    public RescheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RescheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RescheduleFragment newInstance(String param1, String param2) {
        RescheduleFragment fragment = new RescheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reschedule, container, false);

        date = (Button) view.findViewById(R.id.btndate);
        time = (Button) view.findViewById(R.id.btntime);
        confirm = (Button) view.findViewById(R.id.btnconfirm);

        date.setOnClickListener(this);
        time.setOnClickListener(this);
        confirm.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    Date myDate;
    Time mytime;
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
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                myDate = new Date(mYear,mMonth,mDay);

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
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
                mytime = new Time(mHour,mMinute,00);

                break;
            case R.id.btnconfirm:
                final AppiontmentItem appiontmentItem = new AppiontmentItem();
                appiontmentItem.setAppoint_id(mParam1);
                appiontmentItem.setDate(myDate.toString());
                appiontmentItem.setTime(mytime.toString());
                appiontmentItem.setStatus(String.valueOf(1));

                JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.POST, Constants.SHEDULE_UPDATE, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("error");
                            if (!success){
                                Snackbar.make(view,"Appointment Confirmed",Snackbar.LENGTH_LONG).show();
                                dismiss();
                            }else {
                                Snackbar.make(view,"Appointment Confirmed Failed",Snackbar.LENGTH_LONG).show();
                                Log.d("Error message:",response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error message:",error.getLocalizedMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("shed_id", appiontmentItem.getAppoint_id());
                        params.put("shed_status", appiontmentItem.getStatus());
                        params.put("shed_date", appiontmentItem.getDate());
                        params.put("shed_time", appiontmentItem.getTime());
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(arrayRequest);

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

    }

}
