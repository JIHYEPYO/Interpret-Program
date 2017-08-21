package com.ncslab.pyojihye.interpretprogram.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ncslab.pyojihye.interpretprogram.Activity.LoginActivity;
import com.ncslab.pyojihye.interpretprogram.Database.ButtonViewerDatabase;
import com.ncslab.pyojihye.interpretprogram.R;

import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGES_BUTTON_VIEWER;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.delete;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.delete_num;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mUsername;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ViewerOptionListFragment extends Fragment {
    private final String TAG = "ViewerModeOptionListFragment";

    private static ViewerOptionListFragment instance = new ViewerOptionListFragment();
    ArrayAdapter<String> adapter;
    ListView listViewDelete;


    @Override
    public void onResume() {
//        Log.d(TAG,"onResume()");

        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG,"onCreateView()");
        View v = inflater.inflate(R.layout.fragment_viewer_option_list, container, false);


        if (checkLoginAccount()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return v;
        }

        listViewDelete = (ListView) v.findViewById(R.id.listViewDeleteWordList);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, delete);
        listViewDelete.setAdapter(adapter);
        listViewDelete.setLongClickable(true);
        listViewDelete.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ButtonViewerDatabase dataBase = new ButtonViewerDatabase(mUsername, currentTime(), "Delete Cancel", delete.get(position));
                mFirebaseDatabaseReference.child(MESSAGES_BUTTON_VIEWER).push().setValue(dataBase);

                delete.remove(position);
                delete_num--;

                onResume();
                return true;
            }
        });
        return v;
    }

    public static synchronized ViewerOptionListFragment getInstance() {
        return instance;
    }
}
