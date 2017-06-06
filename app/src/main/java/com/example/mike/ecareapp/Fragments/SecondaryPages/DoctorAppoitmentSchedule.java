package com.example.mike.ecareapp.Fragments.SecondaryPages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.Custom.Constants;
import com.example.mike.ecareapp.Interfaces.TransferInterface;
import com.example.mike.ecareapp.MainActivity;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoctorAppoitmentSchedule.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoctorAppoitmentSchedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAppoitmentSchedule extends DialogFragment implements TransferInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID = "id";
    private static final String HOSPITAL = "hospital";
    private static final String TREATMENT = "treatment";
    private static final String PATNAME = "patname";
    private static final String TIME = "time";
    private static final String DATE = "date";

    // TODO: Rename and change types of parameters
    private String myId;
    private String myHospital;
    private String myTreatment;
    private String myPatname;
    private String myTime;
    private String myDate;

    TextView time, date;

    private OnFragmentInteractionListener mListener;

    public DoctorAppoitmentSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorAppoitmentSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorAppoitmentSchedule newInstance(String param1, String param2, String treatment, String patname, String time, String date) {
        DoctorAppoitmentSchedule fragment = new DoctorAppoitmentSchedule();
        Bundle args = new Bundle();
        args.putString(ID, param1);
        args.putString(HOSPITAL, param2);
        args.putString(TREATMENT, treatment);
        args.putString(PATNAME,patname);
        args.putString(TIME, time);
        args.putString(DATE, date);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myId = getArguments().getString(ID);
            myHospital = getArguments().getString(HOSPITAL);
            myTreatment = getArguments().getString(TREATMENT);
            myTime = getArguments().getString(TIME);
            myPatname = getArguments().getString(PATNAME);
            myDate = getArguments().getString(DATE);
        }
    }

    AppiontmentItem appiontmentItem;
    String patname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_appoitment_schedule, container, false);
        appiontmentItem = new AppiontmentItem();

        final TextView name = (TextView) view.findViewById(R.id.tvname);
        final TextView hospital = (TextView) view.findViewById(R.id.tvHospital);
        final TextView treatment = (TextView) view.findViewById(R.id.tvtreatment);
        date = (TextView) view.findViewById(R.id.tvdate);
        time = (TextView) view.findViewById(R.id.tvtime);

        String url = Constants.PATIENT_GET_URL + myPatname;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("response", response);
                    JSONArray jsonObject = new JSONArray(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject(0);
                    patname = jsonObject1.getString("name");
                    hospital.setText(myHospital);
                    treatment.setText(myTreatment);
                    date.setText(myDate);
                    time.setText(myTime);
                    name.setText(myPatname);

                } catch (JSONException e) {
                    Log.d("Error in json", e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error in volley", error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);



        Button reschedule = (Button) view.findViewById(R.id.btnreschedule);
        Button confirm = (Button) view.findViewById(R.id.btnconfirm);
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DoctorAppointmentFrag",myId);
                FragmentManager manager = getFragmentManager();
                RescheduleFragment fragment = RescheduleFragment.newInstance(myId,"");
                fragment.show(manager,"Reschedule");
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appiontmentItem.setStatus("1");
                dismiss();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void getChanges(AppiontmentItem appiontmentItem) {
        date.setText(appiontmentItem.getDate());
        time.setText(appiontmentItem.getTime());
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
        void onFragmentInteraction(Uri uri);
    }
}
