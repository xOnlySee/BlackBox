package com.example.blackbox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ScrollView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.blackbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Only See
 * Clase correspondiente a la pantalla de creación de cuentas
 */
public class Login extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputEmail, inputPassword, inputConfirmPassword;
    MaterialButton createAccountButton;
    ScrollView layout;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    /**
     * Declaramos la funcionabilidad del botón de volver atras
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Creamos un Intent que vaya a la actividad de inicio de sesión
        Intent intent = new Intent(Login.this, SignIn.class);
        startActivity(intent);
    }

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

        //Instanciamos el objeto de la clase FireBaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Instanciamos el objeto AwesomeValidation donde especificamos el tipo de validación
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //Añadimos la validación a los campos pertinentes
        awesomeValidation.addValidation(this, R.id.inputEmail_logInScreen, Patterns.EMAIL_ADDRESS, R.string.email_error);
        awesomeValidation.addValidation(this, R.id.inputPassword_logInScreen, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}", R.string.password_error);

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
                    //Comprobamos que la validación de los datos introducidos por el usuario son validos

                    //En caso de que sean válidos...
                    if (awesomeValidation.validate()) {
                        //Creamos un MaterialAlertDialog para informar al usuario de que creará la cuenta
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);
                            materialAlertDialogBuilder.setTitle("¿Deseas crear la cuenta?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    /**
                                     * Declaramos la funcionabilidad para crear la cuenta y añadirla a FireBase
                                     * @param dialog the dialog that received the click
                                     * @param which the button that was clicked (ex.
                                     *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                                     *              of the item clicked
                                     */
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addAcount(inputEmail.getText().toString(), inputPassword.getText().toString());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    /**
                                     * Declaramos la funcionabilidad para que se cierre el MaterialAlertDialog
                                     * @param dialog the dialog that received the click
                                     * @param which the button that was clicked (ex.
                                     *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                                     *              of the item clicked
                                     */
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        materialAlertDialogBuilder.setCancelable(true);

                                        Snackbar snackbar = Snackbar.make(layout, "Cuenta no creada", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                })
                                .show();
                    }
                }
            }
        });
    }

    /**
     * Método donde crearemos la cuenta con el email y la contraseña
     * @param email Varible de tipo String donde almacena el email del usuario
     * @param password Variable de tipo String donde almacena la contraseña del usuario
     */
    public void addAcount(String email, String password) {
        //Usamos este método donde le pasamos el email y la contraseña para crear la cuenta
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    /**
                     * Añadimos la funcionabilidad en casa deque se haya podido crear
                     * @param task Objeto de la clase Task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //En caso de que haya salido todo bien
                        if (task.isSuccessful()) {
                            //Creamos una SnackBar para informar al usuario que la cuenta ha sido creada de forma exitosa
                            Snackbar snackbar = Snackbar.make(layout, "Cuenta creada con éxito", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Iniciar sesión", new View.OnClickListener() {
                                /**
                                 * Declaramos la funcionabilidad del botón "Iniciar sesión", que será un Intent de la pantalla de crear cuenta a la de inicio de sesión
                                 * @param v The view that was clicked.
                                 */
                                @Override
                                public void onClick(View v) {
                                    //Creamos e iniciamos un Intent que vaya a la pantalla de inicio de sesión
                                    Intent intent = new Intent(Login.this, SignIn.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}