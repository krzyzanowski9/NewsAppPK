package com.example.android.newsapppk;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> NewsList) {
        super(context, 0, NewsList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        TextView articleTitle = convertView.findViewById(R.id.article_title);
        TextView articleAuthor = convertView.findViewById(R.id.article_author);
        TextView articleDate = convertView.findViewById(R.id.article_date);

        News currentnews = getItem(position);

        articleAuthor.setText(currentnews.getArticleAuthor());
        articleDate.setText(currentnews.getArticleDate());
        articleTitle.setText(currentnews.getArticleTitle());

        // Return the whole list item layout
        return listItemView;
    }
}


