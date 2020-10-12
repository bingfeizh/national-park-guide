package com.hfad.nationalparksguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hfad.nationalparksguide.data.model.FirebaseHandler;
import com.hfad.nationalparksguide.data.model.User;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText emailText;
    EditText passwordText;
    Button signupButton;
    TextView loginLink;
    FirebaseAuth mFirebaseAuth;

    DatabaseReference databaseReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        signupButton = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.link_login);

        mFirebaseAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseHandler.getDbReference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // On click go back to login screen
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    /**
     * Sign up.
     * First validates input format. If correct use Firebase email account creation.
     */
    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    onSignupSuccess();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    onSignupFailed();
                }
            }
        });
    }


    /**
     * On register success, store the user email in "Users" collection in Firebase db
     * Go to Login activity.
     */
    public void onSignupSuccess(){
        // Get registered email and put into db
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();

        User curUser = new User(userEmail);

        int charLoc = userEmail.indexOf("@");

        databaseReference.child("Users").child(userEmail.substring(0,charLoc)).setValue(curUser);


        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Toast.makeText(this, "Account created! Login with email.", Toast.LENGTH_SHORT).show();

        //Go back to login screen.
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Make a toast msg indicating sign up failure.
     */
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed. Email address might have been used.", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }


    /**
     * Invoked by SignUp() to validate input format. R
     * @return True if no formatting error
     */
    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Please enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("Password need to be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}