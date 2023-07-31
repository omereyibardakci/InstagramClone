package com.asus.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asus.instagramclone.R;
import com.asus.instagramclone.adapter.PostsAdapter;
import com.asus.instagramclone.databinding.ActivityFeedBinding;
import com.asus.instagramclone.model.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private ActivityFeedBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Posts> postsArrayList;
    PostsAdapter postsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postsArrayList = new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();

        getDataFromFirestore();


        // receylerView
        binding.receylerView.setLayoutManager(new LinearLayoutManager(this));
        postsAdapter = new PostsAdapter(postsArrayList);
        binding.receylerView.setAdapter(postsAdapter);

    }




    public void getDataFromFirestore(){

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");    // To access the data we have create in the firestore

        // firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>()
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG);
                }
                if (value != null ){

                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){

                        Map<String, Object> dataFirestore = documentSnapshot.getData();

                        // Casting
                        String userEmail =(String) dataFirestore.get("userEmail");
                        String comment = (String) dataFirestore.get("comment");
                        String downloadUrl = (String) dataFirestore.get("downloadUrl");

                        Posts posts = new Posts(userEmail,comment,downloadUrl);

                        postsArrayList.add(posts);

                    }

                    postsAdapter.notifyDataSetChanged();    // SO IMPORTANT !!!

                }

            }
        });



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_post){
            Intent intentToShare =  new Intent(FeedActivity.this, ShareActivity.class);
            startActivity(intentToShare);

        } else if (item.getItemId()==R.id.log_out) {

            mAuth.signOut();

            Intent intentToMain = new Intent(FeedActivity.this, MainActivity.class);
            intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // closes all previous activities
            startActivity(intentToMain);

        }

        return super.onOptionsItemSelected(item);
    }
}