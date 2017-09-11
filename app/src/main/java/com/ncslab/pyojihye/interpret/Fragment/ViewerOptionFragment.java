package com.ncslab.pyojihye.interpret.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ncslab.pyojihye.interpret.Activity.LoginActivity;
import com.ncslab.pyojihye.interpret.Activity.ViewerActivity;
import com.ncslab.pyojihye.interpret.Database.ButtonViewerDatabase;
import com.ncslab.pyojihye.interpret.R;

import static com.ncslab.pyojihye.interpret.ECT.Const.MESSAGES_BUTTON_VIEWER;
import static com.ncslab.pyojihye.interpret.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpret.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpret.ECT.Const.delete;
import static com.ncslab.pyojihye.interpret.ECT.Const.delete_num;
import static com.ncslab.pyojihye.interpret.ECT.Const.gap;
import static com.ncslab.pyojihye.interpret.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpret.ECT.Const.mUsername;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ViewerOptionFragment extends Fragment {
    private final String TAG = "ViewerModeOptionFragment";

    private static ViewerOptionFragment instance = new ViewerOptionFragment();
    private EditText editTextDelete;
    private EditText editTextGap;
    private Button buttonDelete;
    private Button buttonPrint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG,"onCreateView()");

        View v = inflater.inflate(R.layout.fragment_viewer_option, container, false);

        editTextDelete = (EditText) v.findViewById(R.id.editTextDeleteWord);
        editTextGap = (EditText) v.findViewById(R.id.editTextOutputGap);
        buttonDelete = (Button) v.findViewById(R.id.buttonDelete);
        buttonPrint = (Button) v.findViewById(R.id.buttonPrint);

        if (checkLoginAccount()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return v;
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Log.d(TAG,"deleteButtonClick()");

                View parent = (View) view.getParent();
                if (editTextDelete.getText().toString().equals("")) {
                    Snackbar snackbar = Snackbar.make(parent.findViewById(R.id.buttonDelete), R.string.snack_bar_delete, Snackbar.LENGTH_LONG);

                    View v = snackbar.getView();
                    TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {
                    boolean deleteEqual = false;

                    for (int i = 0; i < delete_num; i++) {
                        if (delete.get(i).equalsIgnoreCase(editTextDelete.getText().toString())) {
//                            Log.v("EQUAL!!", Const.delete.get(i));
                            deleteEqual = true;
                            Snackbar snackbar = Snackbar.make(parent.findViewById(R.id.buttonDelete), R.string.snack_bar_equal, Snackbar.LENGTH_LONG);
                            View v = snackbar.getView();
                            TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        }
                    }
                    if (deleteEqual != true) {
                        delete.add(editTextDelete.getText().toString());
                        delete_num++;

                        downKeyboard(getContext(), editTextDelete);

                        ButtonViewerDatabase dataBase = new ButtonViewerDatabase(mUsername, currentTime(), "Delete",editTextDelete.getText().toString());
                        mFirebaseDatabaseReference.child(MESSAGES_BUTTON_VIEWER).push().setValue(dataBase);

                        Snackbar snackbar = Snackbar.make(parent.findViewById(R.id.buttonDelete), R.string.snack_bar_delete_success, Snackbar.LENGTH_LONG);
                        View v = snackbar.getView();
                        TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                        editTextDelete.setText("");
                    }
                }
            }
        });

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG,"PrintButtonClick()");

                View parent = (View) view.getParent();
                if (editTextGap.getText().toString().equals("")) {
                    Snackbar snackbar = Snackbar.make(parent.findViewById(R.id.buttonPrint), R.string.snack_bar_gap, Snackbar.LENGTH_LONG);

                    View v = snackbar.getView();
                    TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {
                    gap = Integer.parseInt(editTextGap.getText().toString());

                    ButtonViewerDatabase dataBase = new ButtonViewerDatabase(mUsername, currentTime(), "Gap",editTextGap.getText().toString());
                    mFirebaseDatabaseReference.child(MESSAGES_BUTTON_VIEWER).push().setValue(dataBase);

                    Intent intentViewer = new Intent(getActivity(), ViewerActivity.class);
                    startActivity(intentViewer);
                }
            }
        });
        return v;
    }

    public static synchronized ViewerOptionFragment getInstance() {
        return instance;
    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}