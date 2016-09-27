package com.jzn.simplemvprxjavaretrofit.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiangzn on 16/9/21.
 */
public class BookInfo {
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String[] autor;
    @SerializedName("price")
    private String price;
    @SerializedName("pages")
    private String pages;
    @SerializedName("summary")
    private String detail;

    public BookInfo() {
    }

    public BookInfo(String title, String[] autor, String price, String pages, String detail) {
        this.title = title;
        this.autor = autor;
        this.price = price;
        this.pages = pages;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String[] getAutor() {
        return autor;
    }

    public void setAutor(String[] autor) {
        this.autor = autor;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "title='" + title + '\'' +
                ", autor='" + autor + '\'' +
                ", price='" + price + '\'' +
                ", pages='" + pages + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
