package rodent.rodentmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.View;

/**
 * Created by Teemu on 25.10.2015.
 */
public class NewFileDialogFragment extends DialogFragment {

    public interface NewFileDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String filename);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    NewFileDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NewFileDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NewFileDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.newfiledialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setMessage("Enter filename");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) dialogView.findViewById(R.id.filename_edittext);
                mListener.onDialogPositiveClick(NewFileDialogFragment.this, editText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(NewFileDialogFragment.this);
            }
        });
        return builder.create();
    }
}
