package com.afundacion.fp.library;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MonstersActivity extends AppCompatActivity {
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment1()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment2()).commit();

        BottomNavigationView bar = findViewById(R.id.bottomNavigation);
        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.item1) {
                    Toast.makeText(context, "Este es el primero", Toast.LENGTH_LONG).show();

                    Fragment myFragment = new Fragment1();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).commit();
                }
                if (item.getItemId() == R.id.item2) {
                    Toast.makeText(context, "Segundo Digimon", Toast.LENGTH_LONG).show();

                    Fragment myFragment = new Fragment2();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).commit();
                }
                if (item.getItemId() == R.id.item3) {
                    Toast.makeText(context, "Este es el tercer Digimon", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
    }
}
