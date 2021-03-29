package com.dc.crud_firestore.Model;

public class Buku {

    String id,judul,desc;

    //constructor kosong
    public Buku() {
    }
    //constructor yg data nya akan dipush ke db
    public Buku(String id, String judul, String desc) {
        this.id = id;
        this.judul = judul;
        this.desc = desc;
    }

    //setter and getter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
