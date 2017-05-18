package com.example.mike.ecareapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.mike.ecareapp.Database.DatabaseHandler;
import com.example.mike.ecareapp.Interfaces.TransferInterface;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.R;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static DoctorAppoitmentSchedule newInstance(String param1, String param2) {
        DoctorAppoitmentSchedule fragment = new DoctorAppoitmentSchedule();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_appoitment_schedule, container, false);

        final DatabaseHandler handler = new DatabaseHandler(getContext());
        final AppiontmentItem appiontmentItem = handler.getAppointment(mParam1);
        TextView name = (TextView) view.findViewById(R.id.tvname);
        TextView hospital = (TextView) view.findViewById(R.id.tvHospital);
        TextView treatment = (TextView) view.findViewById(R.id.tvtreatment);
        date = (TextView) view.findViewById(R.id.tvdate);
        time = (TextView) view.findViewById(R.id.tvtime);

        hospital.setText(appiontmentItem.getHospital());
        treatment.setText(appiontmentItem.getTreatment());
        date.setText(appiontmentItem.getDay()+"/"+appiontmentItem.getMonth()+"/"+appiontmentItem.getYear());
        time.setText(appiontmentItem.getHour()+":"+appiontmentItem.getMinute());
        name.setText(handler.getPatient(appiontmentItem.getPat_id()).getName());

        Button reschedule = (Button) view.findViewById(R.id.btnreschedule);
        Button confirm = (Button) view.findViewById(R.id.btnconfirm);
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                RescheduleFragment fragment = RescheduleFragment.newInstance(mParam1,"");
                fragment.show(manager,"Reschedule");
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appiontmentItem.setStatus("1");
                handler.modifyAppointment(appiontmentItem);
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
        date.setText(appiontmentItem.getDay()+"/"+appiontmentItem.getMonth()+"/"+appiontmentItem.getYear());
        time.setText(appiontmentItem.getHour()+":"+appiontmentItem.getMinute());
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
