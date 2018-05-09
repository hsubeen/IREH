package com.example.ghd_t.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    @Override
    public void onStart() {
        super.onStart();
        // 현재 로그인되어있는 유저 확인
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn_google= (Button) findViewById(R.id.login_google);
        Button logout_btn_google = (Button) findViewById(R.id.logout_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("238952738543-29o961bu98e51mu2saejl6nb95qd870m.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //이미 로그인 되어있는 상태라면
        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();

            //로그인화면 버튼에 현재 로그인되어있는 계정 표시하기
            String email = user.getEmail() + "으로 로그인";
            login_btn_google.setText(email);

            login_btn_google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("알림", "구글 LOGIN");
                    //로그인버튼 클릭시, 로그인되어있는 계정 그대로


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            });
        }else {
            //로그인되어있지 않은 상태라면
            login_btn_google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("알림", "구글 LOGIN");
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        }


        logout_btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.v("알림", "구글 LOGOUT");
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext(),R.style.MyAlertDialogStyle);
                alt_bld.setTitle("로그아웃").setIcon(R.drawable.check_dialog_64).setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                        .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 네 클릭
                                // 로그아웃 함수 call
                                signOut();
                        }}).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 아니오 클릭. dialog 닫기.
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //정보를 받아와 firebaseAuthWithGoogle 에서 인증함
            if (result.isSuccess()) {
                //Google SignIn 성공

                progressDialog = new ProgressDialog(LoginActivity.this,R.style.MyAlertDialogStyle);
                progressDialog.setMessage("환영합니다");
                progressDialog.show();

                Log.v("알림", "google sign 성공, FireBase와 Auth.");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
               //Google SignIn 실패
                Log.v("알림", result.isSuccess() +" Google Sign In failed. Because : " + result.getStatus().toString());
                if(result.getStatus().getStatusCode() == 7){
                    //NETWORK_ERROR
                    Toast.makeText(LoginActivity.this, "네트워크를 연결상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("알림", "Google Auth와 Firebase 통신");
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.v("알림", "Firebase 인증 실패");
                            Toast.makeText(LoginActivity.this, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }else {


                            Log.v("알림", "Firebase 인증 성공");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String cu = mAuth.getUid();
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String photoUrl = user.getPhotoUrl().toString();
                            String phone = user.getPhoneNumber();

                            Log.v("알림", "현재로그인한 유저 " + cu);
                            Log.v("알림", "현재로그인한 이메일 " + email);
                            Log.v("알림", "유저 이름 " + name);
                            Log.v("알림", "유저 사진 " + photoUrl);
                            Log.v("알림", "유저 폰 " + phone);
                            //환영합니다 창 삭제
                            progressDialog.dismiss();
                            UserData userdata = new UserData(name, photoUrl);
                            mDatabase.child("Users").child(cu).setValue(userdata);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "FireBase 아이디 생성이 완료 되었습니다", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

     }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("알림", "onConnectionFailed");
    }


    public void signOut() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mAuth.signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.v("알림", "로그아웃 성공");
                                setResult(1);
                            } else {
                                setResult(0);
                            }
                            //로그아웃 후 app 종료
                            finish();
                        }
                    });
                }
            }
            @Override
            public void onConnectionSuspended(int i) {
                Log.v("알림", "Google API Client Connection Suspended");
                setResult(-1);
                finish();
            }
        });
    }



}
