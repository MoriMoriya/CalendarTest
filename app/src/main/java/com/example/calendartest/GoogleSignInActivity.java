package com.example.calendartest;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class GoogleSignInActivity extends MainActivity implements
        View.OnClickListener {

    public static GoogleSignInAccount mAccount;
    public static AuthCredential ac;

    private static final String TAG ="GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    public static FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    String signed_out = "SignedOut";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch(ApiException e){
                Log.w(TAG,"Google sign in failed",e);
                updateUI(null);
            }
        }
    }
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG,"firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAccount = acct;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.w(TAG,"signInWithCredential:failure",task.getException());
                            Toast.makeText(GoogleSignInActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }


    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    private void signOut(){
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }
    private void revokeAccess(){
        mAuth.signOut();
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            mStatusTextView.setText(user.getEmail());
            mDetailTextView.setText(user.getDisplayName());

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
        }else{
            mStatusTextView.setText(signed_out);
            mDetailTextView.setText(signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            findViewById(R.id.disconnect_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }
}
