package com.example.arjunvidyarthi.news;

import android.graphics.Bitmap;

/**
 * Created by Arjun Vidyarthi on 13-Oct-17.
 */

public class NewsData {

    String Title;
    String Author;
    String SectionAndDate;
    Bitmap NewsThumb;
    String webLink;

    public NewsData(String Title, String Author, String SectionandDate, Bitmap NewsThumb, String webLink){
        this.Title = Title;
        this.Author = Author;
        this.SectionAndDate = SectionandDate;
        this.NewsThumb = NewsThumb;
        this.webLink = webLink;

    }

    public String returnTitle(){
        return Title;
    }

    public String returnAuthor(){
        return Author;
    }

    public String returnSectionAndDate(){
        return SectionAndDate;
    }

    public Bitmap returnNewsThumb(){
        return NewsThumb;
    }

    public String returnWebLink(){return webLink; }
}
