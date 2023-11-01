package com.example.blackbox.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blackbox.R;
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

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("biografia").isEmpty() || documentSnapshot.getString("imagenPerfil").isEmpty()) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity())
                            .setTitle("Configura tu perfil")
                            .setMessage("Para poder continar debes de configurar tu perfil")
                            .setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /*
                                    CONFIGURAR ACTIVIDAD DE EDITAR PERFIL
                                     */
                                }
                            });
                    materialAlertDialogBuilder.show();
                }
            }
        });

        return view;
    }
}