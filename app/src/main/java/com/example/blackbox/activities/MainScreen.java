package com.example.blackbox.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.blackbox.R;
import com.example.blackbox.fragments.Home;
import com.example.blackbox.fragments.Message;
import com.example.blackbox.fragments.Profile;
import com.example.blackbox.fragments.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author Only See
 * Clase donde gestionaremos la pantalla donde contendrá los fragmentos
 */
public class MainScreen extends AppCompatActivity {
    //Declaramos los elementos necesarios
    final Home home = new Home();
    final Search search = new Search();
    final Message message = new Message();
    final Profile profile = new Profile();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        //Identificamos el BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Usamos el BottomNavigationView para declararle la funcionalidad
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        //Usamos el método loadFragment() donde especificamos el fragmento que se carga de forma predeterminada
        loadFragment(home);
    }

    /**
     * Creamos un método donde implementemos la funcionabilidad que va a tener cada opción del BottomNavigationView
     */
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        //Comprobamos mediante un if-else que menu ha seleccionado el usuario

        if (item.getItemId() == R.id.homeFragment) {
            loadFragment(home);
        } else if (item.getItemId() == R.id.searchFragment) {
            loadFragment(search);
        } else if (item.getItemId() == R.id.messageFragment) {
            loadFragment(message);
        } else if (item.getItemId() == R.id.profileFragment) {
            loadFragment(profile);
        }

        return true;
    };

    /**
     * Método creado para cargar un determinado fragmento
     * @param fragment Representa un fragmento
     */
    public void loadFragment(Fragment fragment) {
        //Creamos un FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //Usamos el FragmentTransaction .replace donde le indicamos el frameContainer y el fragmento
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.commit();
    }
}