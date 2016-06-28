package com.pfizer.actionForm;

import com.opensymphony.xwork2.ActionSupport;

public  class AddGuestForm extends ActionSupport
{
    private String nt_id;

    private String nt_domain;

    private String classid;

    private String enrolledby;

    private String email;

    private String lastname;

    private String firstname;

    private String emplid;

    public void setEmplid(String emplid)
    {
        this.emplid = emplid;
    }

    public String getEmplid()
    {
        return this.emplid;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getFirstname()
    {
        return this.firstname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getLastname()
    {
        return this.lastname;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEnrolledby(String enrolledby)
    {
        this.enrolledby = enrolledby;
    }

    public String getEnrolledby()
    {
        return this.enrolledby;
    }

    public void setClassid(String classid)
    {
        this.classid = classid;
    }

    public String getClassid()
    {
        return this.classid;
    }

    public void setNt_domain(String nt_domain)
    {
        this.nt_domain = nt_domain;
    }

    public String getNt_domain()
    {
        return this.nt_domain;
    }

    public void setNt_id(String nt_id)
    {
        this.nt_id = nt_id;
    }

    public String getNt_id()
    {
        return this.nt_id;
    }
}

