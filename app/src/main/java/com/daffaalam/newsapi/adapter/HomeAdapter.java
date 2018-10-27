package com.daffaalam.newsapi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daffaalam.newsapi.R;
import com.daffaalam.newsapi.activity.DetailActivity;
import com.daffaalam.newsapi.model.ArticlesItem;
import com.daffaalam.newsapi.tools.Functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ALL")
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private final Context contextHome;
    private final List<ArticlesItem> articlesItemList;
    private Date dateConvert;
    private Bundle bundle;
    private Pair<View, String> pairA;
    private Pair<View, String> pairB;

    public HomeAdapter(Context contextHome, List<ArticlesItem> articlesItemList) {
        this.contextHome = contextHome;
        this.articlesItemList = articlesItemList;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(contextHome).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeHolder holder, @SuppressLint("RecyclerView") final int position) {

        try {
            dateConvert = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(articlesItemList.get(position).getPublishedAt());
        } catch (ParseException e) {
            dateConvert = null;
        }

        if (articlesItemList.get(position).getUrlToImage() == null) {
            articlesItemList.get(position).setUrlToImage("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/No_image_3x4.svg/1280px-No_image_3x4.svg.png");
        }

        holder.tvItemHomeTitle.setText(articlesItemList.get(position).getTitle());
        holder.tvItemHomeAuthor.setText(articlesItemList.get(position).getAuthor());
        holder.tvItemHomePublished.setText(new SimpleDateFormat("EEE, dd MMM yy", Locale.US).format(dateConvert));

        Glide.with(contextHome).load(articlesItemList.get(position).getUrlToImage()).into(holder.ivItemHome);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString(Functions.SOURCE, articlesItemList.get(position).getSource().getName());
                bundle.putString(Functions.AUTHOR, articlesItemList.get(position).getAuthor());
                bundle.putString(Functions.TITLE, articlesItemList.get(position).getTitle());
                bundle.putString(Functions.DESC, articlesItemList.get(position).getDescription());
                bundle.putString(Functions.URL, articlesItemList.get(position).getUrl());
                bundle.putString(Functions.IMAGE, articlesItemList.get(position).getUrlToImage());
                bundle.putString(Functions.DATE, articlesItemList.get(position).getPublishedAt());
                bundle.putString(Functions.CONTENT, articlesItemList.get(position).getContent());
                pairA = Pair.create((View) holder.ivItemHome, "gambar");
                pairB = Pair.create((View) holder.tvItemHomeAuthor, "author");
                contextHome.startActivity(new Intent(contextHome, DetailActivity.class).putExtras(bundle), ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) contextHome, pairA, pairB).toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesItemList.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {

        final ImageView ivItemHome;
        final TextView tvItemHomeTitle;
        final TextView tvItemHomeAuthor;
        final TextView tvItemHomePublished;

        HomeHolder(View itemView) {
            super(itemView);
            ivItemHome = itemView.findViewById(R.id.ivItemHome);
            tvItemHomeTitle = itemView.findViewById(R.id.tvItemHomeTitle);
            tvItemHomeAuthor = itemView.findViewById(R.id.tvItemHomeAuthor);
            tvItemHomePublished = itemView.findViewById(R.id.tvItemHomePublished);
        }
    }
}
