package com.pfizer.actionForm;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

	public  class P4ClassRoomReportForm extends ActionSupport 
    {
        private List weeks = new ArrayList();

        private String week_id = "0";
        private String wave_id = "0";

        public void setWeek_id(String week_id)
        {
            this.week_id = week_id;
        }

        public String getWeek_id()
        {
            return this.week_id;
        }
        public void setWave_id(String wave_id)
        {
            this.wave_id = wave_id;
        }

        public String getWave_id()
        {
            return this.wave_id;
        }

        public void setWeeks(List weeks)
        {
            this.weeks = weeks;
        }

        public List getWeeks()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.weeks == null)
            {
                this.weeks = new ArrayList();
            }

            return this.weeks;
        }
    }
    
