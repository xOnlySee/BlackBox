package com.example.blackbox.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.blackbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

/**
 * @author Only See
 * Clase correspondiente a la clase de recuperación de contraseña
 */
public class ChangePassword extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputEmail;
    MaterialButton changePasswordButton;
    ConstraintLayout layout;
    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;

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

        //Instanciamos el objeto de la clase FireBaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Instanciamos el objeto de la clase AwesomeValidation donde le indicamos el nivel de validación
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.inputEmail_changePasswordScreen, Patterns.EMAIL_ADDRESS, R.string.email_error);

        //Creamos e instanciamos el objeto de la clase LifecycleOwner
        LifecycleOwner lifecycleOwner = this;

        //Creamos e instanciamos un objetop de la clase OnBackPressedCallBack
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            /**
             * Añadimos la funcionalidad para el botón de retroceso
             */
            @Override
            public void handleOnBackPressed() {
                //Creamos un Intent que vaya a la actividad de inicio de sesión
                Intent intent = new Intent(ChangePassword.this, SignIn.class);
                startActivity(intent);
            }
        };

        //Usamos el método .addCallBack() donde le pasamos el ciclo de vida y la funcionalidad del botón de retroceso
        getOnBackPressedDispatcher().addCallback(lifecycleOwner, onBackPressedCallback);

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

                //En cualquier otro caso...
                } else {
                    //Llamamos al método donde enviará un correo electrónico para cambiar la contraseña
                    verifyAccount(inputEmail.getText().toString());
                }
            }
        });
    }

    /**
     * Método donde comprobaremos si una cuenta esta registrada en FireBase
     * @param email Variable de tipo String donde almacenará el email
     */
    public void verifyAccount(String email) {
        firebaseAuth.setLanguageCode("es");

        //Usamos el objeto de la clase FirebaseAuth donde le pasamos el email para comprobar que la cuenta existe
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            /**
             * Commprobamos si la cuenta esta registrada o no mediante un if-else y usando un listado de todos los modos de inicio de sesión
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //En caso de que el proceso haya salido de forma exitosa
                if (task.isSuccessful()) {
                    boolean exist = !task.getResult().getSignInMethods().isEmpty();

                    if (exist) {
                        Log.i("Cuenta", "La cuenta " + email + " SI esta registrada");
                    } else {
                        Log.e("Cuenta", "La cuenta " + email + " NO esta registrada");
                    }
                } else {
                    Log.e("Cuenta", "Error al verificar la cuenta: " + task.getException().toString());
                }
            }
        });
    }

    /**
     * Método donde enviaremos un correo electrónico al usuario para que pueda cambiar la contraseña
     * @param email Variable de tipo String donde contiene el email
     */
    public void changePassword(String email) {
        //Usamos el objeto FireBaseAuth seguido del método para enviar el correo de recuperación de la contraseña
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            /**
             * Método donde gestionaremos que el proceso de recuperación de cuenta se haya llevado a cabo de forma exitosa
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //En caso de que el proceso se haya llevado de forma correcta
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(layout, "Revisa tu bandeja de entrada", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Aceptar", new View.OnClickListener() {
                        /**
                         * Añadimos la funcionabilidad al botón del SnackBar
                         * @param v The view that was clicked.
                         */
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });
    }
}