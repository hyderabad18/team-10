package com.cfg.iandeye.volunter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cfg.iandeye.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class Volunteer_Registration extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer__registration);
        showSignInScreen();
    }

    private void showSignInScreen() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.common_google_signin_btn_icon_dark)
                        .setTosUrl("http://salcit.in")
                        .setTheme(R.style.AppTheme)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()

                        ))
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        SharedPreferences preferences= getSharedPreferences("profile",0);
        SharedPreferences.Editor editor=preferences.edit();
        String UID = "";

        /* Error here if there is no network while first installation add network check here */
        try {
            if (!user.getEmail().equals(null))
                UID = user.getEmail();
            else
                UID = user.getPhoneNumber();
        }catch (Exception e)
        {
            e.getLocalizedMessage();
            return;
        }

        editor.putString("UID",UID);
        editor.apply();

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Intent intent = new Intent(Volunteer_Registration.this,File_Upload_Activity.class);
                startActivity(intent);

            }
            Log.e("Login","Unknown sign in response");
        }
    }

}
