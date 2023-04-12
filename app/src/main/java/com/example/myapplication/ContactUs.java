package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ContactUs extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        //contact form
        Button submit = findViewById(R.id.submit);
        EditText nameE = findViewById(R.id.yourName);
        EditText emailE = findViewById(R.id.yourEmail);
        EditText commentE = findViewById(R.id.yourComment);

        //shared preference : keep the same name and email for each device
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String nameP = sharedPreferences.getString("name", "");
        String emailP = sharedPreferences.getString("email", "");
        nameE.setText(nameP);
        emailE.setText(emailP);

        submit.setOnClickListener(click -> {
            String name = String.valueOf(nameE.getText());
            String email = String.valueOf(emailE.getText());
            String comment = String.valueOf(commentE.getText());
            String content = name + " " + email + " " + comment;
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:cao00108@algonquinlive.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "BBC News Reader");
            intent.putExtra(Intent.EXTRA_TEXT,content);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("name", nameE.getText().toString());
            myEdit.putString("email", emailE.getText().toString());
            myEdit.apply();
            if (intent.resolveActivity(getPackageManager()) != null && name!=null && email!=null) {
                startActivity(intent);
                String message = getResources().getString(R.string.sent);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                nameE.setText("");
                emailE.setText("");
                commentE.setText("");
            } else {
                String message = getResources().getString(R.string.failed);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        });
    }

    //menu and nav
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } if (id == R.id.fav) {
            Intent intent = new Intent(this, Favorite.class);
            startActivity(intent);
        } else if (id == R.id.contact) {
            Intent intent = new Intent(this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.exit) {
            finishAffinity();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.homeIcon) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.helpIcon) {
            AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
            String message = getResources().getString(R.string.note3);
            aDialog.setMessage(message)
                    .setTitle(R.string.howTo)
                    .setNeutralButton(R.string.ok, (click, arg) -> {
                    })
                    .create()
                    .show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }
}