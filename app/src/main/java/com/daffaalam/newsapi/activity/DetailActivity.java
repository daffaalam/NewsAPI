package com.daffaalam.newsapi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daffaalam.newsapi.R;
import com.daffaalam.newsapi.tools.Functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DetailActivity extends Functions implements View.OnClickListener {

    Toolbar tbDetail;
    Bundle bundleDetail;
    ImageView ivDetail;
    TextView tvDetailTitle;
    TextView tvDetailSubTitle;
    TextView tvDetailContent;
    TextView tvDetailMore;
    Date dateConvert;
    String subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("");

        bundleDetail = getIntent().getExtras();
        tbDetail = findViewById(R.id.tbDetail);
        ivDetail = findViewById(R.id.ivDetail);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailSubTitle = findViewById(R.id.tvDetailSubTitle);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvDetailMore = findViewById(R.id.tvDetailMore);

        setSupportActionBar(tbDetail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(bundleDetail.getString(IMAGE)).into(ivDetail);

        try {
            dateConvert = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(bundleDetail.getString(DATE));
        } catch (ParseException e) {
            dateConvert = null;
        }

        subTitle = bundleDetail.getString(AUTHOR) + "\n" + new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US).format(dateConvert);

        tvDetailTitle.setText(bundleDetail.getString(TITLE));
        tvDetailSubTitle.setText(subTitle);
        tvDetailContent.setText(bundleDetail.getString(DESC));

        tvDetailMore.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                startActivity(Intent.createChooser(new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, bundleDetail.getString(TITLE) + "\n" + bundleDetail.getString(URL)).setType("text/plain"), "Share this article"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDetailMore:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bundleDetail.getString(URL))));
                break;
        }
    }
}
