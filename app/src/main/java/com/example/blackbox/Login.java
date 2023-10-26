package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

/**
 * @author Only See
 * Clase correspondiente a la pantalla de creación de cuentas
 */
public class Login extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputEmail, inputPassword, inputConfirmPassword;
    MaterialButton createAccountButton;
    ConstraintLayout layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Identificamos los TextInputEditText
        inputEmail = findViewById(R.id.inputEmail_logInScreen);
        inputPassword = findViewById(R.id.inputPassword_logInScreen);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword_logInScreen);

        //Identificiamos el MaterialButton
        createAccountButton = findViewById(R.id.createAccountButton_logInScreen);

        //Identificamos el ConstrainLayout
        layout = findViewById(R.id.loginInScreen);

        //Añadimos la funcionabilidad del botón de creación de cuenta
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Declaramos la funcionalidad del botón de crear cuenta para que después de verificar si la información es correcta, cree la cuenta
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Verificamos mediante un if-else la información de los TextView

                //En caso de que algunos de los campos esten vacios
                if (inputEmail.getText().toString().isEmpty() || inputPassword.getText().toString().isEmpty() || inputConfirmPassword.getText().toString().isEmpty()) {
                    //Informamos de los sucecido por medio de una SnackBar
                    Snackbar snackbar = Snackbar.make(layout, "Debes de rellenar los campos necesarios", Snackbar.LENGTH_LONG);
                    snackbar.show();

                //En caso de que las contraseñas no sean iguales
                } else if (!inputPassword.getText().toString().toLowerCase().equals(inputConfirmPassword.getText().toString().toLowerCase())) {
                    //Informamos de los sucecido por medio de una SnackBar
                    Snackbar snackbar = Snackbar.make(layout, "Asegurate de que las contraseñas sean iguales", Snackbar.LENGTH_LONG);
                    snackbar.show();

                //En cualquier otro caso
                } else {

                }
            }
        });
    }
}