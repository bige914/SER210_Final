package edu.quinnipiac.ser210.wordcrunch;
/**
 * MainActivity activity, acts as NavHost for nav_graph, initializes database if it does not exist,
 * toolbar creation for menu use in MainFragment
 *
 * @authors Ellsworth Evarts IV
 * @date 5/02/2020
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    //DrawerLayout drawerLayout;
    //NavigationView navigationView;
    NavController navController;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupActionBarWithNavController(this, navController);
        //NavigationUI.setupWithNavController(navigationView, navController);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        myDb = new DatabaseHelper(this);

        Cursor data = myDb.getData();
        data.moveToFirst();
        if (data.getCount() == 0){
            Log.e("Database", "Not yet created, creating database");
            boolean insertData = myDb.insertData(0,0,0);

            if (insertData){
                Log.d("MainActivity: ", "database initialized");

            }
        }
    }
}
