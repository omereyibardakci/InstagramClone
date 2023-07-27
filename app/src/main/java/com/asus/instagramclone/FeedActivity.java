package com.asus.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class FeedActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mAuth=FirebaseAuth.getInstance();
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
            Intent intentToShare =  new Intent(FeedActivity.this,ShareActivity.class);
            startActivity(intentToShare);

        } else if (item.getItemId()==R.id.log_out) {

            mAuth.signOut();

            Intent intentToMain = new Intent(FeedActivity.this,MainActivity.class);
            intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // closes all previous activities
            startActivity(intentToMain);

        }

        return super.onOptionsItemSelected(item);
    }
}