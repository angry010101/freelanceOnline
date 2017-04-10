package com.example.angry.project2345;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by S1 on 2/14/2017.
 */
public class AddTaskActivity extends Activity {
    EditText name,desc,curr;
    Button sendbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        name = (EditText) findViewById(R.id.title_add_task);
        desc = (EditText) findViewById(R.id.add_task_desc);
        curr = (EditText) findViewById(R.id.add_task_currency);
        sendbtn = (Button) findViewById(R.id.sendTask);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n,d,c;
                n = name.getText().toString();
                d = desc.getText().toString();
                c = curr.getText().toString();

                if (n.equals("") || d.equals("") || c.equals(""))
                    return;
                DbFirebase.addTaskInfo(n, d, c);
                Toast.makeText(getApplicationContext(),
                        "Your suggestion has beed added",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
