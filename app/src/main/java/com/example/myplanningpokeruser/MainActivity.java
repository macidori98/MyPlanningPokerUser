package com.example.myplanningpokeruser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myplanningpokeruser.Fragment.LoginFragment;
import com.example.myplanningpokeruser.Utils.FragmentNavigation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentNavigation.getInstance(this).replaceFragment(new LoginFragment(), R.id.fragment_content);
    }
}
