package com.example.blackbox.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blackbox.R;
import com.example.blackbox.activities.EditProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * @author Only See
 * Fragmento correspondiente al fragmento de inicio
 */
public class Home extends Fragment {
    //Declaramos los elementos necesarios
    View view;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    private String idDocumentValue;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idDocumentValue = getArguments().getString("idDocument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home, container, false);

        //Instanciamos el objeto de la clase FirebaseFireStore
        firestore = FirebaseFirestore.getInstance();

        //Instanciamos el objeto de la clase DocumentReference
        documentReference = firestore.collection("perfil").document(idDocumentValue);

        //Usamos el objeto de la clase DocumentReferences para añadir la funcionalidad
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /**
             * Creamos y añadimos la funcionalidad al MaterialAlertDialog
             * @param documentSnapshot Objeto de la clase DocumentSnapshot
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //En caso de que los campos de la biografia, el nombre de usuario o la imagen del perfil esten vacios...
                if (documentSnapshot.getString("biografia").isEmpty() || documentSnapshot.getString("nombreUsuario").isEmpty() || documentSnapshot.getString("imagenPerfil").isEmpty()) {
                    //Creamos un MaterialAlertDialog
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity())
                            .setTitle("Configura tu perfil")
                            .setMessage("Para poder continar debes de configurar tu perfil")
                            .setCancelable(false)
                            .setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Creamos un Intent que vaya a la pantalla de editar perfil y le enviamos el email del usuario
                                    Intent intent = new Intent(getActivity().getApplicationContext(), EditProfile.class);
                                        intent.putExtra("idDocument", idDocumentValue);
                                    startActivity(intent);
                                }
                            });
                    materialAlertDialogBuilder.show();
                }
            }
        });

        return view;
    }
}