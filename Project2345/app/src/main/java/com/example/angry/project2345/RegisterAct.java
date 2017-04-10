package com.example.angry.project2345;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by angry on 13.03.2017.
 */

public class RegisterAct extends AppCompatActivity {

    private static Activity thisAct;
    private DbFirebaseAuth.SignIn registerUsrManager;


    @BindView(R.id.register_phone)
    EditText phoneView;

    @BindView(R.id.register_password)
    EditText passwordView;

    @BindView(R.id.register_go)
    Button startBtn;


    private Context context;
    public static Handler h;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_act);

        ButterKnife.bind(this);
        thisAct = this;
        context = this;
  /*      registerUsrManager = DbFirebaseAuth.signInManager;
        registerUsrManager.init();

        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg==null) return;
                switch (msg.what){
                    case 0:
                        //failure
                        Toast.makeText(getApplicationContext(), "failed",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        DbUserData.user = (FirebaseUser) msg.obj;
                        DbFirebase.createNewUser();
                        Intent i = new Intent(getApplicationContext(),MapsActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        };

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="",password="";
                email = phoneView.getText().toString();
                password = passwordView.getText().toString();

                if (email.equals("") || password.equals("")){
                    Toast.makeText(context,"Please fill all the lines", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUsrManager.createUsr(email,password);
            }
        });*/
    }

    public static Activity getThisAct() {
        return thisAct;
    }
}
