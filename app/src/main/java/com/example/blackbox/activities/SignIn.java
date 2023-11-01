package com.example.blackbox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.blackbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * @author Only See
 * Clase correspondiente al inicio de sesión
 */
public class SignIn extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputEmail, inputPassword;
    TextView changePasswordText, loginText;
    MaterialButton signInButton;
    ScrollView layout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    private String idDocumentValue;

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

        //Instanciamos el objeto de la clase FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Intanciamos el objeto de la clase FirebaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto de la clase CollectionReference
        collectionReference = firestore.collection("perfil");

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
                    signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
                }
            }
        });
    }

    /**
     * Método donde iniciaremos sesión con el email y la contraseña
     * @param email Variable de tipo String que representa el email
     * @param password Variable de tipo String que representa la contraseña
     */
    public void signIn(String email, String password) {
        //Usamos el objeto de la clase FirebaseAuth para iniciar sesión
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            /**
             * Método donde comprobaremos si se puede iniciar sesión o no
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //En caso de que el proceso se haya llevado de forma exitosa
                if (task.isSuccessful()) {
                    //Llamamos al método para obtener el ID del documento del usuario e iniciar sesión
                    documentID(inputEmail.getText().toString());

                //En caso contrario...
                } else if (!task.isSuccessful()) {
                    //Mostrarmos por medio de una Snackbar de lo ocurrido al usuario
                    Snackbar snackbar = Snackbar.make(layout, "Verifica la información", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    /**
     * Método donde obtendremos el ID del documento del usuario
     * @param email Variable de tipo String que representa el email del usuario
     */
    public void documentID(String email) {
        //Usamos los métodos necesarios para obtener el ID del documento del usuario
        collectionReference.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * Almacenamos en la variable "idDocumentValue" el ID del documento del usuario
             * @param task Objeto de la clase Task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //En caso de que el proceso se haya llevado acabo de forma exitosa...
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        //Almacenamos el resultado en la variable indicada
                        idDocumentValue = documentSnapshot.getId();

                        //Creamos un Intent que vaya a la pantalla principal donde contendrá los distintos fragmentos
                        Intent intent = new Intent(SignIn.this, MainScreen.class);
                            intent.putExtra("idDocument", idDocumentValue);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}