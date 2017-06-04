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

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Mike on 5/28/2017.
 */

public class ProcessUser {

    private static ProcessUser processUser;
    private final Context context;
    private  WorkerInterface workerInterface;
    private int type;
    private  MainObject mainObject;
    private final Activity activity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static ProcessUser getNewInstance(Context context, Activity activity, WorkerInterface wo){
        if (processUser != null){
            processUser = new ProcessUser(context, activity, wo);
        }
        return processUser;
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

    public boolean confirmDetails(String email, String password){
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
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("type", type);
                            activity.startActivity(intent);;
                            activity.finish();
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

    private void registerUser(){

        switch (userType(mainObject)) {
            case 0:

                final PatientItem patientItem = (PatientItem) mainObject;

                Log.d("item",patientItem.getName()+patientItem.getEmail()+patientItem.getLocation());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.PATIENT_REGISTER_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RegisterUser","response");
                        try {
                            JSONObject jsonObject = response;
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
                        Log.d("RegisterUser",error.getLocalizedMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pat_name", patientItem.getName());
                        params.put("pat_email", patientItem.getEmail());
                        params.put("pat_password", patientItem.getPassword());
                        params.put("pat_location", patientItem.getLocation());
                        return params;
                    }
                };


//                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PATIENT_REGISTER_URL,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("RegisterUser","response");
//                                try {
//                                    JSONArray jresponse = new JSONArray(response);
//                                    JSONObject jsonObject = jresponse.getJSONObject(0);
//                                    if (jsonObject.getBoolean("error"))
//                                        processStatus(true);
//                                    else
//                                        processStatus(false);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("RegisterUser","response");
//                                processStatus(false);
//                            }
//                        }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("pat_name", patientItem.getName());
//                        params.put("pat_email", patientItem.getEmail());
//                        params.put("pat_password", patientItem.getPassword());
//                        params.put("pat_location", patientItem.getLocation());
//                        return params;
//                    }
//
//                };
                final RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsonObjectRequest);
            break;

            case 1:
                final DoctorItem doctorItem = (DoctorItem) mainObject;


                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Constants.DOCTOR_REGISTER_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RegisterUser","response");
                        try {
                            JSONObject jsonObject = response;
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
                requestQueue1.add(jsonObjectRequest1);

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

}
