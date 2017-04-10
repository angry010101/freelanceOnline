package com.example.angry.project2345;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by S1 on 2/27/2017.
 */
public class MyTasksActivity extends AppCompatActivity {
    public static Handler h;

    @BindView(R.id.mytasksaddbtn)
    Button btnaddtask;
    @BindView(R.id.mytasksremovebtn)
    Button btnremovetask;
    @BindView(R.id.mytasksprogressBar)
    ProgressBar pb;
    @BindView(R.id.mytaskslistview)
    ListView lvSimple;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tasks_view_act);
        ButterKnife.bind(this);


        btnremovetask.setVisibility(View.INVISIBLE);
        btnaddtask.setVisibility(View.INVISIBLE);

        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("blabla", "handleMessage: handlemessage started");
                pb.setVisibility(View.INVISIBLE);
                UserInfoClass.TaskInfo taskInfo = (UserInfoClass.TaskInfo) msg.obj;
                if (msg.obj == null || ((UserInfoClass.TaskInfo) msg.obj).getName().equals("")){
                    lvSimple.setVisibility(View.INVISIBLE);
                    btnaddtask.setVisibility(View.VISIBLE);
                    btnremovetask.setVisibility(View.INVISIBLE);
                    btnaddtask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), AddTaskActivity.class));
                            finish();
                        }
                    });
                    return;
                }
                btnaddtask.setVisibility(View.INVISIBLE);
                lvSimple.setVisibility(View.VISIBLE);
                btnremovetask.setVisibility(View.VISIBLE);
                btnremovetask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DbFirebase.removeTask(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(MapsActivity.getContext(),
                                R.string.mytaskstaskremoved, Toast.LENGTH_LONG);
                        finish();
                    }
                });


                Log.d("blabla", "handleMessage: handlemessage taskinfo "+ taskInfo.equals(null));
                final String ATTRIBUTE_NAME_TEXT = "title";
                final String ATTRIBUTE_NAME_DESC = "desc";
                final String ATTRIBUTE_NAME_PAYM = "payment";
                final String ATTRIBUTE_NAME_DATE = "date";
/*
                String[] texts = { "sometext 1", "sometext 2", "sometext 3",
                        "sometext 4", "sometext 5" };
                boolean[] checked = { true, false, false, true, false };
             //   int img = R.drawable.ic_launcher;*/



                ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(1);
                Map<String, Object> m;
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_TEXT, taskInfo.getName());
                    m.put(ATTRIBUTE_NAME_DESC, taskInfo.getDescription());
                    m.put(ATTRIBUTE_NAME_PAYM, taskInfo.getReward());
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                    m.put(ATTRIBUTE_NAME_DATE, sdf.format(taskInfo.getDate()));
//                    m.put(ATTRIBUTE_NAME_IMAGE, img);
                    data.add(m);


                // массив имен атрибутов, из которых будут читаться данные
                String[ ] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_DESC,
                        ATTRIBUTE_NAME_PAYM, ATTRIBUTE_NAME_DATE};
                // массив ID View-компонентов, в которые будут вставлять данные
                int[] to = { R.id.mytaskviewelementtitle, R.id.mytaskviewelementdesc,
                        R.id.mytaskviewelementpay,R.id.mytaskviewelementdate };

                // создаем адаптер
                SimpleAdapter sAdapter = new SimpleAdapter(MapsActivity.getContext()
                        , data, R.layout.my_task_view_list_element,
                        from, to);

                // определяем список и присваиваем ему адаптер
                lvSimple.setAdapter(sAdapter);
                sAdapter.notifyDataSetChanged();
            }
        };
        DbFirebase.getMyTasks();
    }


}
