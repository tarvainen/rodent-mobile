package rodent.rodentmobile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rodent.rodentmobile.R;

public class NewFileDialog extends DialogFragment {

    public interface NewFileDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String filename, float depth, float width, float height);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private NewFileDialogListener mListener;

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
        builder.setMessage(R.string.lib_title_new_file_dialog);

        builder.setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) dialogView.findViewById(R.id.filename);
                String filename = editText.getText().toString();
                float shapedepth = 0, paperwidth = 0, paperheight = 0;

                try {
                    editText = (EditText) dialogView.findViewById(R.id.shapedepth);
                    shapedepth = Float.valueOf(editText.getText().toString());
                    editText = (EditText) dialogView.findViewById(R.id.paperwidth);
                    paperwidth = Float.valueOf(editText.getText().toString());
                    editText = (EditText) dialogView.findViewById(R.id.paperheight);
                    paperheight = Float.valueOf(editText.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.lib_new_file_invalid_input), Toast.LENGTH_SHORT).show();
                    return;
                }

                mListener.onDialogPositiveClick(NewFileDialog.this, filename, shapedepth, paperwidth, paperheight);
            }
        });

        builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(NewFileDialog.this);
            }
        });
        return builder.create();
    }

}
