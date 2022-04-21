package com.jonathan.pam_tas.models;

public class CartModel {

    String productName;
    String productPrice;
    String recipient;
    String letter;
    String img_url;
    String documentId;



    public CartModel(){

    }

    public CartModel(String productName, String productPrice, String recipient, String letter, String img_url) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.recipient = recipient;
        this.letter = letter;
        this.img_url = img_url;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
