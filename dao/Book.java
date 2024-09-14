package com.example.biblioteca_sicura.dao;

import javax.persistence.*;

@Entity
@Table(name = "libreria")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long isbn;
    private String titolo;
    private String autore;
    private String editore;
    private String img;
    public Book(){}
    public Book(String nome, String autore, String editore, String immagine) {

        this.titolo = nome;
        this.autore = autore;
        this.editore = editore;
        this.img = immagine;
    }
    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String nome) {
        this.titolo = nome;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String immagine) {
        this.img = immagine;
    }
}
