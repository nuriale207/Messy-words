package com.example.practica1_desordenadas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoIniciarSesion extends DialogFragment {
    //Diálogo que pregunta al usuario si realmente quiere cerrar sesión
    public interface ListenerdelDialogoIniciarSesion {
        void alpulsarCerrarSesion();
        void alpulsarCambiarUsuario();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.seguroDeCerrarSesion));
        DialogoIniciarSesion.ListenerdelDialogoIniciarSesion miListener = (DialogoIniciarSesion.ListenerdelDialogoIniciarSesion) getActivity();

        builder.setPositiveButton(getString(R.string.cerrarSesion), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarCerrarSesion();
            }
        });

        builder.setNeutralButton(getString(R.string.cambiarUsuario), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarCambiarUsuario();
            }
        });

        builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
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
