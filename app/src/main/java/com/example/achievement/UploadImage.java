package com.example.achievement;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadImage extends AppCompatActivity {

    ImageView imageView;
    DatabaseReference reference;
    ProgressBar progressBar;
    View progressView;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_upload_image );

        imageView = findViewById ( R.id.imageView2 );
        imageView.setImageBitmap ( DataModel.uploadImage );

        reference = FirebaseDatabase.getInstance ().getReference ().child ( "AuthentiCation" );
        progressBar = findViewById ( R.id.progressBarU );
        progressView = findViewById ( R.id.view3U );



    }


    public void uploadData( View view) {




        if (DataModel.year != null) {

            if (DataModel.className != null) {
                if (DataModel.uploadDataType == 0) {

                    progressView.setVisibility ( View.VISIBLE );
                    progressBar.setVisibility ( View.VISIBLE );
                    DateFormat df = new SimpleDateFormat ( "yyyy.MM.dd G 'at' HH:mm:ss z" );
                    String date = df.format ( Calendar.getInstance ( ).getTime ( ) );
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ( );
                    Bitmap bitmap = DataModel.uploadImage;
                    bitmap.compress ( Bitmap.CompressFormat.JPEG , 50 , byteArrayOutputStream );
                    byte[] uploadData = byteArrayOutputStream.toByteArray ( );
                    StorageReference storage = FirebaseStorage.getInstance ( ).getReference ().child ("AcademicYear " + DataModel.year );
                    StorageReference reference1 = storage.child ( DataModel.className ).child ( DataModel.rollNo ).child ( DataModel.cateOfCertificate + date + ".jpg" );
                    reference1.putBytes ( uploadData ).addOnCompleteListener ( new OnCompleteListener <UploadTask.TaskSnapshot> ( ) {
                        @Override
                        public void onComplete( @NonNull Task <UploadTask.TaskSnapshot> task ) {
                            if (task.isSuccessful ( )) {

                                progressBar.setVisibility ( View.INVISIBLE );
                                progressView.setVisibility ( View.INVISIBLE );
                                updateCountData ( );
                                Toast.makeText ( UploadImage.this , "Image Uploaded" , Toast.LENGTH_SHORT ).show ( );

                            } else {

                                Toast.makeText ( UploadImage.this , "Image Upload Failed" , Toast.LENGTH_SHORT ).show ( );
                                progressBar.setVisibility ( View.INVISIBLE );
                                progressView.setVisibility ( View.INVISIBLE );
                            }
                        }
                    } );


                } else if (DataModel.uploadDataType == 1) {

                    progressView.setVisibility ( View.VISIBLE );
                    progressBar.setVisibility ( View.VISIBLE );
                    DateFormat df = new SimpleDateFormat ( "yyyy.MM.dd G 'at' HH:mm:ss z" );
                    String date = df.format ( Calendar.getInstance ( ).getTime ( ) );
                    FirebaseStorage storage = FirebaseStorage.getInstance ( );
                    StorageReference reference2 = storage.getReference ( ).child ( DataModel.className ).child ( DataModel.rollNo ).child ( DataModel.cateOfCertificate + date + ".jpg" );
                    reference2.putFile ( DataModel.uploadURl ).addOnCompleteListener ( new OnCompleteListener <UploadTask.TaskSnapshot> ( ) {
                        @Override
                        public void onComplete( @NonNull Task <UploadTask.TaskSnapshot> task ) {

                            if (task.isSuccessful ( )) {
                                updateCountData ( );
                                Toast.makeText ( UploadImage.this , "PDF Uploaded" , Toast.LENGTH_SHORT ).show ( );
                                progressBar.setVisibility ( View.INVISIBLE );
                                progressView.setVisibility ( View.INVISIBLE );


                            } else {

                                Toast.makeText ( UploadImage.this , "PDF Uploaded Failed" , Toast.LENGTH_SHORT ).show ( );
                                progressBar.setVisibility ( View.INVISIBLE );
                                progressView.setVisibility ( View.INVISIBLE );

                            }
                        }
                    } );


                }
            } else {

                Toast.makeText ( UploadImage.this , "Please check your Internet1" , Toast.LENGTH_SHORT ).show ( );
            }


        }else{

            Toast.makeText ( UploadImage.this,"Please check your Internet2",Toast.LENGTH_SHORT ).show ();

        }

    }


    //function to update Certificate Count

  void updateCountData(){

        try {


            if (DataModel.catiOfCertiInt == 1) {

                DataModel.countSport = DataModel.countSport + 1;
            } else if (DataModel.catiOfCertiInt == 2) {

                DataModel.countAcadmic = DataModel.countAcadmic + 1;
            } else if (DataModel.countTech == 3) {

                DataModel.countTech = DataModel.countTech + 1;
            } else if (DataModel.countOther == 4) {
                DataModel.countOther = DataModel.countOther + 1;
            }

            DataModel.count = DataModel.count + 1;


            DatabaseReference database2 = FirebaseDatabase.getInstance ( ).getReference ( ).child ( "ExcelSheet" ).child ( DataModel.ID );
            Map <String, String> map = new HashMap <> ( );
            map.put ( "A-Class" , DataModel.className );
            map.put ( "B-Roll No" , DataModel.rollNo );
            map.put ( "C-Name" , DataModel.nameOfStudent );
            map.put ( "D-Academic" , String.valueOf ( DataModel.countAcadmic ) );
            map.put ( "E-Sport" , String.valueOf ( DataModel.countSport ) );
            map.put ( "F-Technical" , String.valueOf ( DataModel.countTech ) );
            map.put ( "G-Other" , String.valueOf ( DataModel.countOther ) );
            map.put ( "Total" , String.valueOf ( DataModel.count ) );
            database2.setValue ( map );

        }catch (Exception e){

            System.out.print ( e )  ;


        }




  }


}
