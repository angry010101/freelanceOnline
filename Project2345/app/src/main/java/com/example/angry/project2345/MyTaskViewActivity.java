package com.example.angry.project2345;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by S1 on 2/21/2017.
 */
public class MyTaskViewActivity extends AppCompatActivity {
    EditText desc,title,reward;
    Button btnsave,btnRemove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_task_view_act);

        desc = (EditText) findViewById(R.id.myTaskViewDesc);
        title = (EditText) findViewById(R.id.myTaskViewTitle);
        reward = (EditText) findViewById(R.id.myTaskViewCurr);
        btnsave = (Button) findViewById(R.id.myTaskViewSavebtn);
        btnRemove = (Button) findViewById(R.id.myTaskViewRemoveTaskBtn);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sdesc,stitle,scurr,spay;
                sdesc = desc.getText().toString();
                stitle = title.getText().toString();
                scurr = reward.getText().toString();

                if (sdesc.equals("") ||
                        stitle.equals("") ||
                        scurr.equals("")) {
                    Toast.makeText(MapsActivity.getContext(),
                            "You should fill text fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                        DbFirebase.addTaskInfo(stitle,sdesc,scurr);
                        Toast.makeText(getApplicationContext(),
                                "Your task has been added",
                                Toast.LENGTH_SHORT).show();
                        finish();
            }
        });


        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DbFirebase.removeTask(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                });
                t.start();
                Toast.makeText(getApplicationContext(),
                        "Your task has been removed",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private void getAndSetData(){
        DbFirebase.setTaskListener(DbFirebase.key,"0");
        UserInfoClass.TaskInfo t = DbFirebase.getInfoUserData();
        title.setText(t.getName());
        desc.setText(t.getDescription());
        reward.setText(t.getReward());
    }
}
