package com.example.ReceipeSearchPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ReceipeSearchPage.Adpater.AddressAdapter;
import com.example.ReceipeSearchPage.Adpater.FavAdapter;
import com.example.ReceipeSearchPage.Adpater.ProductAdapter;
import com.example.ReceipeSearchPage.Helper.Constaints;
import com.example.ReceipeSearchPage.Helper.DatabaseHelper;
import com.example.ReceipeSearchPage.Helper.DatabaseHelper2;
import com.example.ReceipeSearchPage.Helper.GetDataServices;
import com.example.ReceipeSearchPage.Helper.RetrofitClientInstance;
import com.example.ReceipeSearchPage.Helper.SharedPrefManager;
import com.example.ReceipeSearchPage.Model.ProductModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    androidx.appcompat.widget.SearchView searchView;
    ArrayList<ProductModel> arrayList=new ArrayList<>();
    RecyclerView recyclerView, favRec;
    ProductAdapter adapter;
    ProgressBar progressBar;
    ImageView menu;
    DrawerLayout drawerLayout;
    TextView appver;
    LinearLayout help,language;
    RadioGroup changeLanguage;
    String currentLanguage;
    public static FavAdapter adapter1;
    FloatingActionButton floating;
    public static RecyclerView recent_history,favorites;
    public static AddressAdapter addressAdapter;
    public static TextView noItem;
    ProgressDialog progressDialog;
    AlertDialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        searchView=findViewById(R.id.search);
        appver=findViewById(R.id.tv_app);
        language=findViewById(R.id.language);
        floating=findViewById(R.id.floating);
        recent_history=findViewById(R.id.recent_history);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recent_history.setLayoutManager(layoutManager);
        recent_history.setHasFixedSize(true);
        addressAdapter=new AddressAdapter(getApplicationContext(),new DatabaseHelper(getApplicationContext()).getAllHistory());
        recent_history.setAdapter(addressAdapter);
        if (Constaints.isNetworkConnected(this)) {

        } else {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Please connect your internet connection! ");
            builder.setTitle("No Internet");
            builder.show();
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create().dismiss();
                }
            });
        }

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder=new AlertDialog.Builder(MainActivity.this).create();
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.search_history,null);
                noItem=view.findViewById(R.id.no_item);

                favRec=view.findViewById(R.id.fav_recycler);
                RecyclerView.LayoutManager manager=new LinearLayoutManager(MainActivity.this);
                favRec.setLayoutManager(manager);
                favRec.setHasFixedSize(true);
                adapter1=new FavAdapter(new DatabaseHelper2(MainActivity.this).getAllHistory(),MainActivity.this);
                favRec.setAdapter(adapter1);
                if (adapter1.getItemCount()==0){
                    noItem.setVisibility(View.VISIBLE);
                }else {noItem.setVisibility(View.GONE);}

                builder.setView(view);
                builder.show();

                MyAsyncTasks myAsyncTasks=new MyAsyncTasks();
                myAsyncTasks.execute();




            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder=new AlertDialog.Builder(MainActivity.this).create();
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.language,null);
                changeLanguage=view.findViewById(R.id.radiogroup);
                changeLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId==R.id.us){
                            setAppLocale("us");
                        }if (checkedId==R.id.uk){
                            setAppLocale("uk");
                        }

                    }
                });
                builder.setView(view);
                builder.show();

            }
        });
        help=findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.help_layout,null);
                builder.setView(view);
                builder.show();
            }
        });
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        String versionName = BuildConfig.VERSION_NAME;
        appver.setText("App Version "+versionName);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        recyclerView=findViewById(R.id.recycler);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        progressBar=findViewById(R.id.progressBar);



        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);

                return false;
            }
        });
        getData();


    }
    public void getData(){
        progressBar.setVisibility(View.VISIBLE);
        GetDataServices getDataServices = RetrofitClientInstance.getRetrofitInstance().create(GetDataServices.class);
        Call<JsonObject> call=getDataServices.getData();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("res",response.body().toString());
                    JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                    JSONArray jsonArray=jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        ProductModel model=new ProductModel(
                                jsonObject1.getString("title"),
                                jsonObject1.getString("href"),
                                jsonObject1.getString("ingredients"),
                                jsonObject1.getString("thumbnail")
                        );
                        arrayList.add(model);
                        adapter=new ProductAdapter(arrayList,MainActivity.this);
                        recyclerView.setAdapter(adapter);

                    }
                }catch (Exception e){
                    //Toast.makeText(MainActivity.this,e.getMessage().toString() +"", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Toast.makeText(MainActivity.this,t.getMessage().toString()+ "", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);



            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawers();
        }else {
            super.onBackPressed();
        }

    }
    private void setAppLocale(String localeCode){
        if (!localeCode.equals(currentLanguage)){
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
            resources.updateConfiguration(configuration, displayMetrics);
            configuration.locale = new Locale(localeCode.toLowerCase());
            resources.updateConfiguration(configuration, displayMetrics);
            recreate();
            //SharedPrefLang.getInstance(this).appLanguage(localeCode);
            builder.dismiss();

        } else {
            Toast.makeText(this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(MainActivity.this);
//            progressDialog.setMessage("Please Wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            adapter1=new FavAdapter(new DatabaseHelper2(MainActivity.this).getAllHistory(),MainActivity.this);
            favRec.setAdapter(adapter1);
            if (adapter1.getItemCount()==0){
                noItem.setVisibility(View.VISIBLE);
            }else {noItem.setVisibility(View.GONE);}


            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            //progressDialog.dismiss();


        }


    }


}

