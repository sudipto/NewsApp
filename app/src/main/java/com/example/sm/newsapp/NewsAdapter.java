package com.example.sm.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by sm on 25/12/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private Context mContext;

    /** public constructor for the NewsAdapter  */
    public NewsAdapter (Context context, ArrayList<News> newsArrayList) {
        super(context,0,newsArrayList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.single_news_item,parent,false);

        //  Get the current news to be diplayed
        News news = getItem(position);

        //  Get the textviews and imageview from the layout
        TextView section = (TextView) listItemView.findViewById(R.id.section);
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        TextView contributors = (TextView) listItemView.findViewById(R.id.contributors);
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        TextView trailtext = (TextView) listItemView.findViewById(R.id.trail_text);
        TextView readTime = (TextView) listItemView.findViewById(R.id.reading_time);
        ImageView image = (ImageView) listItemView.findViewById(R.id.image);

        //  Set the section, title and date
        section.setText(" " + news.getSection() + " ");
        title.setText(news.getTitle());
        date.setText(news.getDate());

        //  Set the author names
        StringBuilder authors = new StringBuilder("by ");
        int i;

        ArrayList<String> authorNames = news.getAuthor();

        if (authorNames.size() != 0) {
            for ( i = 0; i < authorNames.size() - 1; i++) {
                authors.append(authorNames.get(i) + ",");
            }
            authors.append(authorNames.get(i) + ".");
            contributors.setText(authors);
        } else
            contributors.setVisibility(View.GONE);

        //  Set the trailing text
        if (news.getTrailText() != null)
            trailtext.setText(Html.fromHtml(Html.fromHtml(news.getTrailText()).toString()));
        else
            trailtext.setVisibility(View.INVISIBLE);

        //  Set the reading time
        if (news.getReadingTime() == 0)
            readTime.setText("1 min read");
        else {
            readTime.setText(news.getReadingTime() + " min read");
        }

        // Set the image.
        // This is the only place where we are using the Picasso Library by Square.
        Picasso.with(mContext).load(news.getImageUrl() /* get the image URL */)
                .error(R.drawable.img_not_available /* Use this drawable image when
                the image isn't available */)
                .fit()
                .centerCrop()   // scaleType
                .into(image);


        return listItemView;
    }
}
