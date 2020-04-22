package edu.quinnipiac.ser210.wordcrunch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private ShareActionProvider provider;
    private NavController navController = null;
    Toolbar toolbar;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        super.onCreateOptionsMenu(menu, inflater);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        if (provider == null)
            Log.d("MainFragment", "noshare provider");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home: this.navController.navigateUp();
            return true;
            case R.id.action_settings: navController.navigate(R.id.action_mainFragment_to_settingsFragment);
            return true;
            case R.id.action_performance: navController.navigate(R.id.action_mainFragment_to_performanceFragment);
            return true;
            case R.id.action_help: navController.navigate(R.id.action_mainFragment_to_instructionsFragment);
            return true;
        }

        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi there");
            if (provider != null) {
                provider.setShareIntent(intent);
            } else
                //Toast.makeText(this, "no provider", Toast.LENGTH_LONG).show();
                return true;
        }

        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Button playButton = (Button) view.findViewById(R.id.action_play);
        playButton.setOnClickListener(this);
        Button howTo = (Button) view.findViewById(R.id.action_how_to);
        howTo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_play: navController.navigate(R.id.action_mainFragment_to_gameFragment);
            break;
            case R.id.action_how_to: navController.navigate(R.id.action_mainFragment_to_howToFragment);
            break;
        }
    }
}
