package com.example.mike.ecareapp.Custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mike.ecareapp.MainActivity;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Mike on 5/28/2017.
 */

public class ProcessUser {

    private static ProcessUser processUser;
    private  Context context;
    private  WorkerInterface workerInterface;
    private int type;
    private  MainObject mainObject;
    private  Activity activity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    public ProcessUser (Context context, int type){
        this.context = context;
        this.type = type;
    }

    public ProcessUser(Context context, Activity activity, WorkerInterface workerInterface){
        this.context = context;
        this.activity = activity;
        this.workerInterface = workerInterface;

        mAuth = FirebaseAuth.getInstance();
        Log.d("ProcessUser: ","Auth object created");

    }

    public ProcessUser(Context context, Activity activity, int type){
        this.context = context;
        this.activity = activity;
        this.type = type;

        mAuth = FirebaseAuth.getInstance();
        Log.d("ProcessUser: ","Auth object created");

    }

    /**
     * @param mainObject
     * @return
     *
     * methord for getting the type of object
     */

    private int userType(MainObject mainObject){
        if (mainObject instanceof PatientItem){
            return 0;
        }else
            return 1;
    }

    /**
     *
     * @param mainObject
     *
     * methord to create firebase user
     */

    public void createFirebaseUser(MainObject mainObject){
        this.mainObject = mainObject;
        Log.d("createFirebaaseUser: ","Start of methord");
        // [START create_user_with_email]
        switch (userType(mainObject)){
            case 0:
            PatientItem patientItem = (PatientItem) mainObject;
                mAuth.createUserWithEmailAndPassword(patientItem.getEmail(), patientItem.getPassword())
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    registerUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(activity, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // [START_EXCLUDE]
                                //  hideProgressDialog();
                                // [END_EXCLUDE]
                            }
                        });
                // [END create_user_with_email]

                break;
            case 1:
            DoctorItem doctorItem = (DoctorItem) mainObject;
                mAuth.createUserWithEmailAndPassword(doctorItem.getEmail(), doctorItem.getPassword())
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    registerUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(activity, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // [START_EXCLUDE]
                                //hideProgressDialog();
                                // [END_EXCLUDE]
                            }
                        });
                // [END create_user_with_email]

                break;
        }

    }

    /**
     * methord to send email notification
     */

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        //findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(context, "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    /**
     *
     * @param email
     * @param password
     * @param type
     * @return
     *
     * methord to confirm user details
     */

    public boolean confirmDetails(final String email, String password){
        Log.d(TAG, "signIn:" + email);
        final boolean[] status = new boolean[1];
        //showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Log.d("Email", email);
                            String url = Constants.PATIENT_GET_URL+email;

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        Log.d("response", response);
                                        JSONArray jsonObject = new JSONArray(response);
                                        JSONObject jsonObject1 = jsonObject.getJSONObject(0);

                                        Intent intent = new Intent(context, MainActivity.class);
                                        intent.putExtra("type", type);
                                        intent.putExtra("id", jsonObject1.getString("id"));
                                        activity.startActivity(intent);;
                                        activity.finish();
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

//                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.PATIENT_GET_URL, null, new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//
//                                    try {
//                                        Intent intent = new Intent(context, MainActivity.class);
//                                        intent.putExtra("type", type);
//                                        intent.putExtra("id", response.getString("id"));
//                                        activity.startActivity(intent);;
//                                        activity.finish();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    error.printStackTrace();
//                                }
//                            }){
//                                @Override
//                                protected Map<String, String> getParams() throws AuthFailureError {
//                                    Map<String, String> params = new HashMap<String, String>();
//                                    params.put("pat_email", email);
//                                    return params;
//                                }
//                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            status[0] = false;
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                            status[0] = false;
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
        return status[0];
    }

    /**
     * @param
     * @return
     *
     * methrod to register users to ecare database
     */
    String id;
    public String getUserId(){
        return id;
    }

    private void registerUser(){

        switch (userType(mainObject)) {
            case 0:

                final PatientItem patientItem = (PatientItem) mainObject;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PATIENT_REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("RegisterUser","response");
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("object",response);
                                    boolean stat = jsonObject.getBoolean("error");
                                    if (!stat){
                                        sendEmailVerification();
                                        workerInterface.isSuccessful(true);
                                    }else {
                                        workerInterface.isSuccessful(false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("RegisterUser",error.getLocalizedMessage());

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pat_name", patientItem.getName());
                        params.put("pat_email", patientItem.getEmail());
                        params.put("pat_password", patientItem.getPassword());
                        params.put("pat_location", patientItem.getLocation());
                        return params;
                    }

                };
                final RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            break;

            case 1:
                final DoctorItem doctorItem = (DoctorItem) mainObject;

                StringRequest request = new StringRequest(Request.Method.POST, Constants.DOCTOR_REGISTER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RegisterUser","response");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean stat = jsonObject.getBoolean("error");
                            if (!stat){
                                sendEmailVerification();
                                workerInterface.isSuccessful(true);
                            }else {
                                workerInterface.isSuccessful(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RegisterUser",error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("doc_name", doctorItem.getName());
                        params.put("doc_email", doctorItem.getEmail());
                        params.put("doc_password", doctorItem.getPassword());
                        params.put("doc_hospital", doctorItem.getHospital());
                        params.put("doc_specialty", doctorItem.getSpecialty());
                        return params;
                    }
                };
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                requestQueue1.add(request);

            break;

        }
    }

    public void onStart(){
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean signOutUser(){
        final boolean[] status = {false};
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    firebaseAuth.signOut();
                    status[0] = true;
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        return status[0];
    }

    public List<MainObject> getAppointments(final String id){
        final List<MainObject> mainObjectList = new ArrayList<>();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        switch (type){
            case 0:
                String url = Constants.SHEDULE_PATIENT_GET_URL+id;
                JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response appointment: ", response.toString());
                        for (int i = 0; i <response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                AppiontmentItem appiontmentItem = new AppiontmentItem();
                                appiontmentItem.setAppoint_id("id");
                                appiontmentItem.setDate(object.getString("date"));
                                appiontmentItem.setTime(object.getString("time"));
                                appiontmentItem.setHospital(object.getString("hospital"));
                                appiontmentItem.setTreatment(object.getString("treatment"));
                                appiontmentItem.setPat_id("pat_id");
                                appiontmentItem.setDoc_id("doc_id");
                                appiontmentItem.setStatus("status");
                                mainObjectList.add(appiontmentItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(arrayRequest);
                return mainObjectList;
            case 1:
                String url1 = Constants.SHEDULE_DOCTOR_GET_URL+id;
                JsonArrayRequest arrayRequest2 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i <response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                DoctorAppointmentItem appiontmentItem = new DoctorAppointmentItem();
                                appiontmentItem.setAppoint_id("id");
                                appiontmentItem.setDate(object.getString("date"));
                                appiontmentItem.setTime(object.getString("time"));
                                appiontmentItem.setHospital(object.getString("hospital"));
                                appiontmentItem.setTreatment(object.getString("treatment"));
                                appiontmentItem.setPat_id("pat_id");
                                appiontmentItem.setDoc_id("doc_id");
                                appiontmentItem.setStatus("status");
                                mainObjectList.add(appiontmentItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(arrayRequest2);
                return mainObjectList;
            default:
                Log.d("Unknown id", id);
                return mainObjectList;
        }
    }

}
