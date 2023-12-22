package com.example.blackbox.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

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

        //Creamos e instanciamos un objeto de la clase LifecycleOwner
        LifecycleOwner lifecycleOwner = this;

        //Creamos e instanciamos un objeto de la clase OnBackpressedCallBack
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            /**
             * Añadimos la funcionabilidad del botón de retroceso
             */
            @Override
            public void handleOnBackPressed() {
                //Llamamos al método .showButtonSheet()
                showButtonSheet();
            }
        };

        //Usamos el método .addCallBack() donde le pasamos la vida del ciclo y la funcionalidad del botón de retroceso
        getOnBackPressedDispatcher().addCallback(lifecycleOwner, onBackPressedCallback);

        //Identificamos el BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Usamos el BottomNavigationView para declararle la funcionalidad
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        //Creamos un Bundle para obtener los datos que otras actividades
        Bundle bundle = getIntent().getExtras();

        //Añadimos los argumentos necesarios a los fragmentos necesarios
        home.setArguments(bundle);

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

    /**
     * Método donde mostraremos el BottonSheetDialog y daremos la funcionalidad a las distintas opciones
     */
    public void showButtonSheet() {
        //Creamos e instanciamos un objeto de la clase Dialog
        final Dialog dialog = new Dialog(MainScreen.this);

        //Usamos el método .requestWindowFeature() para ocultar el título
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Usamos el método .setContentView() donde le pasamos el Layout creado
        dialog.setContentView(R.layout.buttom_sheet_dialog);

        //Creamos e identificamos todos los eleentos que tendrá el Dialog
        LinearLayout exitAplication = dialog.findViewById(R.id.closeAplication_buttomSheet),
        logOut = dialog.findViewById(R.id.logOut_buttomSheet),
        closeWindow = dialog.findViewById(R.id.closeWindow_buttomSheet);

        //Añadimos la funcionalidad al elemento de cerrar la aplicación
        exitAplication.setOnClickListener(new View.OnClickListener() {
            /**
             * Añadimos la funcionabilidad para que cierre la aplicación
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent donde nos llevará a la pantalla de inicio y finalizará la aplicación
                Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        });

        //Añadimos la funcionalidad al elemento de cerrar sesión
        logOut.setOnClickListener(new View.OnClickListener() {
            /**
             * Añadimos la funcionabilidad para que vaya a la actividad de inicio de sesión
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent que vaya a la actividad de inicio de sesión
                Intent intent = new Intent(MainScreen.this, SignIn.class);
                startActivity(intent);
            }
        });

        //Añadimos la funcionabilidad al elemento de cerrar la ventana
        closeWindow.setOnClickListener(new View.OnClickListener() {
            /**
             * Añadimos la funcionabilidad para que la ventana se cierre cuando el elemento sea pulsado
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Usamos el método .dismiss() para cerrar el Dialog
                dialog.dismiss();
            }
        });

        //Por último mostramos el Dialog
        dialog.show();
    }
}