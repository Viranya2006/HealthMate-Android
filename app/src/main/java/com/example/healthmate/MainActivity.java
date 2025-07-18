package com.example.healthmate;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; // Import this class
import androidx.fragment.app.Fragment;
import com.example.healthmate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        // Handle bottom navigation item clicks
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_first_aid) {
                selectedFragment = new FirstAidFragment();
            } else if (itemId == R.id.nav_language) {
                showLanguageDialog();
                return false; // Don't switch fragment, just show dialog
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });
    }

    private void showLanguageDialog() {
        final String[] languages = {getString(R.string.lang_english), getString(R.string.lang_sinhala)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_language_title));
        builder.setItems(languages, (dialog, which) -> {
            String langCode = (which == 0) ? "en" : "si";
            LocaleHelper.setLocale(this, langCode);

            // Recreate the activity to apply the language change
            recreate();

            String toastMessage = (langCode.equals("en")) ? "Language changed to English" : "භාෂාව සිංහලට මාරු විය";
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
}
