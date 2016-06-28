package com.pfizer.db; 

import java.util.Date;

public class P2LRegistration
    {
        private Integer createRegistration;

        private String ssActivityStartDate;

        private String ssActivityCode;

        private String notes;

        private Integer status;

        private String timezone;

        private String currency;

        private Float cost;

        private String paymentTerm;

        private String cancellationDate;

        private Integer passed;

        private Float score;

        private String launchDate;

        private String completionDate;

        private String registrationDate;

        private String startDate;

        private String classCode;

        private Integer empNumber;

        public void setEmpNumber(Integer empNumber)
        {
            this.empNumber = empNumber;
        }

        public Integer getEmpNumber()
        {
            return this.empNumber;
        }

        public void setClassCode(String classCode)
        {
            this.classCode = classCode;
        }

        public String getClassCode()
        {
            return this.classCode;
        }

        public void setStartDate(String startDate)
        {
            this.startDate = startDate;
        }

        public String getStartDate()
        {
            return this.startDate;
        }

        public void setRegistrationDate(String registrationDate)
        {
            this.registrationDate = registrationDate;
        }

        public String getRegistrationDate()
        {
            return this.registrationDate;
        }

        public void setCompletionDate(String completionDate)
        {
            this.completionDate = completionDate;
        }

        public String getCompletionDate()
        {
            return this.completionDate;
        }

        public void setLaunchDate(String launchDate)
        {
            this.launchDate = launchDate;
        }

        public String getLaunchDate()
        {
            return this.launchDate;
        }

        public void setScore(Float score)
        {
            this.score = score;
        }

        public Float getScore()
        {
            return this.score;
        }

        public void setPassed(Integer passed)
        {
            this.passed = passed;
        }

        public Integer getPassed()
        {
            return this.passed;
        }

        public void setCancellationDate(String cancellationDate)
        {
            this.cancellationDate = cancellationDate;
        }

        public String getCancellationDate()
        {
           return this.cancellationDate;
        }

        public void setPaymentTerm(String paymentTerm)
        {
            this.paymentTerm = paymentTerm;
        }

        public String getPaymentTerm()
        {
            return this.paymentTerm;
        }

        public void setCost(Float cost)
        {
            this.cost = cost;
        }

        public Float getCost()
        {
            return this.cost;
        }

        public void setCurrency(String currency)
        {
            this.currency = currency;
        }

        public String getCurrency()
        {
            return this.currency;
        }

        public void setTimezone(String timezone)
        {
            this.timezone = timezone;
        }

        public String getTimezone()
        {
            return this.timezone;
        }

        public void setStatus(Integer status)
        {
            this.status = status;
        }

        public Integer getStatus()
        {
            return this.status;
        }

        public void setNotes(String notes)
        {
            this.notes = notes;
        }

        public String getNotes()
        {
            return this.notes;
        }

        public void setSsActivityCode(String ssActivityCode)
        {
            this.ssActivityCode = ssActivityCode;
        }

        public String getSsActivityCode()
        {
            return this.ssActivityCode;
        }

        public void setSsActivityStartDate(String ssActivityStartDate)
        {
            this.ssActivityStartDate = ssActivityStartDate;
        }

        public String getSsActivityStartDate()
        {
            return this.ssActivityStartDate;
        }

        public void setCreateRegistration(Integer createRegistration)
        {
            this.createRegistration = createRegistration;
        }

        public Integer getCreateRegistration()
        {
            return this.createRegistration;
        }
    }