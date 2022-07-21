package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

    private EditText edtPassCur,edtPassNew;
    private Button btnChangPass;
    private Button btnCancel;
    private SharedPreferences sharedPreferences;
    private Dialog dialogProgress;
    private TextView txtDialog;
    private ImageView imgBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        sharedPreferences = getSharedPreferences("datalogin",MODE_PRIVATE);

        dialogProgress = new Dialog(ChangePassActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Update pass.....");

        edtPassCur = findViewById(R.id.edt_Pas_Curr);
        edtPassNew = findViewById(R.id.edt_new_Pass);
        btnChangPass = findViewById(R.id.btnXacNhanChangePass);
        btnCancel = findViewById(R.id.btn_Cancel_changePass);
        imgBackHome = findViewById(R.id.imgB_back_home);

        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassActivity.this.finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassActivity.this.finish();
            }
        });

        btnChangPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassNew.getText().toString().trim() != null && edtPassCur.getText().toString().trim().equals(sharedPreferences.getString("matkhau",""))){
                    changePass(edtPassNew.getText().toString().trim());
                }
                else {
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu mới không được để trống", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void changePass(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dialogProgress.show();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialogProgress.dismiss();
                            Toast.makeText(ChangePassActivity.this, "Update Pass thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}