package com.dmos5.github_dmos5.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dmos5.github_dmos5.R;
import com.dmos5.github_dmos5.api.RetrofitService;
import com.dmos5.github_dmos5.model.Repository;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dmos5.github_dmos5.constants.Constants.URL_GITHUB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText edtUsername;
    private Button btnSearch;
    private ImageView mImageView;

    private RecyclerView mRecyclerView;
    private AdapterRepository adapter;

    private List<Repository> mRepositoryist;

    private static final int REQUEST_PERMISSION = 64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername   = findViewById(R.id.edt_username);
        btnSearch     = findViewById(R.id.btn_search);
        mImageView    = findViewById(R.id.img_empty);
        mRecyclerView = findViewById(R.id.rcview_repo);

        adapter = new AdapterRepository(mRepositoryist, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        btnSearch.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        String username;

        if(v == btnSearch){
            if ( permissionVerify() ) {
                username = edtUsername.getText().toString(); // get username
                if (!username.isEmpty()) {
                    searchRepository(username);
                }
                else {
                    mImageView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(this, R.string.hint_username, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                requestPermission();
            }
        }

    }

    private boolean permissionVerify() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void searchRepository(String username) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL_GITHUB).addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<List<Repository>> repos = service.getRepository(username);

        repos.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.isSuccessful()) {
                    List<Repository> repos = response.body();
                    adapter.update(repos, mImageView, mRecyclerView);
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
               Toast.makeText(MainActivity.this, R.string.request_falied, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermission() {
        final Activity activity = this;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage(R.string.permission_allow)
                    .setPositiveButton(R.string.permission_allow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                activity,
                                new String[]{
                                        Manifest.permission.INTERNET
                                },
                                REQUEST_PERMISSION
                            );
                        }
                    })
                    .setNegativeButton(R.string.permission_deny, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.INTERNET
                },
                REQUEST_PERMISSION
            );
        }
    }

}