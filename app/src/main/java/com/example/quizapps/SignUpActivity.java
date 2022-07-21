package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, pass, confirmPass;
    private Button btnSignUp;
    private ImageView btnBack;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog dialogProgress;
    private TextView txtDialog;
    public static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreferences=getSharedPreferences("datalogin",MODE_PRIVATE);

        anhXa();
        eventBtn();

        mAuth = FirebaseAuth.getInstance();
        dialogProgress = new Dialog(SignUpActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Registering User.....");
    }

    private void eventBtn() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    signUpNewUser();
                }

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean validate(){
        emailStr = email.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();
        nameStr = name.getText().toString().trim();

        if (emailStr.isEmpty()){
            email.setError("Enter Email ID");
            return false;
        }

        if (passStr.isEmpty()){
            pass.setError("Enter Your Password");
            return false;
        }

        if (confirmPassStr.isEmpty()){
            confirmPass.setError("Enter Confirm Password");
            return false;
        }
        if (nameStr.isEmpty()){
            name.setError("Enter Your Name");
            return false;
        }

        if (passStr.compareTo(confirmPassStr) != 0){
            Toast.makeText(this,"Mật khẩu và xác nhận mật khẩu phải trùng nhau",Toast.LENGTH_SHORT).show();
            return false;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("taikhoan",emailStr);
        editor.putString("matkhau",passStr);
        editor.commit();

        return true;
    }

    private void signUpNewUser(){
        dialogProgress.show();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();

                            DbQuery.creaUserData(emailStr, nameStr, new MyCompleteListener(){
                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            dialogProgress.dismiss();
                                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this,"Da co loi xay ra, Vui long thu lai",Toast.LENGTH_SHORT).show();
                                            dialogProgress.dismiss();
                                        }
                                    });


                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignUpActivity.this,"Da co loi xay ra, Vui long thu lai",Toast.LENGTH_SHORT).show();
                                    dialogProgress.dismiss();
                                }
                            });


                        } else {
                            Toast.makeText(SignUpActivity.this,"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                            dialogProgress.dismiss();
                        }
                    }
                });
    }

    private void anhXa() {
        name = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.emailID);
        pass = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirm_pass);
        btnSignUp = (Button) findViewById(R.id.signup_B);
        btnBack = (ImageView) findViewById(R.id.backB);

    }
}