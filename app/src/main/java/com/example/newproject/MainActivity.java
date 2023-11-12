package com.example.newproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.newproject.models.Category;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Syncing with backend server....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        binding.appBarMain.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog();
            }
        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categories, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // make the app bar visible only if the current fragment is the home fragment
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_home) {
                binding.appBarMain.fabRefresh.setVisibility(View.VISIBLE);
            } else {
                binding.appBarMain.fabRefresh.setVisibility(View.GONE);
            }

            if (destination.getId() == R.id.nav_categories) {
                binding.appBarMain.fabAdd.setVisibility(View.VISIBLE);
            } else {
                binding.appBarMain.fabAdd.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.
                onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_github) {
            showServerUrlDialog("repo_url");
            return true;
        }
        if (id == R.id.action_username) {
            showServerUrlDialog("username");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddCategoryDialog() {
        // Create an AlertDialog with an EditText for entering the server URL
        Context context = this; // Use the appropriate context here
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter category name:");

        // Create an EditText and set it as the dialog's view
        final EditText input = new EditText(context);
        input.setHint("Category name");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        builder.setView(input);

        // Set positive (OK) button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = input.getText().toString();
                Category category = new Category("", categoryName);
                category.save();
                //setShareKey(type, repoUrl);
            }
        });

        // Set negative (Cancel) button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }

    private void showServerUrlDialog(String type) {
        // Create an AlertDialog with an EditText for entering the server URL
        Context context = this; // Use the appropriate context here
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (type == "repo_url")
            builder.setTitle("Enter GitHub repo URL");
        else if (type == "username")
            builder.setTitle("Enter Your account username");

        // Create an EditText and set it as the dialog's view
        final EditText input = new EditText(context);
        if (type == "repo_url")
            input.setHint("Enter GitHub repo URL");
        else if (type == "username")
            input.setHint("Enter Your account username");
        String savedServerUrl = getSharedKey(type);
        if (!savedServerUrl.isEmpty()) {
            input.setText(savedServerUrl);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        builder.setView(input);

        // Set positive (OK) button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String repoUrl = input.getText().toString();
                setShareKey(type, repoUrl);
            }
        });

        // Set negative (Cancel) button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }

    private void setShareKey(String key, String value) {
        // Save the URL using SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this); // Use the appropriate context here
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // You can retrieve the URL using a method like this
    private String getSharedKey(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this); // Use the appropriate context here
        return preferences.getString(key, "");
    }


}