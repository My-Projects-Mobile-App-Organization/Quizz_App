package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone;
    private Button btnCancel, btnSave;
    private LinearLayout editB;
    private TextView txtProfile;
    private LinearLayout btn_edit_layout;
    private String nameStr, phoneStr;
    private Dialog dialogProgress;
    private TextView txtDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogProgress = new Dialog(MyProfileActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Updating data.....");


        anhXa();

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    saveData();
                }
            }
        });
    }

    private void disableEditing() {
        edtEmail.setEnabled(false);
        edtName.setEnabled(false);
        edtPhone.setEnabled(false);

        btn_edit_layout.setVisibility(View.GONE);

        edtName.setText(DbQuery.myprofileModel.getName());
        edtEmail.setText(DbQuery.myprofileModel.getEmail());

        if (DbQuery.myprofileModel.getPhone() != null)
            edtPhone.setText(DbQuery.myprofileModel.getPhone());

        String profileName = DbQuery.myprofileModel.getName();

        txtProfile.setText(profileName.toUpperCase().substring(0,1));
    }

    private void enableEditing(){
        edtName.setEnabled(true);
        edtPhone.setEnabled(true);

        btn_edit_layout.setVisibility(View.VISIBLE);
    }

    private void anhXa() {
        edtName = findViewById(R.id.mp_name);
        edtEmail = findViewById(R.id.mp_email);
        edtPhone = findViewById(R.id.mp_phone);
        txtProfile = findViewById(R.id.profile_text_name);
        editB = findViewById(R.id.editB);
        btnCancel = findViewById(R.id.cancelB);
        btnSave = findViewById(R.id.saveB);
        btn_edit_layout = findViewById(R.id.btn_edit_layout);
    }

    private boolean validate(){
        nameStr = edtName.getText().toString();
        phoneStr = edtPhone.getText().toString();

        if (nameStr.isEmpty()){
            edtName.setError("Tên không thể bỏ trống");
            return false;
        }
        if (! phoneStr.isEmpty()){
            if ( ! (phoneStr.length() == 10) && (TextUtils.isDigitsOnly(phoneStr))){
                edtPhone.setError("Nhập đúng định dạng số điện thoại");
                return false;
            }
        }
        return true;
    }

    private void saveData(){
        dialogProgress.show();

        if (phoneStr.isEmpty()){
            phoneStr = null;
        }
        DbQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this,"Update thành công",Toast.LENGTH_SHORT).show();
                disableEditing();
                dialogProgress.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Có lỗi gì đó xảy ra vui lòng thử lại",Toast.LENGTH_SHORT).show();
                dialogProgress.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}