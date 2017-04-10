package com.example.angry.project2345;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;

/**
 * Created by angry on 07.04.2017.
 */
public class FragmentSignIn extends DemoParallaxFragment {

    private DemoParallaxAdapter mCatsAdapter;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.sign_in, container, false);
        signInManager = DbFirebaseAuth.signInManager;
        phone = (EditText) v.findViewById(R.id.sign_in_phone);
        password = (EditText) v.findViewById(R.id.sign_in_password);
        trydemo = (Button) v.findViewById(R.id.sign_in_demo);

        btnSignIn = (Button) v.findViewById(R.id.sign_in_go);

        signInManager.init();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getContext(),MapsActivity.class));
            getActivity().finish();
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
        setListeners();
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg == null) return;
                switch (msg.what){
                    case 0: //failure
                        //show that wrong pass and email
                        //do nothing
                        Toast.makeText(getContext(), "failed",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        DbUserData.user = (FirebaseUser) msg.obj;
                        Intent i = new Intent(getContext(),MapsActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(i);
                        getActivity().finish();
                        break;
                    default:
                        break;
                }
            }
        };




        return v;
    }


    public void setListeners(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="",pass="";

                email = phone.getText().toString();
                pass = password.getText().toString();

                if (email.equals("") || pass.equals("")){
                    Toast.makeText(getContext(),"Please fill all the lines", Toast.LENGTH_SHORT).show();
                    return;
                }
                signInManager.SignInGo(email,pass);
            }
        });


        trydemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MapsActivity.class));
            }
        });
    }

    public void setAdapter(DemoParallaxAdapter catsAdapter) {
        mCatsAdapter = catsAdapter;
    }
}
