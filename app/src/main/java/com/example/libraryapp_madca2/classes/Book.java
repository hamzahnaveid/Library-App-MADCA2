package com.example.libraryapp_madca2.classes;

public class Book {

    int id;
    String title;
    String author;
    String category;
    String startDate;
    String review;
    String status;
    int favourite;

    public Book(int id, String title, String author, String category, String startDate, String review, String status, int favourite) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.startDate = startDate;
        this.review = review;
        this.status = status;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String toString() {
        return "Title: " + this.title + "\n" +
                "Author " + this.author + "\n\n";
    }

}
