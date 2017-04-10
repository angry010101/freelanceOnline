package com.example.angry.project2345;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by S1 on 3/6/2017.
 */
public class Settings_act extends AppCompatActivity {

    @BindView(R.id.settings_act_btn_save)
    Button btnsave;
    @BindView(R.id.settings_act_edit_new_pass)
    EditText newpass;
    @BindView(R.id.settings_act_edit_curr_pass)
    EditText currpass;
    FirebaseUser user;
    String newPass="";
    String cpass="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);

        ButterKnife.bind(this);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPass = newpass.getText().toString();
                cpass = currpass.getText().toString();
                if (!newPass.equals("") ||
                        !cpass.equals("") ){


                    user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(),cpass);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Password has been changed",
                                                            Toast.LENGTH_SHORT).show();
                                                            finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Error password not updated",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Error password not updated",
                                                Toast.LENGTH_SHORT).show();
                                        Log.w("changepass", "onComplete: ",task.getException());
                                    }
                                }
                            });

                    return;
                }
                //change
             /*   Toast.makeText(getApplicationContext(),
                        "OK",
                        Toast.LENGTH_SHORT).show();*/
                finish();
            }
        });
    }
}
