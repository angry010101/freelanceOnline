package com.example.angry.project2345;

/**
 * Created by angry on 07.04.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentRegister extends DemoParallaxFragment {

    private DemoParallaxAdapter mCatsAdapter;
    private static Activity thisAct;
    private DbFirebaseAuth.SignIn registerUsrManager;

    private Context context;
    public static Handler h;



    @BindView(R.id.register_phone)
    EditText phoneView;

    @BindView(R.id.register_password)
    EditText passwordView;

    @BindView(R.id.register_go)
    CircularProgressButton startBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.register_act, container, false);
        startBtn = (CircularProgressButton) v.findViewById(R.id.register_go);
        phoneView = (EditText) v.findViewById(R.id.register_phone);
        startBtn.setBackgroundColor(Color.GRAY);
        startBtn.setStrokeColor(Color.WHITE);

        passwordView = (EditText) v.findViewById(R.id.register_password);

        startBtn.setIndeterminateProgressMode(true);
        registerUsrManager = DbFirebaseAuth.signInManager;
        registerUsrManager.init();

        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg==null){
                    Log.d("viewpagerreg", "handleMessage: returned");
                    return;
                }
                switch (msg.what){
                    case 0:
                        //failure
                        Toast.makeText(getContext(), "failed",
                                Toast.LENGTH_SHORT).show();
                        startBtn.setProgress(-1);
                        break;
                    case 1:
                        startBtn.setProgress(100);
                        DbUserData.user = (FirebaseUser) msg.obj;
                        DbFirebase.createNewUser();
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

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="",password="";
                email = phoneView.getText().toString();
                password = passwordView.getText().toString();

                if (email.equals("") || password.equals("")){
                    Toast.makeText(getContext(),"Please fill all the lines", Toast.LENGTH_SHORT).show();
                    return;
                }
                startBtn.setProgress(50);
                registerUsrManager.createUsr(email,password);
            }
        });

      /*  final ImageView image = (ImageView) v.findViewById(R.id.image);

        image.setImageResource(getArguments().getInt("image"));
        image.post(new Runnable() {
            @Override
            public void run() {
                Matrix matrix = new Matrix();
                matrix.reset();

                float wv = image.getWidth();
                float hv = image.getHeight();

                float wi = image.getDrawable().getIntrinsicWidth();
                float hi = image.getDrawable().getIntrinsicHeight();

                float width = wv;
                float height = hv;

                if (wi / wv > hi / hv) {
                    matrix.setScale(hv / hi, hv / hi);
                    width = wi * hv / hi;
                } else {
                    matrix.setScale(wv / wi, wv / wi);
                    height= hi * wv / wi;
                }

                matrix.preTranslate((wv - width) / 2, (hv - height) / 2);
                image.setScaleType(ImageView.ScaleType.MATRIX);
                image.setImageMatrix(matrix);
            }
        });


        TextView text = (TextView)v.findViewById(R.id.name);
        text.setText(getArguments().getString("name"));

        TextView more = (TextView)v.findViewById(R.id.more);

        more.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCatsAdapter != null) {
                    mCatsAdapter.remove(DemoParallaxFragment.this);
                    mCatsAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatsAdapter != null) {
                    int select = (int) (Math.random() * 4);

                    int[] resD = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
                    String[] resS = {"Nina", "Niju", "Yuki", "Kero"};

                    DemoParallaxFragment newP = new DemoParallaxFragment();
                    Bundle b = new Bundle();
                    b.putInt("image", resD[select]);
                    b.putString("name", resS[select]);
                    newP.setArguments(b);
                    mCatsAdapter.add(newP);
                }
            }
        });

        */
        return v;
    }

    public void setAdapter(DemoParallaxAdapter catsAdapter) {
        mCatsAdapter = catsAdapter;
    }
}