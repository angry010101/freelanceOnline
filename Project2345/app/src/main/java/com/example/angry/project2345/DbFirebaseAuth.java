package com.example.angry.project2345;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by angry on 13.03.2017.
 */

public class DbFirebaseAuth {
    static final String TAG="AUTH";

    public static SignIn signInManager = new SignIn();


    public static class SignIn{
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;

        public FirebaseAuth getAuth(){
            return mAuth;
        }

        public void init(){
            mAuth= FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        //startActivity Maps Activity
                        /*Handler h = SignInAct.h;
                        h.sendMessage(h.obtainMessage(1,user.getUid()));*/
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };

        }


        public void addListener(){
            mAuth.addAuthStateListener(mAuthListener);
        }

        public void removeListener(){
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }

        public void SignInGo(String email, String password){
            mAuth.signInWithEmailAndPassword(email+"@angry.com", password)
                    .addOnCompleteListener(ActivityLogInRegister.activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            Handler h = FragmentSignIn.h;
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {

                                h.sendMessage(h.obtainMessage(0,task.getException()));
                                Log.w(TAG, "signInWithEmail:failed", task.getException());

                            }
                            else{
                                h.sendMessage(h.obtainMessage(1,task.getResult().getUser()));
                            }

                            // ...
                        }
                    });
        }

        public void SignOut(){
            if (mAuth != null){
                mAuth.signOut();
            }
        }


        public void createUsr(String email, String password){
            if (ActivityLogInRegister.activity == null){
                Log.d(TAG, "createUsr: act == null");
                return;
            }
            mAuth.createUserWithEmailAndPassword(email+"@angry.com", password)
                    .addOnCompleteListener(ActivityLogInRegister.activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            Handler h = FragmentRegister.h;
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                /*Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();*/
                                h.sendEmptyMessage(0);
                                Log.w(TAG, "onComplete: " + task.getException());
                            }
                            else {
                                h.sendMessage(h.obtainMessage(1,task.getResult().getUser()));
                            }
                            // ...
                        }
                    });
        }
    }
}
