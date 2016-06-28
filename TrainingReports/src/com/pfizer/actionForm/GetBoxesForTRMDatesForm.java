package com.pfizer.actionForm;

import com.opensymphony.xwork2.ActionSupport;

public  class GetBoxesForTRMDatesForm extends ActionSupport
{
    private String selectedBox;

    private String selectedDate;

    public void setSelectedDate(String selectedDate)
    {
        this.selectedDate = selectedDate;
    }

    public String getSelectedDate()
    {
        return this.selectedDate;
    }

    public void setSelectedBox(String selectedBox)
    {
        this.selectedBox = selectedBox;
    }

    public String getSelectedBox()
    {
        return this.selectedBox;
    }
}