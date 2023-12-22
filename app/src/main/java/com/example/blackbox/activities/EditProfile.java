package com.example.blackbox.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.blackbox.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    StorageReference storageReference;
    FloatingActionButton buttonChangeImageProfile;
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    private String idDocumentValue, route = "imagenes_perfil/";
    private static final int COD_SEL_IMAGE = 300;

    /**
     * Método donde gestionaremos la imagen
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Comprobamos que se ha seleccionado una imagen de la galería
        if (requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK && data != null) {
            //Obtenemos la Uri de la imagen seleccionada
            Uri uri = data.getData();

            //Actualizamos la imagen del ImageView
            Glide.with(this).load(uri).override(150, 150).into(imageProfile);

            //Llamamos al método .selectProfile() donde le pasamos el objeto de la clase Uri
            selectProfile(uri);
        }
    }

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

        //Instanciamos el objeto de la clase StorageReference
        storageReference = FirebaseStorage.getInstance().getReference();

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

        //Creamos e instanciamos el objeto de la clase LifecycleOwner
        LifecycleOwner lifecycleOwner = this;

        //Creamos e instanciamos el objeto de la clase OnBackPressedCallBack
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            /**
             * Añadimos la funcionabilidad para el botón de retroceso
             */
            @Override
            public void handleOnBackPressed() {
                //En caso de que el método .verifyInformation devuelva "true" (alguno de los campo estan vacios)
                if (verifyInformation() == true) {
                    //Creamos una Snackbar para mostrar por pantalla lo sucedido al usuario
                    Snackbar snackbar = Snackbar.make(layout, "Debes de rellenar la información", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("De acuerdo", new View.OnClickListener() {
                            /**
                             * Hacemos el Snackbar se cierre
                             * @param v The view that was clicked.
                             */
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });

                        snackbar.show();

                //En caso de que el método .verifyInformation devuelva "false" (no hay campos vacios)
                } else if (verifyInformation() == false) {
                    //Creamos un Intent que vuelva a la pantalla de inicio. Le pasamos el ID del documento del usuario
                    Intent intent = new Intent(EditProfile.this, MainScreen.class);
                        intent.putExtra("idDocument", idDocumentValue);
                    startActivity(intent);
                }
            }
        };

        //Usamos el método .addCallback donde le pasamos la vida del ciclo y la funcionalidad del botón de retroceso
        getOnBackPressedDispatcher().addCallback(lifecycleOwner, onBackPressedCallback);

        //Añadimos la funcionabilidad al botón de cambiar la imagen de perfil
        buttonChangeImageProfile.setOnClickListener(new View.OnClickListener() {
            /**
             * Creamos un Intent implicito que redirija al usuario a la galeria donde tiene las imagenes
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent = new Intent(Intent.ACTION_PICK);

                //Añadimos el tipo de Intent
                intent.setType("image/*");

                //Iniciamos el Intent junto al código de estado
                startActivityForResult(intent, COD_SEL_IMAGE);
            }
        });

        //Usamos el objeto de la clase DocumentReference para obtener los datos necesarios del usuario
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /**
             * Realizamos las comprobaciones necesarias para añadir la información a los campos necesarios
             * @param documentSnapshot Objeto de la clase DocumentSnapshot
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Creamos e instanciamos un objeto de la clase Uri donde obtendremos el enlace de la imagen de perfil
                Uri uri = Uri.parse(documentSnapshot.getString("imagenPerfil"));

                //Usamos FirebaseStorage donde obtenemos la instancia, el objeto de la clase Uri y los metadatos de la imagen obtenida
                FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(uri)).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    /**
                     * Método donde realizaremos la función para obtener los metadatos de la imagen atualizada y donde usaremos Glide para añadir la imagen de perfil
                     * @param storageMetadata Objeto de la clase StorageMetadata
                     */
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        //Almacenamos en la variable de tipo long la hora de la modificación de la imagen de perfil
                        long lastModification = storageMetadata.getUpdatedTimeMillis();

                        //Usamos Glide donde le pasamos los valores necesarios así como los métodos que necesitamos para cargar la imagen de perfil del usuario
                        Glide.with(EditProfile.this)
                                .load(uri)
                                .signature(new ObjectKey(lastModification))
                                .into(imageProfile);
                    }
                });

                //En caso de que la información del campo de la biografia (en FireBase) no este vacio, añadimos dicha información en el campo de la biografia
                if (!documentSnapshot.getString("biografia").isEmpty()) {
                    inputBiography.setText(documentSnapshot.getString("biografia"));
                }

                //En caso de que la información del campo del nombre de usuario (en FireBase) no este vacio, añadimos dicha información en el campo del nombre de usuario
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

    /**
     * Métodod donde gestionaremos la nueva imagen de perfil
     * @param uri Objeto de la clase Uri
     */
    private void selectProfile(Uri uri) {
        //Usamos el objeto de la clase StorageReferces donde le indicamos la ruta y el nombre de la imagen y usamos el método .putFile() junto a la variable Uri
        storageReference.child("imagenes_perfil/imagenPerfil" + idDocumentValue).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            /**
             * Método realizaremos las acciones donde actualizará la imagen de perfil
             * @param taskSnapshot Objeto de la clase TaskSnapShot
             */
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Realizamos las acciones pertinentes para guardar el link de la imagen (almacenada en el módulo "Storage") en el módulo "FireStore"
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            /**
                             * Método donde actualizaremos el link de la imagen de perfil del usuario con la nueva imagen (subida previamente a FireBase)
                             * @param uri Objeto de la clase Uri
                             */
                            @Override
                            public void onSuccess(Uri uri) {
                                //Creamos un HasMap donde añadiremos el nuevo valor a actualizar
                                HashMap<String, Object> map = new HashMap<>();
                                    map.put("imagenPerfil", uri);

                                //Usamos el objeto de la clase FireStore donde indicamos el nombre de la colección, el ID del usuario y el HashMap con el nuevo valor
                                firestore.collection("perfil").document(idDocumentValue).update(map);
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean verifyInformation() {
        boolean empty = false;

        if (inputBiography.getText().toString().isEmpty() || inputUserName.getText().toString().isEmpty()) {
            empty = true;
        } else if (!inputBiography.getText().toString().isEmpty() || !inputUserName.getText().toString().isEmpty()) {
            empty = false;
        }

        return empty;
    }
}