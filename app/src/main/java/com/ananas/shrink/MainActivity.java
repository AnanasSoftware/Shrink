package com.ananas.shrink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ananas.shrink.rest.Response;
import com.ananas.shrink.rest.ShrinkApi;
import com.ananas.shrink.utility.Database;
import com.ananas.shrink.utility.Utility;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnAbout;
    private Button btnShrink;
    private TextInputEditText long_link;
    private LinearLayout scrollViewContainer;
    private ShrinkApi shrinkApi;
    private Database database;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.btnAbout = findViewById(R.id.btnAbout);
        this.btnShrink = findViewById(R.id.btnShrink);
        this.long_link = findViewById(R.id.long_link);
        this.scrollViewContainer = findViewById(R.id.scrollViewContainer);
        database = new Database(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        shrinkApi = retrofit.create(ShrinkApi.class);
        initializeHandlers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadViewLinks();
    }
    
    private void initializeHandlers(){
        this.btnShrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                String long_link2 = MainActivity.this.long_link.getText().toString();
                if(!long_link2.equals("")){
                    if(Utility.isItLink(long_link2)){
                        if(Utility.isDeviceOnline(MainActivity.this)){
                            Dialog wdlg = Utility.waitDialog(MainActivity.this);
                            wdlg.show();
                            MainActivity.this.long_link.setEnabled(false);
                            MainActivity.this.shrinkApi.shrinkUrl(BuildConfig.API_KEY, long_link2).enqueue(new Callback<Response>() {

                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    if(!response.isSuccessful()){
                                        Toast.makeText(MainActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(response.body().getError() == 0){
                                            afterShrink(response.body().getUrl(), long_link2);
                                        } else {
                                            Toast.makeText(MainActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    wdlg.dismiss();
                                    MainActivity.this.long_link.setEnabled(true);
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    wdlg.dismiss();
                                    MainActivity.this.long_link.setEnabled(true);
                                    Toast.makeText(MainActivity.this, getString(R.string.may_no_connection), Toast.LENGTH_SHORT).show();
                                    t.printStackTrace();
                                }

                            });
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.add_real_link), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.add_link_bellow), Toast.LENGTH_SHORT).show();
                }
                view.setEnabled(true);
            }
        });
        this.btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void afterShrink(String short_link, String long_link){
        this.long_link.setText("");
        Toast.makeText(this, getString(R.string.link_shortened), Toast.LENGTH_SHORT).show();
        boolean ok = this.database.addNewLink(
                short_link,
                long_link,
                new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(new Date())
        );
        reloadViewLinks();
    }
    
    private void loadLinks(){
        Cursor links = database.loadLinks();
        if(links.getCount() > 0){
            findViewById(R.id.noLinksYet).setVisibility(View.GONE);
            while(links.moveToNext()){
                String id = links.getString(0);
                String long_link = links.getString(1);
                String short_link = links.getString(2);
                addSingleLink(id, short_link, long_link);
            }
        } else {
            findViewById(R.id.noLinksYet).setVisibility(View.VISIBLE);
        }
    }
    
    private void addSingleLink(String id, String short_link, String long_link){
        scrollViewContainer.addView(
                Utility.genLinkItem(
                        this.getApplicationContext(),
                        short_link,
                        long_link,
                        view -> {
                            Dialog dialog = Utility.linkInformationDialog(MainActivity.this, id, long_link, short_link);
                            dialog.findViewById(R.id.dbtnDelete).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    database.deleteLink(Integer.parseInt(id));
                                    reloadViewLinks();
                                    Toast.makeText(MainActivity.this, getString(R.string.link_deleted), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                )
        );
    }

    private void removeViewLinks(){
        scrollViewContainer.removeAllViews();
    }

    private void reloadViewLinks(){
        removeViewLinks();
        loadLinks();
    }
    
}
