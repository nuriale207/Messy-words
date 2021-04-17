package com.example.practica1_desordenadas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoExisteContacto extends DialogFragment {
    //Dialogo fin de nivel
    AlertDialog.Builder builder;
    public interface ListenerdelDialogExisteContacto{
        void alpulsarAñadir();
        //void alpulsarSiguienteNivel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.anadir));
        builder.setMessage(getString(R.string.contactoRepetido));
        DialogoExisteContacto.ListenerdelDialogExisteContacto miListener = (DialogoExisteContacto.ListenerdelDialogExisteContacto) getActivity();

        builder.setPositiveButton(getString(R.string.anadir), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarAñadir();
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
