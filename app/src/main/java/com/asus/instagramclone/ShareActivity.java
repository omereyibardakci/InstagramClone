package com.asus.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.asus.instagramclone.databinding.ActivityMainBinding;
import com.asus.instagramclone.databinding.ActivityShareBinding;

public class ShareActivity extends AppCompatActivity {

    private ActivityShareBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



    }


    public void selectImage(View view){


    }

    public void share(View view){


    }


}