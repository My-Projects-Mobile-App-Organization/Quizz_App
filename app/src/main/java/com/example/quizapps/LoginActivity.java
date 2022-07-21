package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    private TextView forgotPass,btnSignUp;
    private FirebaseAuth mAuth;
    private String emailLoginStr, passLoginStr;
    private Dialog dialogProgress;
    private TextView txtDialog;
    private RelativeLayout signGG;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        edtEmail = (EditText) findViewById(R.id.email);
        edtPass = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login_B);
        btnSignUp = (TextView) findViewById(R.id.sign_B);
        signGG = (RelativeLayout) findViewById(R.id.gg_SignB);



        dialogProgress = new Dialog(LoginActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Signing in.....");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id11))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (validateData()){
                    login();
                }
                
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        signGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
              //  Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        dialogProgress.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();


                            FirebaseUser user = mAuth.getCurrentUser();

                            // check tai khoan gg co phai tk moi ko
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                DbQuery.creaUserData(user.getEmail(), user.getDisplayName(), new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {

                                        DbQuery.loadData(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                dialogProgress.dismiss();

                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                Toast.makeText(LoginActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();
                                                dialogProgress.dismiss();
                                            }
                                        });


                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(LoginActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();
                                        dialogProgress.dismiss();
                                    }
                                });
                            } else {
                                DbQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialogProgress.dismiss();

                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(LoginActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();
                                        dialogProgress.dismiss();
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            dialogProgress.dismiss();
                        }
                    }
                });
    }

    private void login() {
        dialogProgress.show();
        mAuth.signInWithEmailAndPassword(emailLoginStr, passLoginStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();

                            DbQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    dialogProgress.dismiss();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();

                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(LoginActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();
                                    dialogProgress.dismiss();
                                }
                            });


                        } else {
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            dialogProgress.dismiss();
                        }
                    }
                });
    }

    private boolean validateData() {
        boolean status = false;
        emailLoginStr = edtEmail.getText().toString().trim();
        passLoginStr = edtPass.getText().toString().trim();

        if (emailLoginStr.isEmpty()){
            edtPass.setError("Enter Password");
            return false;
        }

        if (passLoginStr.isEmpty()){
            edtEmail.setError("Enter E-Mail ID");
            return false;
        }

        return true;

    }

}