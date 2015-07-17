package org.operationspark.hallebotgenerator.ui.main;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.operationspark.hallebotgenerator.R;
import org.operationspark.hallebotgenerator.models.HalleBot;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.generate)
    Button generate;

    GoogleApiClient mGoogleApiClient;

    Dialog mLoadingDialog;

    boolean connected;

    @Bind(R.id.name)
    EditText name;

    @Bind(R.id.intelligence)
    EditText intelligence;

    @Bind(R.id.vitality)
    EditText vitality;

    @Bind(R.id.strength)
    EditText strength;

    @Bind(R.id.agility)
    EditText agility;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLoadingDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        buildGoogleApiClient();
        connected = false;
        generate.setEnabled(false);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            connected = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        connected = true;
        generate.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @OnClick(R.id.generate)
    public void generateClicked(View view){
        if(connected) {
            if(name.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(this, "Name required", Toast.LENGTH_LONG)
                        .show();
            }else {

                showLoadingDialog();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HalleBot bot = HalleBot.build(name.getText().toString(), getLocation());

                        if (bot != null) {
                            intelligence.setText(Integer.toString(bot.intelligence));
                            vitality.setText(Integer.toString(bot.vitality));
                            strength.setText(Integer.toString(bot.strength));
                            agility.setText(Integer.toString(bot.agility));
                        }

                        hideProgressDialog();
                    }
                }, 2000);
            }
        }
    }

    private void showLoadingDialog(){
        if(mLoadingDialog != null) {
            Window window = mLoadingDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
            mLoadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLoadingDialog.show();
        }
    }

    public void hideProgressDialog() {
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    public org.operationspark.hallebotgenerator.models.Location getLocation(){
        if(connected) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                return new org.operationspark.hallebotgenerator.models.Location(
                        mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(),
                        mLastLocation.getAccuracy()
                );
            }
        }

        return null;
    }
}
