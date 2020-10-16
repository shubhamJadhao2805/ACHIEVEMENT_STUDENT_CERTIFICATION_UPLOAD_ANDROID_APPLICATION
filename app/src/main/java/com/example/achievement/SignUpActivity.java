package com.example.achievement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.IDN;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {


    EditText id;
    EditText password;
    DatabaseReference reference;
    DatabaseReference reference2;
    Spinner classNameSpinner;
    String[] classNameArray = {"Please select your year","First year","Second year","Third year","Fourt year"};
    EditText nameField;
    EditText rollNoField;
    String classSelected;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_up );

        id = findViewById ( R.id.loginid2 );
        password = findViewById ( R.id.pass2 );
        nameField = findViewById ( R.id.nameTextField );
        rollNoField = findViewById ( R.id.rollNoTextField );
        classNameSpinner = findViewById ( R.id.spinnerClassName );


        reference = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "AuthentiCation" );
        reference2 = FirebaseDatabase.getInstance ().getReference ().child ( "StudentID");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter <> (this,android.R.layout.simple_spinner_item,classNameArray);
        arrayAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        classNameSpinner.setAdapter ( arrayAdapter );
        classNameSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected( AdapterView <?> parent , View view , int position , long id ) {

                classSelected = classNameArray[position];

            }

            @Override
            public void onNothingSelected( AdapterView <?> parent ) {

            }
        } );




    }

    public void signUp( View view ) {


        if (TextUtils.isEmpty ( id.getText ( ) ) && TextUtils.isEmpty ( password.getText ( ) ) && TextUtils.isEmpty ( nameField.getText ()) && TextUtils.isEmpty (rollNoField.getText ()) && TextUtils.isEmpty ( classSelected )) {

            if (TextUtils.isEmpty ( password.getText ( ) )) {

                Toast.makeText ( SignUpActivity.this , "Please enter LogInID" , Toast.LENGTH_SHORT ).show ( );

            } else if (TextUtils.isEmpty ( password.getText ( ) )) {
                Toast.makeText ( SignUpActivity.this , "Please enter Password" , Toast.LENGTH_SHORT ).show ( );

            }else if(TextUtils.isEmpty ( nameField.getText () )){
                Toast.makeText ( SignUpActivity.this , "Please enter name" , Toast.LENGTH_SHORT ).show ( );


            }else if(TextUtils.isEmpty ( rollNoField.getText () )){

                Toast.makeText ( SignUpActivity.this , "Please enter rollNo" , Toast.LENGTH_SHORT ).show ( );

            }else if(TextUtils.isEmpty ( classSelected )){

                Toast.makeText ( SignUpActivity.this , "Please select year" , Toast.LENGTH_SHORT ).show ( );

            }


        } else {

reference2.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
    @Override
    public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

        if(dataSnapshot.hasChild ( id.getText ().toString () )){
            reference.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                    if(dataSnapshot.hasChild ( id.getText ().toString () )){

                        Toast.makeText ( SignUpActivity.this,"User already exists",Toast.LENGTH_SHORT ).show ();

                    }else{
                        DatabaseReference chiled = reference.child ( id.getText ( ).toString ( ) );
                        HashMap <String, String> map = new HashMap <> ( );
                        map.put ( "ID" , id.getText ( ).toString ( ) );
                        map.put ( "password" , password.getText ( ).toString ( ) );
                        map.put ( "name",nameField.getText ().toString () );
                        map.put ( "rollNo",rollNoField.getText ().toString () );
                        map.put ( "class",classSelected );
                        map.put ( "count","0" );
                        chiled.setValue ( map ).addOnCompleteListener ( new OnCompleteListener <Void> ( ) {
                            @Override
                            public void onComplete( @NonNull Task <Void> task ) {

                                if (task.isSuccessful ( )) {

                                    Toast.makeText ( SignUpActivity.this , "LogIn Succesful" , Toast.LENGTH_SHORT ).show ( );

                                } else {


                                    Toast.makeText ( SignUpActivity.this , "LogIn Failed" , Toast.LENGTH_SHORT ).show ( );
                                }

                            }
                        } );


                    }
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError ) {

                }
            } );


        }else{

            Toast.makeText ( SignUpActivity.this,"Please enter valid ID",Toast.LENGTH_SHORT).show ();
        }

    }

    @Override
    public void onCancelled( @NonNull DatabaseError databaseError ) {

    }
} );
        }

    }


        public void logIn(View view){

            Intent intent = new Intent ( SignUpActivity.this , MainActivity.class );
            startActivity ( intent );


        }


}
