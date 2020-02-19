package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.chambeechat.R;
import com.example.chambeechat.ui.fragments.ChatsFragment;
import com.example.chambeechat.ui.fragments.ExplorarFragment;
import com.example.chambeechat.ui.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InicioActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment chatsFragment = new ChatsFragment();
    private Fragment explorarFragment = new ExplorarFragment();
    private Fragment perfilFragment = new PerfilFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;

                switch (menuItem.getItemId()) {
                    case R.id.iChats: {
                        fragment = chatsFragment;
                        break;
                    }
                    case R.id.iExplorar: {
                        fragment = explorarFragment;
                        break;
                    }
                    case R.id.iPerfil: {
                        fragment = perfilFragment;
                        break;
                    }
                    default: {
                        fragment = chatsFragment;
                    }
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();

                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.iChats);
    }
}
