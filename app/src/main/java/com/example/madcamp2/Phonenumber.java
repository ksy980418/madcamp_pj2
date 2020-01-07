package com.example.madcamp2;

class Phonenumber {
    String name;
    String number;
    String photo_id;
    String photo_url;
    String photo_thum_id;
    String person_id;

    public Phonenumber(String name, String number, String photo_id, String photo_thum_id, String person_id, String photo_url) {
        this.name = name;
        this.number = number;
        this.photo_id=photo_id;
        this.photo_thum_id=photo_thum_id;
        this.person_id=person_id;
        this.photo_url=photo_url;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public String getPhoto_id() {
        return this.photo_id;
    }

    public String getPerson_id() {
        return this.person_id;
    }

    public String getPhoto_url() {
        return this.photo_url;
    }
    public String getPhoto_thum_id() {
        return this.photo_thum_id;
    }


}