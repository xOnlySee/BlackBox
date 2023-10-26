package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

/**
 * @author Only See
 * Clase correspondiente a la clase de recuperación de contraseña
 */
public class ChangePassword extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputEmail;
    MaterialButton changePasswordButton;
    ConstraintLayout layout;

    /**
     * Método donde definiremos la funcionabilidad del botón de volver atras
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Creamos un Intent que vaya a la pantalla de inicio de sesión
        Intent intent = new Intent(ChangePassword.this, SignIn.class);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        //Identificamos el TextInputEditText
        inputEmail = findViewById(R.id.inputEmail_changePasswordScreen);

        //Identificamos el MaterialButton
        changePasswordButton = findViewById(R.id.changePasswordButton_changePasswordScreen);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.changePasswordScreen);

        //Declaramos la funcionabilidad del botón de cambio de contraseña
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Añadimos la funcionabilidad para que el usuario del correo electrónico reciba un mensaje para que cambie la contraseña
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Comprobamos mediante un if-else el contenido del TextInputEditText

                //En caso de que no haya información
                if (inputEmail.getText().toString().isEmpty()) {
                    //Creamos una Snackbar para informar del problema
                    Snackbar snackbar = Snackbar.make(layout, "Debes de introducir un correo electrónico", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
}