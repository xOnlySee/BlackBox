package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

/**
 * @author Only See
 * Clase correspondiente al inicio de sesión
 */
public class SignIn extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputEmail, inputPassword;
    TextView changePasswordText, loginText;
    MaterialButton signInButton;
    ConstraintLayout layout;

    /**
     * Método donde definiremos la funcionabilidad del botón de volver atras
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Creamos un try-cacth para controlar los errores
        try {
            //Usamos el método finalize() donde saldremos de la aplicación
            finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_in);

        //Identificamos los TextInputEditText
        inputEmail = findViewById(R.id.inputEmail_signInScreen);
        inputPassword = findViewById(R.id.inputPassword_signInScreen);

        //Identificamos los TextView
        changePasswordText = findViewById(R.id.changePasswordText_signInScreen);
        loginText = findViewById(R.id.loginInText_signInScreen);

        //Identificamos el MaterialButton
        signInButton = findViewById(R.id.signInButton_signInScreen);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.signInScreen);

        //Añadimos la funcionabilidad al TextView de cambiar la contraseña
        changePasswordText.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionalidad para que vaya desde la pantalla de inicio de sesión a la actividad de recuperar la contraseña
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos un Intent que vaya a la actividad de recuperar la contraseña
                Intent intent = new Intent(SignIn.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        //Añadimos la funcionabilidad al TextView de crear una cuenta
        loginText.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad pars que vaya desde la pantalla de inicio de sesión a la actividad de crear cuenta
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, Login.class);
                startActivity(intent);
            }
        });

        //Añadimos la funcionabilidad del botón de incio de sesión
        signInButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionabilidad del botón para que el usuario pueda o no acceder a la aplicación dependiendo de si esta registrado o no
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Comprobamos la información de los campos del email y de la contraseña mediante un if-else

                //En caso de que algunos de los campos esten vacios...
                if (inputEmail.getText().toString().isEmpty() || inputPassword.getText().toString().isEmpty()) {
                    //Creamos una SnackBar para informar al usuario
                    Snackbar snackbar = Snackbar.make(layout, "Debes de introducir la información necesaria", Snackbar.LENGTH_LONG);
                    snackbar.show();

                //En cualquier otro caso
                } else {
                    //Realizamos las operaciones para realizar el inicio de sesión

                    /*
                    HACER EL INICIO DE SESIÓN
                     */
                }
            }
        });
    }
}