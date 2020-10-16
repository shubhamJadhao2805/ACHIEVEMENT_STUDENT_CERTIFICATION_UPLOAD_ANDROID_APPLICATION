package com.example.achievement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class FileSelectPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Spinner typeOfCertificateSpinner;
    Spinner categoOFSpinner;
    String[] typeArray = {"State","National","International","Other"};
    String[] cateArray = {"Category of certificate ","Sport","Academic","Technical","Other"};
    TextView nameOfUser;
    TextView rollNo;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_file_select_page );
        Toolbar toolbar = ( Toolbar ) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        FloatingActionButton fab = ( FloatingActionButton ) findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick( View view ) {
                Snackbar.make ( view , "Replace with your own action" , Snackbar.LENGTH_LONG )
                        .setAction ( "Action" , null ).show ( );
            }
        } );

        DrawerLayout drawer = ( DrawerLayout ) findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this , drawer , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ( );

        NavigationView navigationView = ( NavigationView ) findViewById ( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener ( this );








        DatabaseReference reference = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "AcademicYear" );
        reference.addValueEventListener ( new ValueEventListener ( ) {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {

                try {


                    DataModel.year = dataSnapshot.getValue ( ).toString ( );
                } catch (Exception e) {

                    System.out.print ( "Exception" );

                }

            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError ) {

            }
        } );







        categoOFSpinner = findViewById ( R.id.spinner5);
        typeOfCertificateSpinner = findViewById ( R.id.spinner4 );
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter <> ( this,android.R.layout.simple_spinner_item,typeArray );
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter <> ( this,android.R.layout.simple_spinner_item,cateArray );
        arrayAdapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        arrayAdapter2.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        typeOfCertificateSpinner.setAdapter (arrayAdapter);
        categoOFSpinner.setAdapter ( arrayAdapter2 );

        typeOfCertificateSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected( AdapterView <?> parent , View view , int position , long id ) {
                if(position != 0) {
                    DataModel.typeOfCertificate = typeArray[position];
                    DataModel.typeOfCertiInt = position;
                }
            }

            @Override
            public void onNothingSelected( AdapterView <?> parent ) {

            }
        } );


        categoOFSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected( AdapterView <?> parent , View view , int position , long id ) {
                if (position != 0) {
                    DataModel.cateOfCertificate = cateArray[position];
                    DataModel.catiOfCertiInt = position;
                }

            }

            @Override
            public void onNothingSelected( AdapterView <?> parent ) {

            }
        } );


        try {


            rollNo = findViewById ( R.id.rollNo2 );
            nameOfUser = findViewById ( R.id.nameOfUser );

            rollNo.setText ( DataModel.rollNo );
            nameOfUser.setText ( DataModel.nameOfStudent );
        }catch (Exception e){

System.out.print ( "Exception" );
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = ( DrawerLayout ) findViewById ( R.id.drawer_layout );
        if (drawer.isDrawerOpen ( GravityCompat.START )) {
            drawer.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ( );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ( ).inflate ( R.menu.file_select_page , menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ( );

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected ( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId ( );

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {




        } else if (id == R.id.nav_send) {

            preferences = getApplicationContext ( ).getSharedPreferences ( "MyPref" , 0 );
            editor = preferences.edit ( );
            editor.clear ();
            editor.putString ( "isUserLogIn","No" );
            editor.commit ();
            Toast.makeText ( FileSelectPage.this,"LogOut Succesful",Toast.LENGTH_SHORT ).show ();
            Intent intent = new Intent ( FileSelectPage.this,MainActivity.class );
            startActivity ( intent );

        }

        DrawerLayout drawer = ( DrawerLayout ) findViewById ( R.id.drawer_layout );
        drawer.closeDrawer ( GravityCompat.START );
        return true;
    }




    public void selectFile(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setMessage ( "Select type of File" );
        builder.setCancelable ( true );
        builder.setPositiveButton ( "JPG" , new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog , int which ) {

                DataModel.uploadDataType = 0;
                Intent intent2 = new Intent ( );
                intent2.setType ("image/*");
                intent2.setAction (Intent.ACTION_GET_CONTENT);
                startActivityForResult (Intent.createChooser (intent2, "Select Picture"), PICK_IMAGE);


            }
        } );

        builder.setNegativeButton ( "PDF" , new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog , int which ) {
                DataModel.uploadDataType = 1;
                Intent intent = new Intent ( );
                intent.setType ("*/*");
                intent.setAction (Intent.ACTION_GET_CONTENT);
                startActivityForResult (intent, 2);

            }
        } );


        AlertDialog alert11 = builder.create();
        alert11.show();

    }



    //Activity When File is Selected


    @Override
    protected void onActivityResult( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult ( requestCode , resultCode , data );

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri uri = data.getData();

            try {
                DataModel.uploadImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Intent intent = new Intent(FileSelectPage.this,UploadImage.class);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        else if(requestCode == 2 && resultCode == RESULT_OK && data != null){

            DataModel.uploadURl = data.getData ();
            Intent intent = new Intent (FileSelectPage.this,UploadImage.class);
            startActivity (intent);

        }else{
            Toast.makeText (FileSelectPage.this,"Error Picking Image",Toast.LENGTH_SHORT).show ();

        }


    }
}
