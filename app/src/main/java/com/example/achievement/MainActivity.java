package com.example.achievement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    EditText logInId;
    EditText password;
    DatabaseReference reference;
    ProgressBar progressBar;
    View progressBarView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String isAcadmicYearChange;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        isAcadmicYearChange = "No";
        logInId = findViewById ( R.id.logInID );
        password = findViewById ( R.id.pass2 );
        reference = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "AuthentiCation" );
        progressBar = findViewById ( R.id.progressBar1 );
        progressBarView = findViewById ( R.id.progressBarView );
        preferences = getApplicationContext ( ).getSharedPreferences ( "MyPref" , 0 );
        editor = preferences.edit ( );
        ConnectivityManager connectivityManager = ( ConnectivityManager ) getSystemService ( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo ( );
        if (networkInfo != null) {


            try {


                DatabaseReference reference3 = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "IsAcademic Year Change" );
                reference3.addValueEventListener ( new ValueEventListener ( ) {
                    @Override
                    public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                        try {
                            isAcadmicYearChange = dataSnapshot.getValue ( ).toString ( );
                            String isUserLogIn = preferences.getString ( "isUserLogIn" , null );


                            if (isUserLogIn.equals ( "Yes" )) {

                                if (isAcadmicYearChange.equals ( "No" )) {

                                    DataModel.count = Integer.parseInt ( Objects.requireNonNull ( preferences.getString ( "count" , null ) ) );
                                    DataModel.nameOfStudent = preferences.getString ( "name" , null );
                                    DataModel.rollNo = preferences.getString ( "rollNo" , null );
                                    DataModel.ID = preferences.getString ( "ID" , null );

                                    if (DataModel.ID != null) {
                                        getData ( );
                                        Intent intent = new Intent ( MainActivity.this , FileSelectPage.class );
                                        startActivity ( intent );
                                        Toast.makeText ( MainActivity.this , "LogIn" , Toast.LENGTH_SHORT ).show ( );
                                    }

                                } else {


                                    Toast.makeText ( MainActivity.this , "Please Update your Profile" , Toast.LENGTH_SHORT ).show ( );
                                    dialogBox ( preferences.getString ( "ID" , null ) );


                                }
                            }

                        } catch (Exception e) {
                            System.out.print ( "Exception" );
                        }
                    }

                    @Override
                    public void onCancelled( @NonNull DatabaseError databaseError ) {

                    }
                } );


                String isUserLogIn = preferences.getString ( "isUserLogIn" , null );

            } catch (Exception e) {

                System.out.print ( e );

            }


        } else {

            Toast.makeText ( MainActivity.this , "Please check your Internet" , Toast.LENGTH_SHORT ).show ( );

        }
    }

    //Method to LogIn

    public void logIn( View view ) {

        if (TextUtils.isEmpty ( logInId.getText ( ) ) && TextUtils.isEmpty ( password.getText ( ) ) && isAcadmicYearChange.equals ( "Yes" )) {

            if (TextUtils.isEmpty ( logInId.getText ( ) )) {

                Toast.makeText ( MainActivity.this , "Please enter LogInID" , Toast.LENGTH_SHORT ).show ( );

            } else if (TextUtils.isEmpty ( password.getText ( ) )) {
                Toast.makeText ( MainActivity.this , "Please enter Password" , Toast.LENGTH_SHORT ).show ( );

            } else if (isAcadmicYearChange.equals ( "Yes" )) {

                Toast.makeText ( MainActivity.this , "Please Update Your Profile!" , Toast.LENGTH_SHORT ).show ( );

            }


        } else {

            //Perform LogIn
            try {

                progressBarView.setVisibility ( View.VISIBLE );
                progressBar.setVisibility ( View.VISIBLE );
                DatabaseReference reference1 = reference.child ( logInId.getText ( ).toString ( ) );
                reference1.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
                    @Override
                    public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                        try {
                            HashMap <String, String> map = ( HashMap ) dataSnapshot.getValue ( );
                            if (map.get ( "password" ).equals ( password.getText ( ).toString ( ) )) {

                                DataModel.className = map.get ( "class" );
                                DataModel.nameOfStudent = map.get ( "name" );
                                DataModel.rollNo = map.get ( "rollNo" );
                                DataModel.count = Integer.parseInt ( map.get ( "count" ) );
                                DataModel.ID = map.get ( "ID" );
                                getData ( );
                                editor.putString ( "ID" , DataModel.ID );
                                editor.putString ( "isUserLogIn" , "Yes" );
                                editor.putString ( "rollNo" , DataModel.rollNo );
                                editor.putString ( "name" , DataModel.nameOfStudent );
                                editor.putString ( "count" , String.valueOf ( DataModel.count ) );
                                editor.putString ( "pass" , String.valueOf ( map.get ( "password" ) ) );
                                editor.commit ( );
                                progressBar.setVisibility ( View.INVISIBLE );
                                progressBarView.setVisibility ( View.INVISIBLE );
                                Intent intent = new Intent ( MainActivity.this , FileSelectPage.class );
                                startActivity ( intent );


                            } else {

                                progressBar.setVisibility ( View.INVISIBLE );
                                progressBarView.setVisibility ( View.INVISIBLE );
                                Toast.makeText ( MainActivity.this , "Password is incorrect" , Toast.LENGTH_SHORT ).show ( );


                            }
                        } catch (Exception e) {

                            progressBar.setVisibility ( View.INVISIBLE );
                            progressBarView.setVisibility ( View.INVISIBLE );
                            Toast.makeText ( MainActivity.this , "User not exist" , Toast.LENGTH_LONG ).show ( );
                        }

                    }

                    @Override
                    public void onCancelled( @NonNull DatabaseError databaseError ) {

                        progressBarView.setVisibility ( View.INVISIBLE );
                        progressBar.setVisibility ( View.INVISIBLE );

                    }
                } );

            } catch (Exception e) {

                Toast.makeText ( MainActivity.this , e.getMessage ( ) , Toast.LENGTH_LONG ).show ( );
                progressBar.setVisibility ( View.INVISIBLE );
                progressBarView.setVisibility ( View.INVISIBLE );

            }


        }
    }


    //Method to go to signUP page

    public void signUp( View view ) {

        Intent intent = new Intent ( MainActivity.this , SignUpActivity.class );
        startActivity ( intent );
    }


    public void direact( View view ) {

        Intent intent = new Intent ( MainActivity.this , FileSelectPage.class );
        startActivity ( intent );

    }


    //function to retrive Data For COunts

    void getData() {


        final DatabaseReference database2 = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "ExcelSheet" ).child ( DataModel.ID );
        database2.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                try {
                    HashMap <String, String> map = ( HashMap ) dataSnapshot.getValue ( );
                    DataModel.className = map.get ( "A-Class" );
                    DataModel.rollNo = map.get ( "B-Roll No" );
                    DataModel.nameOfStudent = map.get ( "C-Name" );
                    DataModel.countAcadmic = Integer.parseInt ( map.get ( "D-Academic" ) );
                    DataModel.countSport = Integer.parseInt ( map.get ( "E-Sport" ) );
                    DataModel.countTech = Integer.parseInt ( map.get ( "F-Technical" ) );
                    DataModel.countOther = Integer.parseInt ( map.get ( "G-Other" ) );
                    DataModel.count = Integer.parseInt ( map.get ( "Total" ) );

                } catch (Exception e) {

                    System.out.print ( "Exception" );


                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );
    }


    void dialogBox( String ID ) {

        final String[] classNameArray = {"First year" , "Second year" , "Third year" , "Fourth year"};
        final List <String> classList = Arrays.asList ( classNameArray );
        final boolean[] initialValue = new boolean[]{
                true ,
                false ,
                false ,
                false

        };


        Toast.makeText ( MainActivity.this , "DialogISCall" , Toast.LENGTH_SHORT ).show ( );


        final EditText rollNoChange = new EditText ( MainActivity.this );
        rollNoChange.setHint ( "Enter Roll No" );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams ( LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT );
        rollNoChange.setLayoutParams ( params );
        final DatabaseReference database2 = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "ExcelSheet" ).child ( ID );
        final DatabaseReference reference4 = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "AuthentiCation" ).child ( ID );
        AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity.this );
        builder.setTitle ( "Academic year change! please Update your Information" );
        builder.setView ( rollNoChange );


        builder.setMultiChoiceItems ( classNameArray , initialValue , new DialogInterface.OnMultiChoiceClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog , int which , boolean isChecked ) {


initialValue[which] = isChecked;
Toast.makeText ( MainActivity.this,classList.get ( which ),Toast.LENGTH_SHORT ).show ();
DataModel.classSelectedTempar = classList.get ( which );


            }
        } );


        builder.setPositiveButton ( "Save" , new DialogInterface.OnClickListener ( ) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick( DialogInterface dialog , int which ) {

                try {

                    progressBar.setVisibility ( View.VISIBLE );
                    progressBarView.setVisibility ( View.VISIBLE );

                    database2.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                            DataModel.excelSheet = (HashMap<String , String>) dataSnapshot.getValue ();
                            Toast.makeText ( MainActivity.this,DataModel.excelSheet.get ( "A-Class"),Toast.LENGTH_SHORT ).show ();
                            DataModel.excelSheet.put ( "B-Roll No",rollNoChange.getText ().toString () );
                            DataModel.excelSheet.put ( "A-Class",DataModel.classSelectedTempar);

                            database2.setValue ( DataModel.excelSheet ).addOnCompleteListener ( new OnCompleteListener <Void> ( ) {
                                @Override
                                public void onComplete( @NonNull Task<Void> task ) {
                                    if(task.isSuccessful ()){

                                        Toast.makeText ( MainActivity.this,"Succesful",Toast.LENGTH_SHORT ).show ();
                                        progressBar.setVisibility ( View.INVISIBLE );
                                        progressBarView.setVisibility ( View.INVISIBLE );
                                    }
                                }
                            } );

                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError databaseError ) {

                        }
                    } );

                    reference4.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {


                            DataModel.authen = (HashMap<String , String>) dataSnapshot.getValue ();

                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError databaseError ) {

                        }
                    } );








                }catch (Exception e){

Toast.makeText ( MainActivity.this,"Exception",Toast.LENGTH_SHORT ).show ();

                }



            }


        } );


        builder.setNegativeButton ( "Cancel" , new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog , int which ) {

dialog.cancel ();


            }
        } );

        AlertDialog alertDialog = builder.create ( );
        alertDialog.show ( );




    }

}