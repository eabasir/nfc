package ansari.com.nfcaesdemo.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ansari.com.nfcaesdemo.utils.NfcvFunction;
import ansari.com.nfcaesdemo.R;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private PendingIntent pendingIntent;
    private IntentFilter writeTagFilters[];
    private Tag mytag;
    private NfcAdapter adapter;

    private Button btnWrite;
    private Button btnRead;
    private EditText edtWriteBlocKNo;
    private EditText edtReadBlocKNo;
    private EditText edtWriteMessage;
    private EditText edtLength;
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        btnWrite = (Button) findViewById(R.id.btn_write);
        btnRead = (Button) findViewById(R.id.btn_read);
        edtWriteBlocKNo = (EditText) findViewById(R.id.edt_write_block_no);
        edtReadBlocKNo = (EditText) findViewById(R.id.edt_read_block_no);
        edtWriteMessage = (EditText) findViewById(R.id.edt_write_message);
        edtLength = (EditText) findViewById(R.id.edt_length);
        txtView = (TextView) findViewById(R.id.txt_view);


        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};


        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mytag == null) {
                    Snackbar.make(view, "tag is not detected", Snackbar.LENGTH_LONG).show();
                    return;
                }
                try {
                    if (edtWriteBlocKNo.getText().toString().equals("") || edtWriteMessage.getText().toString().equals("")) {
                        Snackbar.make(view, "enter block number and message", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    byte[] data = NfcvFunction.hexStringToByteArray(edtWriteMessage.getText().toString());
                    NfcvFunction.write(mytag, data, Integer.parseInt(edtWriteBlocKNo.getText().toString()));
                    Snackbar.make(view, "data is written on tag", Snackbar.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "error during writing on tag interrupted exception", Snackbar.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(view, "error during writing on tag exception", Snackbar.LENGTH_LONG).show();

                }

            }
        });

        btnRead.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                int blockNum = Integer.valueOf(edtReadBlocKNo.getText().toString());
                int length = Integer.valueOf(edtLength.getText().toString());

                String result = "";
                for (int i = 0; i < blockNum + length; i++) {
                    try {
                        byte[] part = NfcvFunction.read(mytag, blockNum + i);

                        result += NfcvFunction.getHexString(part).substring(2, 10);
                        txtView.setText(result);
                    } catch (Exception e) {
                        Snackbar.make(view, "error during reading tag", Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn() {
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff() {
        adapter.disableForegroundDispatch(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(Main.this, "tag detected" , Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
