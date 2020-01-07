package com.example.madcamp2;

public class wContents {
    private String id;
    private String outer;
    private String inner;
    private String feedback;
    private int goodnum;

    public wContents(String _id, String _outer, String _inner, String _feedback, int _goodnum) {
        id=_id;
        outer = _outer;
        inner = _inner;
        feedback = _feedback;
        goodnum = _goodnum;
    }

    public String getId() {return id;}

    public String getOuter() {return outer;}

    public String getInner() {return inner;}

    public String getFeedback() {return feedback;}

    public int getGoodnum() {return goodnum;}

}
