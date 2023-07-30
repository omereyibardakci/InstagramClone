package com.asus.instagramclone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.asus.instagramclone.databinding.ActivityMainBinding;
import com.asus.instagramclone.databinding.ActivityShareBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class ShareActivity extends AppCompatActivity {

    private ActivityShareBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Uri imageData;

    FirebaseStorage firebaseStorage;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();

        // Firebase
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
    }


    public void selectImage(View view){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(ShareActivity.this,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Permisson needed for go to gallary.",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                        }
                    }).show();
                }else {
                    // reguest permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            }else {
                // go to gallary
                Intent intentToGallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallary);
            }


        }else {
            if (ContextCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(ShareActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Permisson needed for go to gallary.",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                        }
                    }).show();
                }else {
                    // reguest permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else {
                // go to gallary
                Intent intentToGallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallary);
            }


        }

    }


    public void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null){
                        imageData = intentFromResult.getData();
                        binding.imageView.setImageURI(imageData);
                    }
                }
            }
        });


        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                   if(result){
                       // permission granted
                       Intent intentToGallary = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                       activityResultLauncher.launch(intentToGallary);
                   }else {
                       // permission denied
                       Toast.makeText(ShareActivity.this,"Permission needed for go to gallary.",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void share(View view){

        if(imageData != null){

            // universal unique id
            UUID uuid = UUID.randomUUID();
            String imageReferenceName = "images/"+uuid+".jpg";


            storageReference.child(imageReferenceName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageReferenceName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();

                            String comment = binding.editTextComment.getText().toString();

                            HashMap<String, Object> postData = new HashMap<>();         // keeps data as double keywords

                            postData.put("userEmail",userEmail);
                            postData.put("downloadUrl",downloadUrl);
                            postData.put("comment",comment);
                            postData.put("date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(ShareActivity.this, FeedActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShareActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShareActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });


        }




    }


}