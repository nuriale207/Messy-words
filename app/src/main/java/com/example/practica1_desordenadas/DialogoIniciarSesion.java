package com.example.practica1_desordenadas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoIniciarSesion extends DialogFragment {

    public interface ListenerdelDialogoIniciarSesion {
        void alpulsarCerrarSesion();
        void alpulsarCambiarUsuario();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Seguro que quieres cerrar sesión?");
        //builder.setMessage("Has ganado!! " );
        DialogoIniciarSesion.ListenerdelDialogoIniciarSesion miListener = (DialogoIniciarSesion.ListenerdelDialogoIniciarSesion) getActivity();

        builder.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarCerrarSesion();
            }
        });

        builder.setNeutralButton("Cambiar usuario", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarCambiarUsuario();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });


        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
