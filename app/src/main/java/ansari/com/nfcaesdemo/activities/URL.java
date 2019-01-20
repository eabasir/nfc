package ansari.com.nfcaesdemo.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ansari.com.nfcaesdemo.R;
import ansari.com.nfcaesdemo.utils.AES;
import ansari.com.nfcaesdemo.utils.StringFunction;

public class URL extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView edtURL;
    TextView edtKey;
    Button btnDecrypt;
    TextView txtEncHex;
    TextView txtDecHex;
    TextView txtSerial;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        this.edtURL = (TextView) findViewById(R.id.edtURL);
        this.edtKey = (TextView) findViewById(R.id.edtKey);
        this.btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
        this.txtEncHex = (TextView) findViewById(R.id.textView10);
        this.txtDecHex = (TextView) findViewById(R.id.txtDecHex);
        this.txtSerial = (TextView) findViewById(R.id.txtSerial);
        this.txtCount = (TextView) findViewById(R.id.txtCount);

        this.edtURL.setText("https://pas.kishisc.ir/map=;>:<12<1:383=32=21;80329905>?57���");

        this.btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!edtURL.getText().toString().equals("") && !edtKey.getText().toString().equals("")) {

                    try {


                        String part = edtURL.getText().toString().replace("https://pas.kishisc.ir/map", "");


                        byte[] arr_url = part.getBytes("UTF-8");

                        arr_url = Arrays.copyOfRange(arr_url, 0, 32);


                        String encryptedHex = "";
                        for (int i = 0; i < arr_url.length; i++) {

                            byte[] curByte = new byte[1];
                            curByte[0] = (byte) (arr_url[i] - 0x30);


                            String hex = StringFunction.getHexString(curByte);
                            if (hex.substring(0, 1).equals("0"))
                                hex = hex.substring(1);

                            encryptedHex += hex;

                        }
                        txtEncHex.setText(encryptedHex);


                        byte[] key = StringFunction.hexStringToByteArray(edtKey.getText().toString());


                        byte[] hash = StringFunction.hexStringToByteArray(encryptedHex);


                        byte[] decrypted = AES.decrypt(hash, key);

                        if (decrypted != null && decrypted.length > 0) {

                            String decryptedHex = StringFunction.getHexString(decrypted);

                            txtDecHex.setText(decryptedHex);


                            byte[] arr_serial = Arrays.copyOfRange(decrypted, 0, 4);
                            byte[] arr_count = Arrays.copyOfRange(decrypted, 10, 12);

                            int serial = ByteBuffer.wrap(arr_serial).getInt();

                            int count = ((arr_count[1] & 0xff) << 8) | (arr_count[0] & 0xff);

                            txtSerial.setText(serial + "");
                            txtCount.setText(count + "");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Intent intent = getIntent();
            Uri uri = intent.getData();                 // retrieve a Uri object instance or

            if (uri != null) {
                String uriString = intent.getDataString();
                this.edtURL.setText(uriString);
            }
        } catch (Exception e) {
            Toast.makeText(URL.this, "error on parsing detected url", Toast.LENGTH_SHORT).show();
        }
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

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(URL.this, Main.class);
            URL.this.startActivity(myIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
