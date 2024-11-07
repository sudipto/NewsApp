package com.example.sm.newsapp;

import java.util.ArrayList;

/**
 * Created by sm on 25/12/17.
 */

public class News {

    /** Title of the news article   */
    private String mTitle;

    /** Name of section */
    private String mSection;

    /** Authors of the article   */
    private ArrayList<String> mAuthor = new ArrayList<>();

    /** Date of the article */
    private StringBuilder mDate;

    /** Url of the article  */
    private String mUrl;

    /** TrailText of the article    */
    private String mTrailText;

    /** Wordcount of the article    */
    private int mWordcount;

    /** Image/Thumbnail Url of the article */
    private String mThumbnailUrl;

    /** Constructor to create a news article    */
    public News (String title, String section, ArrayList<String> author,
                 StringBuilder date, String url, String trailText, int wordcount, String imageUrl) {
        mTitle = title;
        mSection = section;
        mAuthor.addAll(author);
        mDate = date;
        mUrl = url;
        mTrailText = trailText;
        mWordcount = wordcount;
        mThumbnailUrl = imageUrl;
    }

    /** Get the article title   */
    public String getTitle() {
        return mTitle;
    }

    /** Get the section the article belongs to  */
    public String getSection() {
        return mSection;
    }

    /** Get the name of the authors  */
    public ArrayList<String> getAuthor() {
        return mAuthor;
    }

    /** Get the date of publication */
    public StringBuilder getDate() {
        return mDate;
    }

    /** Get the URL of the news article */
    public String getUrl() {
        return mUrl;
    }

    /** Get the trailtext of the article    */
    public String getTrailText() {
        return mTrailText;
    }

    /** Get the image/thumbnail URL of the article */
    public String getImageUrl() {
        return mThumbnailUrl;
    }

    /**
     * Get the reading time reqd.
     * Instead of fetching the wordcount we directly get the reading time required.
     */
    public int getReadingTime() {
        return Math.round(mWordcount / 275);
    }
}
