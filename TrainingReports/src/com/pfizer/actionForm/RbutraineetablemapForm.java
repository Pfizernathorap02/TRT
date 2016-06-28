package com.pfizer.actionForm;

import com.opensymphony.xwork2.ActionSupport;

public  class RbutraineetablemapForm extends ActionSupport
{
    private String room_id;

    private String week_id;

    private String class_id;

    private String table_id;

    private String room;

    private String product;

    private String day;

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getDay()
    {
        // For data binding to be able to post data back, complex types and
        // arrays must be initialized to be non-null. This type doesn't have
        // a default constructor, so Workshop cannot initialize it for you.

        // TODO: Initialize day if it is null.
        //if(this.day == null)
        //{
        //    this.day = new Date(?);
        //}

        return this.day;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getProduct()
    {
        return this.product;
    }

    public void setRoom(String room)
    {
        this.room = room;
    }

    public String getRoom()
    {
        return this.room;
    }

    public void setTable_id(String table_id)
    {
        this.table_id = table_id;
    }

    public String getTable_id()
    {
        return this.table_id;
    }

    public void setClass_id(String class_id)
    {
        this.class_id = class_id;
    }

    public String getClass_id()
    {
        return this.class_id;
    }

    public void setWeek_id(String week_id)
    {
        this.week_id = week_id;
    }

    public String getWeek_id()
    {
        return this.week_id;
    }

    public void setRoom_id(String room_id)
    {
        this.room_id = room_id;
    }

    public String getRoom_id()
    {
        return this.room_id;
    }
}
