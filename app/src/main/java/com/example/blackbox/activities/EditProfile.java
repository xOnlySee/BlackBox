package com.example.blackbox.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.blackbox.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Only See
 * Clase correspondiente a la gestión del perfil del usuario
 */
public class EditProfile extends AppCompatActivity {
    //Declaramos los elementos necesarios
    TextInputEditText inputUserName, inputBiography;
    MaterialButton buttonConfirmation;
    ScrollView layout;
    ImageView imageProfile;
    FloatingActionButton buttonChangeImageProfile;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    private String idDocumentValue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        //Identificamos los TextInputEditText
        inputUserName = findViewById(R.id.inputUserName_editProfile);
        inputBiography = findViewById(R.id.inputBiography_editProfile);

        //Identificamos el MaterialButton
        buttonConfirmation = findViewById(R.id.buttonConfirmation_editProfile);

        //Identificamos el ScrollView
        layout = findViewById(R.id.editInformation);

        //Identificamos el ImageView
        imageProfile = findViewById(R.id.imageProfile_editProfile);

        //Identificamos el FloatingActionButton
        buttonChangeImageProfile = findViewById(R.id.buttonChangeImageProfile_editProfile);

        //Creamos un Bundle para obtener los elementos necesarios de las otras actividades
        Bundle bundle = getIntent().getExtras();

        //Almacenamos en el String el ID del documento del usuario
        idDocumentValue = bundle.getString("idDocument");

        //Instanciamos el objeto de la clase FirebaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto de la clase CollectionReference
        collectionReference = firestore.collection("perfil");

        //Instanciamos el objeto de la clase DocumentReference
        documentReference = collectionReference.document(idDocumentValue);

        //Usamos el objeto de la clase DocumentReference para obtener los datos necesarios del usuario
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /**
             * Realizamos las comprobaciones necesarias para añadir la información a los campos necesarios
             * @param documentSnapshot Objeto de la clase DocumentSnapshot
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.getString("biografia").isEmpty()) {
                    inputBiography.setText(documentSnapshot.getString("biografia"));
                }

                if (!documentSnapshot.getString("nombreUsuario").isEmpty()) {
                    inputUserName.setText(documentSnapshot.getString("nombreUsuario"));
                }
            }
        });

        //Declaramos la funcionabilidad del botón de confirmar cambios
        buttonConfirmation.setOnClickListener(new View.OnClickListener() {
            /**
             * Realizamos las implementaciones para guardar los cambios
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Comprobamos el contenido de los campos

                //En caso de que algunos de los campos esten vacios...
                if (inputUserName.getText().toString().isEmpty() || inputBiography.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Debes de rellenar todos los campos necesarios", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                //En cualquier otro caso...
                } else {
                    //Creamos un MaterialAlertDialog para que el usuario confirme los cambios
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(EditProfile.this)
                            .setTitle("¿Deseas actualizar el perfil?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                /**
                                 * Declaramos la funcionalidad del botón positivo
                                 * @param dialog the dialog that received the click
                                 * @param which the button that was clicked (ex.
                                 *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                                 *              of the item clicked
                                 */
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Llamamos al método para actualizar la información del usuario
                                    updateProfileInformation(inputUserName.getText().toString(), inputBiography.getText().toString());
                                }
                            });

                        materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /**
                             * Añadimos la funcionabilidad del botón negativo
                             * @param dialog the dialog that received the click
                             * @param which the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                materialAlertDialogBuilder.setCancelable(true);
                            }
                        }).show();
                }
            }
        });
    }

    /**
     * Método donde actualizaremos el perfil del usuario
     * @param nameUser Variable de tipo String donde almacena el nombre de usuario
     * @param biography Variable de tipo String donde almacena la biografia del usuario
     */
    public void updateProfileInformation(String nameUser, String biography) {
        //Creamos un Map donde añadimos la información necesaria
        Map<String, Object> map = new HashMap<>();
            map.put("nombreUsuario", nameUser);
            map.put("biografia", biography);

        //Usamos el objeto de la clase DocumentReference donde actualizamos los valores
        documentReference.update(map);

        //Creamos una Snackbar para informar al usuario que ha modificado su perfil
        Snackbar snackbar = Snackbar.make(layout, "Perfil actualizado", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("De acuerdo", new View.OnClickListener() {
                /**
                 * Declaramos la funcionabilidad del Snackbar
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    //Creamos un Intent que vaya a la pantalla donde contiene los fragmentos y enviamos el ID del documento
                    Intent intent = new Intent(EditProfile.this, MainScreen.class);
                        intent.putExtra("idDocument", idDocumentValue);
                    startActivity(intent);
                }
            });

            snackbar.show();
    }
}