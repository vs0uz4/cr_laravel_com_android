package virtualsystems.com.br.financial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HttpClient httpClient = HttpClientBuilder.create().build();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_username);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_header_email);
        ImageView navAvatar = (ImageView) headerView.findViewById(R.id.nav_header_avatar);

        UserSession userSession = UserSession.getInstance(getApplicationContext());

        navUsername.setText(userSession.getUserName());
        navEmail.setText(userSession.getUserEmail());

        String imgURL = (String) userSession.getUserAvatar();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher_round);
        requestOptions.error(R.mipmap.ic_launcher_round);

        Glide.with(this)
                .load(imgURL)
                .apply(requestOptions)
                .into(navAvatar);

        displayFragment(R.id.nav_home);
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
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayFragment(id);

        return true;
    }

    private void displayFragment(int itemId){
        Fragment fragment = null;

        switch (itemId){
            case R.id.nav_categories:
                fragment = new Categories();
                break;
            case R.id.nav_home:
                fragment = new BillsTotal();
                break;
            case R.id.nav_bills:
                fragment = new Bills();
                break;
            case R.id.nav_logout:
                try {
                    logoutUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logoutUser() throws IOException {
        HttpPost clientPost = new HttpPost("http://192.168.254.8/api/logout");
        String responseMessage;

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(this).getUserToken());

        HttpResponse response = httpClient.execute(clientPost);
        Integer statusCode = response.getStatusLine().getStatusCode();

        switch (statusCode){
            case 204:
                responseMessage = "Disconected";
                break;
            case 400:
                responseMessage = "Bad Request - Invalid Token";
                break;
            case 401:
                responseMessage = "Unauthorized - Invalid Token";
                break;
            case 500:
                responseMessage = "Could not Invalidate Token";
                break;
            default:
                String errorCode = statusCode.toString();
                String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                responseMessage = "Error: " + errorCode + "\n" + errorMsg;
                break;
        }

        UserSession.getInstance(this).clearSession();
        Toast.makeText(this, responseMessage, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
