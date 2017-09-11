package com.ncslab.pyojihye.interpret.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.ncslab.pyojihye.interpret.Database.ButtonMenuDatabase;
import com.ncslab.pyojihye.interpret.Fragment.MyFragmentPagerAdapter;
import com.ncslab.pyojihye.interpret.R;

import static com.ncslab.pyojihye.interpret.ECT.Const.ANONYMOUS;
import static com.ncslab.pyojihye.interpret.ECT.Const.MESSAGE_MENU;
import static com.ncslab.pyojihye.interpret.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpret.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpret.ECT.Const.mFirebaseAuth;
import static com.ncslab.pyojihye.interpret.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpret.ECT.Const.mUsername;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ViewerOptionActivity extends AppCompatActivity {
    private final String TAG = "ViewerOptionActivity";

    public MyFragmentPagerAdapter myFragmentPagerAdapter;
    private static ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");

        setTitle("Viewer Option");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_option);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(pager);

        if (checkLoginAccount()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                ButtonMenuDatabase dataBase = new ButtonMenuDatabase(mUsername, currentTime(), "Sign Out", TAG);
                mFirebaseDatabaseReference.child(MESSAGE_MENU).push().setValue(dataBase);

                mFirebaseAuth.signOut();
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.developer:
                ButtonMenuDatabase dataBase2 = new ButtonMenuDatabase(mUsername, currentTime(), "Developer Info", TAG);
                mFirebaseDatabaseReference.child(MESSAGE_MENU).push().setValue(dataBase2);
                Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}