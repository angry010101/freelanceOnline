package com.example.angry.project2345;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angry on 13.03.2017.
 */

public class SignInAct extends AppCompatActivity {

    private static Activity activity;
    private static Context context;

    private DbFirebaseAuth.SignIn signInManager;

    @BindView(R.id.sign_in_go)
    Button btnSignIn;

    @BindView(R.id.sign_in_phone)
    EditText phone;

    @BindView(R.id.sign_in_password)
    EditText password;

    @BindView(R.id.sign_in_demo)
    Button trydemo;

    public static Handler h;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        context = this;
        setContentView(R.layout.sign_in);

        signInManager = DbFirebaseAuth.signInManager;

        signInManager.init();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,MapsActivity.class));
            finish();
        }

        try {
            String s = GetMyInfo.getPhone();
            if (s != null && !s.equals("")){
                phone.setText(s);
                phone.setActivated(false);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     //   setListeners();
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg == null) return;
                switch (msg.what){
                    case 0: //failure
                        //show that wrong pass and email
                        //do nothing
                        Toast.makeText(getApplicationContext(), "failed",
                                        Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        DbUserData.user = (FirebaseUser) msg.obj;
                        Intent i = new Intent(getApplicationContext(),MapsActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(i);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };


    }

    public static Activity getActivity(){
        return activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        signInManager.addListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        signInManager.removeListener();
    }







}
