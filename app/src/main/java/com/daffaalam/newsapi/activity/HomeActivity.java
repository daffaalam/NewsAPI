package com.daffaalam.newsapi.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daffaalam.newsapi.R;
import com.daffaalam.newsapi.adapter.HomeAdapter;
import com.daffaalam.newsapi.model.RootNews;
import com.daffaalam.newsapi.network.ClientAPI;
import com.daffaalam.newsapi.tools.Functions;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Functions implements View.OnClickListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, DialogInterface.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout srlHome;
    private Toolbar tbHome;
    private RelativeLayout rlHome;
    private RecyclerView rvHome;
    private SearchView svHome;
    private TextView tvHome;
    private FloatingActionButton fabHomePrevious;
    private FloatingActionButton fabHomeNext;
    private String que;
    private String language;
    private String key;
    private Integer pages;
    private Integer articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        srlHome = findViewById(R.id.srlHome);
        tbHome = findViewById(R.id.tbHome);
        rlHome = findViewById(R.id.rlHome);
        tvHome = findViewById(R.id.tvHome);
        rvHome = findViewById(R.id.rvHome);
        fabHomePrevious = findViewById(R.id.fabPrevious);
        fabHomeNext = findViewById(R.id.fabNext);

        que = "android";
        language = "id";
        pages = 1;
        key = "72f658843d634f6eb0984e516f8528a0";
        articles = 0;

        setSupportActionBar(tbHome);

        srlHome.setRefreshing(true);
        srlHome.setOnRefreshListener(this);

        tbHome.setTitle(R.string.app_name);
        tbHome.setNavigationIcon(R.mipmap.ic_launcher);
        tbHome.setNavigationOnClickListener(this);

        rvHome.setHasFixedSize(true);
        rvHome.setLayoutManager(new LinearLayoutManager(this));

        fabHomePrevious.setOnClickListener(this);
        fabHomeNext.setOnClickListener(this);

        rlHome.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.GONE);

        validFAB();

        loadNews(que, language, pages, key);
    }

    private void loadNews(final String q, final String lang, final Integer page, final String apiKey) {
        ClientAPI.retrofit().create(ClientAPI.EndPoint.class).newsAPI_everything(q, lang, page, apiKey).enqueue(new Callback<RootNews>() {
            @Override
            public void onResponse(Call<RootNews> call, Response<RootNews> response) {
                srlHome.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getTotalResults() != 0) {

                            articles = (response.body().getTotalResults() / 20) + 1;

                            tvHome.setVisibility(View.GONE);
                            rlHome.setVisibility(View.VISIBLE);

                            rvHome.setAdapter(new HomeAdapter(HomeActivity.this, response.body().getArticles()));

                            switch (lang) {
                                case "id":
                                    Objects.requireNonNull(getSupportActionBar()).setSubtitle("Indonesia - \"" + q + "\" - page " + page + " of " + articles);
                                    break;
                                case "en":
                                    Objects.requireNonNull(getSupportActionBar()).setSubtitle("English - \"" + q + "\" - page " + page + " of " + articles);
                                    break;
                            }

                            validFAB();

                        } else {
                            rlHome.setVisibility(View.GONE);
                            tvHome.setVisibility(View.VISIBLE);
                            myToast(getResources().getString(R.string.no_data_here));
                        }
                    } else {
                        rlHome.setVisibility(View.GONE);
                        tvHome.setVisibility(View.VISIBLE);
                        myToast("NULL DATA");
                    }
                } else {
                    rlHome.setVisibility(View.GONE);
                    tvHome.setVisibility(View.VISIBLE);
                    myToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<RootNews> call, Throwable t) {
                rlHome.setVisibility(View.GONE);
                tvHome.setVisibility(View.VISIBLE);
                srlHome.setRefreshing(false);
                myToast(t.getMessage());
            }
        });
    }

    private void validFAB() {

        if (pages <= 1) {
            fabHomePrevious.hide();
        } else {
            fabHomePrevious.show();
        }

        if (pages >= articles) {
            fabHomeNext.hide();
        } else {
            fabHomeNext.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        svHome = (SearchView) menu.findItem(R.id.searchMenu).getActionView();
        svHome.setQueryHint("search");
        svHome.setOnSearchClickListener(this);
        svHome.setOnQueryTextListener(this);

        menu.findItem(R.id.searchMenu).setOnActionExpandListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1:
                new AlertDialog.Builder(this).setTitle("Select your country").setItems(new String[]{"Indonesia", "English"}, this).show();
                break;
            case R.id.searchMenu:
                rlHome.setVisibility(View.GONE);
                tvHome.setVisibility(View.GONE);
                break;
            case R.id.fabPrevious:
                if (pages >= 2) {
                    pages = pages - 1;
                    rlHome.setVisibility(View.GONE);
                    tvHome.setVisibility(View.GONE);
                    srlHome.setRefreshing(true);
                    loadNews(que, language, pages, key);
                }
                break;
            case R.id.fabNext:
                if (pages != 0) {
                    pages = pages + 1;
                    rlHome.setVisibility(View.GONE);
                    tvHome.setVisibility(View.GONE);
                    srlHome.setRefreshing(true);
                    loadNews(que, language, pages, key);
                }
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        que = query;
        pages = 1;
        srlHome.setRefreshing(true);
        loadNews(query, language, pages, key);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        rlHome.setVisibility(View.GONE);
        tvHome.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        rlHome.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                if (language.equals("en")) {
                    language = "id";
                    pages = 1;
                    rlHome.setVisibility(View.GONE);
                    tvHome.setVisibility(View.GONE);
                    srlHome.setRefreshing(true);
                    loadNews(que, language, pages, key);
                }
                break;
            case 1:
                if (language.equals("id")) {
                    language = "en";
                    pages = 1;
                    rlHome.setVisibility(View.GONE);
                    tvHome.setVisibility(View.GONE);
                    srlHome.setRefreshing(true);
                    loadNews(que, language, pages, key);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadNews(que, language, pages, key);
    }
}
