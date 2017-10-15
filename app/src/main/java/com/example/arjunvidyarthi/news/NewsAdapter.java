package com.example.arjunvidyarthi.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arjun Vidyarthi on 13-Oct-17.
 */

public class NewsAdapter extends ArrayAdapter<NewsData> {
    public NewsAdapter(Activity context, ArrayList<NewsData> resource) {

        super(context, 0, resource);

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);

        NewsData currentNewsData = getItem(position);

        TextView Title = listItemView.findViewById(R.id.Title);
        Title.setText(currentNewsData.returnTitle());

        TextView Author = listItemView.findViewById(R.id.Author);
        Author.setText(currentNewsData.returnAuthor());

        TextView SectionAndDate = listItemView.findViewById(R.id.SectionAndDate);
        SectionAndDate.setText(currentNewsData.returnSectionAndDate());

        ImageView NewsThumb = listItemView.findViewById(R.id.NewsThumb);
        NewsThumb.setImageBitmap(currentNewsData.returnNewsThumb());

        return listItemView;


    }
}
