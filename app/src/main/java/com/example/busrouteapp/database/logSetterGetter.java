package com.example.busrouteapp.database;

import java.util.Arrays;

public class logSetterGetter {

    private String logDate, logTime, logValue, logAction, logStatus, logOwner, logReader;

    public logSetterGetter(String logDate, String logTime, String logValue, String logAction, String logStatus, String logOwner, String logReader){
        this.logDate = logDate;
        this.logTime = logTime;
        this.logValue = logValue;
        this.logAction = logAction;
        this.logStatus = logStatus;
        this.logOwner = logOwner;
        this.logReader = logReader;
    }

    @Override public String toString(){
        return logDate + " - " + logTime + "\n" + logValue + "\n" + logOwner + " - " + logReader + "\nStatus: " + logStatus;
    }

    public String getLogDate(){return logDate;}
    public String getLogTime(){return logTime;}
    public String getLogValue(){return logValue;}
    public String getLogAction(){return logAction;}
    public String getLogStatus(){return logStatus;}
    public String getLogOwner(){return logOwner;}
    public String getLogReader(){return logReader;}

    public void setLogDate(String logDate){this.logDate = logDate;}
    public void setLogTime(String logTime){this.logTime = logTime;}
    public void setLogValue(String logValue){this.logValue = logValue;}
    public void setLogAction(String logAction){this.logAction = logAction;}
    public void setLogStatus(String logStatus){this.logStatus = logStatus;}
    public void setLogOwner(String logOwner){this.logOwner = logOwner;}
    public void setLogReader(String logReader){this.logReader = logReader;}
}
