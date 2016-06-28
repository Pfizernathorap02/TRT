package com.tgix.printing;

import com.tgix.printing.LoggerHelper;
import com.tgix.printing.PrintingConstants;
import com.tgix.printing.VelocityConvertor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class PersonalizedAgendaBeanP4 {
    private String firstName;
	private String lastName;
	private String  templateBody;
    private String emplId;
    private String serverName;
        private String MondaySession1Product;
    private String MondaySession1Table;
    private String MondaySession1Room;
    private String MondaySession1StartTime;
    private String MondaySession1EndTime;
    private String MondaySession2Product;
    private String MondaySession2Table;
    private String MondaySession2Room;
    private String MondaySession2StartTime;
    private String MondaySession2EndTime;
    private String MondaySession3Product;
    private String MondaySession3Table;
    private String MondaySession3Room;
    private String MondaySession3StartTime;
    private String MondaySession3EndTime;
    private String MondaySession4Product;
    private String MondaySession4Table;
    private String MondaySession4Room;
    private String MondaySession4StartTime;
    private String MondaySession4EndTime;
    private String TuesdaySession1Product;
    private String TuesdaySession1Table;
    private String TuesdaySession1Room;
    private String TuesdaySession1StartTime;
    private String TuesdaySession1EndTime;
    private String TuesdaySession2Product;
    private String TuesdaySession2Table;
    private String TuesdaySession2Room;
    private String TuesdaySession2StartTime;
    private String TuesdaySession2EndTime;
    private String TuesdaySession3Product;
    private String TuesdaySession3Table;
    private String TuesdaySession3Room;
    private String TuesdaySession3StartTime;
    private String TuesdaySession3EndTime;
    private String TuesdaySession4Product;
    private String TuesdaySession4Table;
    private String TuesdaySession4Room;
    private String TuesdaySession4StartTime;
    private String TuesdaySession4EndTime;
                
    private String WednesdaySession1Product;
    private String WednesdaySession1Table;
    private String WednesdaySession1Room;
    private String WednesdaySession1StartTime;
    private String WednesdaySession1EndTime;
    private String WednesdaySession2Product;
    private String WednesdaySession2Table;
    private String WednesdaySession2Room;
    private String WednesdaySession2StartTime;
    private String WednesdaySession2EndTime;
    private String WednesdaySession3Product;
    private String WednesdaySession3Table;
    private String WednesdaySession3Room;
    private String WednesdaySession3StartTime;
    private String WednesdaySession3EndTime;
    private String WednesdaySession4Product;
    private String WednesdaySession4Table;
    private String WednesdaySession4Room;
    private String WednesdaySession4StartTime;
    private String WednesdaySession4EndTime;
    private String ThursdaySession1Product;
    private String ThursdaySession1Table;
    private String ThursdaySession1Room;
    private String ThursdaySession1StartTime;
    private String ThursdaySession1EndTime;
    private String ThursdaySession2Product;
    private String ThursdaySession2Table;
    private String ThursdaySession2Room;
    private String ThursdaySession2StartTime;
    private String ThursdaySession2EndTime;
    private String ThursdaySession3Product;
    private String ThursdaySession3Table;
    private String ThursdaySession3Room;
    private String ThursdaySession3StartTime;
    private String ThursdaySession3EndTime;
    private String ThursdaySession4Product;
    private String ThursdaySession4Table;
    private String ThursdaySession4Room;
    private String ThursdaySession4StartTime;
    private String ThursdaySession4EndTime;
    private String FridaySession1Product;
    private String FridaySession1Table;
    private String FridaySession1Room;
    private String FridaySession1StartTime;
    private String FridaySession1EndTime;
    private String FridaySession2Product;
    private String FridaySession2Table;
    private String FridaySession2Room;
    private String FridaySession2StartTime;
    private String FridaySession2EndTime;
    private String FridaySession3Product;
    private String FridaySession3Table;
    private String FridaySession3Room;
    private String FridaySession3StartTime;
    private String FridaySession3EndTime;
    private String FridaySession4Product;
    private String FridaySession4Table;
    private String FridaySession4Room;
    private String FridaySession4StartTime;
    private String FridaySession4EndTime;
    private String Week_Name;
	public String getWednesdaySession1Product() {
		return WednesdaySession1Product;
	}
	public void setWednesdaySession1Product(String WednesdaySession1Product) {
		WednesdaySession1Product = WednesdaySession1Product;
	}
	public String getWednesdaySession1Table() {
		return WednesdaySession1Table;
	}
	public void setWednesdaySession1Table(String WednesdaySession1Table) {
		WednesdaySession1Table = WednesdaySession1Table;
	}
	public String getWednesdaySession1Room() {
		return WednesdaySession1Room;
	}
	public void setWednesdaySession1Room(String WednesdaySession1Room) {
		WednesdaySession1Room = WednesdaySession1Room;
	}
	public String getWednesdaySession1StartTime() {
		return WednesdaySession1StartTime;
	}
	public void setWednesdaySession1StartTime(String WednesdaySession1StartTime) {
		WednesdaySession1StartTime = WednesdaySession1StartTime;
	}
	public String getWednesdaySession1EndTime() {
		return WednesdaySession1EndTime;
	}
	public void setWednesdaySession1EndTime(String WednesdaySession1EndTime) {
		WednesdaySession1EndTime = WednesdaySession1EndTime;
	}
	public String getWednesdaySession2Product() {
		return WednesdaySession2Product;
	}
	public void setWednesdaySession2Product(String WednesdaySession2Product) {
		WednesdaySession2Product = WednesdaySession2Product;
	}
	public String getWednesdaySession2Table() {
		return WednesdaySession2Table;
	}
	public void setWednesdaySession2Table(String WednesdaySession2Table) {
		WednesdaySession2Table = WednesdaySession2Table;
	}
	public String getWednesdaySession2Room() {
		return WednesdaySession2Room;
	}
	public void setWednesdaySession2Room(String WednesdaySession2Room) {
		WednesdaySession2Room = WednesdaySession2Room;
	}
	public String getWednesdaySession2StartTime() {
		return WednesdaySession2StartTime;
	}
	public void setWednesdaySession2StartTime(String WednesdaySession2StartTime) {
		WednesdaySession2StartTime = WednesdaySession2StartTime;
	}
	public String getWednesdaySession2EndTime() {
		return WednesdaySession2EndTime;
	}
	public void setWednesdaySession2EndTime(String WednesdaySession2EndTime) {
		WednesdaySession2EndTime = WednesdaySession2EndTime;
	}
	public String getWednesdaySession3Product() {
		return WednesdaySession3Product;
	}
	public void setWednesdaySession3Product(String WednesdaySession3Product) {
		WednesdaySession3Product = WednesdaySession3Product;
	}
	public String getWednesdaySession3Table() {
		return WednesdaySession3Table;
	}
	public void setWednesdaySession3Table(String WednesdaySession3Table) {
		WednesdaySession3Table = WednesdaySession3Table;
	}
	public String getWednesdaySession3Room() {
		return WednesdaySession3Room;
	}
	public void setWednesdaySession3Room(String WednesdaySession3Room) {
		WednesdaySession3Room = WednesdaySession3Room;
	}
	public String getWednesdaySession3StartTime() {
		return WednesdaySession3StartTime;
	}
	public void setWednesdaySession3StartTime(String WednesdaySession3StartTime) {
		WednesdaySession3StartTime = WednesdaySession3StartTime;
	}
	public String getWednesdaySession3EndTime() {
		return WednesdaySession3EndTime;
	}
	public void setWednesdaySession3EndTime(String WednesdaySession3EndTime) {
		WednesdaySession3EndTime = WednesdaySession3EndTime;
	}
	public String getWednesdaySession4Product() {
		return WednesdaySession4Product;
	}
	public void setWednesdaySession4Product(String WednesdaySession4Product) {
		WednesdaySession4Product = WednesdaySession4Product;
	}
	public String getWednesdaySession4Table() {
		return WednesdaySession4Table;
	}
	public void setWednesdaySession4Table(String WednesdaySession4Table) {
		WednesdaySession4Table = WednesdaySession4Table;
	}
	public String getWednesdaySession4Room() {
		return WednesdaySession4Room;
	}
	public void setWednesdaySession4Room(String WednesdaySession4Room) {
		WednesdaySession4Room = WednesdaySession4Room;
	}
	public String getWednesdaySession4StartTime() {
		return WednesdaySession4StartTime;
	}
	public void setWednesdaySession4StartTime(String WednesdaySession4StartTime) {
		WednesdaySession4StartTime = WednesdaySession4StartTime;
	}
	public String getWednesdaySession4EndTime() {
		return WednesdaySession4EndTime;
	}
	public void setWednesdaySession4EndTime(String WednesdaySession4EndTime) {
		WednesdaySession4EndTime = WednesdaySession4EndTime;
	}
	public String getThursdaySession1Product() {
		return ThursdaySession1Product;
	}
	public void setThursdaySession1Product(String thursdaySession1Product) {
		ThursdaySession1Product = thursdaySession1Product;
	}
	public String getThursdaySession1Table() {
		return ThursdaySession1Table;
	}
	public void setThursdaySession1Table(String thursdaySession1Table) {
		ThursdaySession1Table = thursdaySession1Table;
	}
	public String getThursdaySession1Room() {
		return ThursdaySession1Room;
	}
	public void setThursdaySession1Room(String thursdaySession1Room) {
		ThursdaySession1Room = thursdaySession1Room;
	}
	public String getThursdaySession1StartTime() {
		return ThursdaySession1StartTime;
	}
	public void setThursdaySession1StartTime(String thursdaySession1StartTime) {
		ThursdaySession1StartTime = thursdaySession1StartTime;
	}
	public String getThursdaySession1EndTime() {
		return ThursdaySession1EndTime;
	}
	public void setThursdaySession1EndTime(String thursdaySession1EndTime) {
		ThursdaySession1EndTime = thursdaySession1EndTime;
	}
	public String getThursdaySession2Product() {
		return ThursdaySession2Product;
	}
	public void setThursdaySession2Product(String thursdaySession2Product) {
		ThursdaySession2Product = thursdaySession2Product;
	}
	public String getThursdaySession2Table() {
		return ThursdaySession2Table;
	}
	public void setThursdaySession2Table(String thursdaySession2Table) {
		ThursdaySession2Table = thursdaySession2Table;
	}
	public String getThursdaySession2Room() {
		return ThursdaySession2Room;
	}
	public void setThursdaySession2Room(String thursdaySession2Room) {
		ThursdaySession2Room = thursdaySession2Room;
	}
	public String getThursdaySession2StartTime() {
		return ThursdaySession2StartTime;
	}
	public void setThursdaySession2StartTime(String thursdaySession2StartTime) {
		ThursdaySession2StartTime = thursdaySession2StartTime;
	}
	public String getThursdaySession2EndTime() {
		return ThursdaySession2EndTime;
	}
	public void setThursdaySession2EndTime(String thursdaySession2EndTime) {
		ThursdaySession2EndTime = thursdaySession2EndTime;
	}
	public String getThursdaySession3Product() {
		return ThursdaySession3Product;
	}
	public void setThursdaySession3Product(String thursdaySession3Product) {
		ThursdaySession3Product = thursdaySession3Product;
	}
	public String getThursdaySession3Table() {
		return ThursdaySession3Table;
	}
	public void setThursdaySession3Table(String thursdaySession3Table) {
		ThursdaySession3Table = thursdaySession3Table;
	}
	public String getThursdaySession3Room() {
		return ThursdaySession3Room;
	}
	public void setThursdaySession3Room(String thursdaySession3Room) {
		ThursdaySession3Room = thursdaySession3Room;
	}
	public String getThursdaySession3StartTime() {
		return ThursdaySession3StartTime;
	}
	public void setThursdaySession3StartTime(String thursdaySession3StartTime) {
		ThursdaySession3StartTime = thursdaySession3StartTime;
	}
	public String getThursdaySession3EndTime() {
		return ThursdaySession3EndTime;
	}
	public void setThursdaySession3EndTime(String thursdaySession3EndTime) {
		ThursdaySession3EndTime = thursdaySession3EndTime;
	}
	public String getThursdaySession4Product() {
		return ThursdaySession4Product;
	}
	public void setThursdaySession4Product(String thursdaySession4Product) {
		ThursdaySession4Product = thursdaySession4Product;
	}
	public String getThursdaySession4Table() {
		return ThursdaySession4Table;
	}
	public void setThursdaySession4Table(String thursdaySession4Table) {
		ThursdaySession4Table = thursdaySession4Table;
	}
	public String getThursdaySession4Room() {
		return ThursdaySession4Room;
	}
	public void setThursdaySession4Room(String thursdaySession4Room) {
		ThursdaySession4Room = thursdaySession4Room;
	}
	public String getThursdaySession4StartTime() {
		return ThursdaySession4StartTime;
	}
	public void setThursdaySession4StartTime(String thursdaySession4StartTime) {
		ThursdaySession4StartTime = thursdaySession4StartTime;
	}
	public String getThursdaySession4EndTime() {
		return ThursdaySession4EndTime;
	}
	public void setThursdaySession4EndTime(String thursdaySession4EndTime) {
		ThursdaySession4EndTime = thursdaySession4EndTime;
	}
	public String getFridaySession1Product() {
		return FridaySession1Product;
	}
	public void setFridaySession1Product(String fridaySession1Product) {
		FridaySession1Product = fridaySession1Product;
	}
	public String getFridaySession1Table() {
		return FridaySession1Table;
	}
	public void setFridaySession1Table(String fridaySession1Table) {
		FridaySession1Table = fridaySession1Table;
	}
	public String getFridaySession1Room() {
		return FridaySession1Room;
	}
	public void setFridaySession1Room(String fridaySession1Room) {
		FridaySession1Room = fridaySession1Room;
	}
	public String getFridaySession1StartTime() {
		return FridaySession1StartTime;
	}
	public void setFridaySession1StartTime(String fridaySession1StartTime) {
		FridaySession1StartTime = fridaySession1StartTime;
	}
	public String getFridaySession1EndTime() {
		return FridaySession1EndTime;
	}
	public void setFridaySession1EndTime(String fridaySession1EndTime) {
		FridaySession1EndTime = fridaySession1EndTime;
	}
	public String getFridaySession2Product() {
		return FridaySession2Product;
	}
	public void setFridaySession2Product(String fridaySession2Product) {
		FridaySession2Product = fridaySession2Product;
	}
	public String getFridaySession2Table() {
		return FridaySession2Table;
	}
	public void setFridaySession2Table(String fridaySession2Table) {
		FridaySession2Table = fridaySession2Table;
	}
	public String getFridaySession2Room() {
		return FridaySession2Room;
	}
	public void setFridaySession2Room(String fridaySession2Room) {
		FridaySession2Room = fridaySession2Room;
	}
	public String getFridaySession2StartTime() {
		return FridaySession2StartTime;
	}
	public void setFridaySession2StartTime(String fridaySession2StartTime) {
		FridaySession2StartTime = fridaySession2StartTime;
	}
	public String getFridaySession2EndTime() {
		return FridaySession2EndTime;
	}
	public void setFridaySession2EndTime(String fridaySession2EndTime) {
		FridaySession2EndTime = fridaySession2EndTime;
	}
	public String getFridaySession3Product() {
		return FridaySession3Product;
	}
	public void setFridaySession3Product(String fridaySession3Product) {
		FridaySession3Product = fridaySession3Product;
	}
	public String getFridaySession3Table() {
		return FridaySession3Table;
	}
	public void setFridaySession3Table(String fridaySession3Table) {
		FridaySession3Table = fridaySession3Table;
	}
	public String getFridaySession3Room() {
		return FridaySession3Room;
	}
	public void setFridaySession3Room(String fridaySession3Room) {
		FridaySession3Room = fridaySession3Room;
	}
	public String getFridaySession3StartTime() {
		return FridaySession3StartTime;
	}
	public void setFridaySession3StartTime(String fridaySession3StartTime) {
		FridaySession3StartTime = fridaySession3StartTime;
	}
	public String getFridaySession3EndTime() {
		return FridaySession3EndTime;
	}
	public void setFridaySession3EndTime(String fridaySession3EndTime) {
		FridaySession3EndTime = fridaySession3EndTime;
	}
	public String getFridaySession4Product() {
		return FridaySession4Product;
	}
	public void setFridaySession4Product(String fridaySession4Product) {
		FridaySession4Product = fridaySession4Product;
	}
	public String getFridaySession4Table() {
		return FridaySession4Table;
	}
	public void setFridaySession4Table(String fridaySession4Table) {
		FridaySession4Table = fridaySession4Table;
	}
	public String getFridaySession4Room() {
		return FridaySession4Room;
	}
	public void setFridaySession4Room(String fridaySession4Room) {
		FridaySession4Room = fridaySession4Room;
	}
	public String getFridaySession4StartTime() {
		return FridaySession4StartTime;
	}
	public void setFridaySession4StartTime(String fridaySession4StartTime) {
		FridaySession4StartTime = fridaySession4StartTime;
	}
	public String getFridaySession4EndTime() {
		return FridaySession4EndTime;
	}
	public void setFridaySession4EndTime(String fridaySession4EndTime) {
		FridaySession4EndTime = fridaySession4EndTime;
	}
	public String getWeek_Name() {
		return Week_Name;
	}
	public void setWeek_Name(String week_Name) {
		Week_Name = week_Name;
	}
    
    	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTemplateBody() {
		return templateBody;
	}
	public void setTemplateBody(String templateBody) {
		this.templateBody = templateBody;
	}
	public String getEmplId() {
		return emplId;
	}
	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
    
    private String getPadding(int size) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size;i++) {
            sb.append("");
        }
        
        return sb.toString();
    }  
    public String getMondaySession1Product() {

		return MondaySession1Product;
	}
	public void setMondaySession1Product(String mondaySession1Product) {
		MondaySession1Product = mondaySession1Product;
	}
	public String getMondaySession1Table() {
        if (MondaySession1Table == null ) {
            MondaySession1Table = "&nbsp;";
        }
        if (MondaySession1Table.length() == 0 ) {
            MondaySession1Table = "&nbsp;";
        }
		return MondaySession1Table;
	}
	public void setMondaySession1Table(String mondaySession1Table) {
		MondaySession1Table = mondaySession1Table;
	}
	public String getMondaySession1Room() {
		return MondaySession1Room;
	}
	public void setMondaySession1Room(String mondaySession1Room) {
		MondaySession1Room = mondaySession1Room;
	}
	public String getMondaySession1StartTime() {
		return MondaySession1StartTime;
	}
	public void setMondaySession1StartTime(String mondaySession1StartTime) {
		MondaySession1StartTime = mondaySession1StartTime;
	}
	public String getMondaySession1EndTime() {
		return MondaySession1EndTime;
	}
	public void setMondaySession1EndTime(String mondaySession1EndTime) {
		MondaySession1EndTime = mondaySession1EndTime;
	}
	public String getMondaySession2Product() {
		return MondaySession2Product;
	}
	public void setMondaySession2Product(String mondaySession2Product) {
		MondaySession2Product = mondaySession2Product;
	}
	public String getMondaySession2Table() {
		return MondaySession2Table;
	}
	public void setMondaySession2Table(String mondaySession2Table) {
		MondaySession2Table = mondaySession2Table;
	}
	public String getMondaySession2Room() {
		return MondaySession2Room;
	}
	public void setMondaySession2Room(String mondaySession2Room) {
		MondaySession2Room = mondaySession2Room;
	}
	public String getMondaySession2StartTime() {
		return MondaySession2StartTime;
	}
	public void setMondaySession2StartTime(String mondaySession2StartTime) {
		MondaySession2StartTime = mondaySession2StartTime;
	}
	public String getMondaySession2EndTime() {
		return MondaySession2EndTime;
	}
	public void setMondaySession2EndTime(String mondaySession2EndTime) {
		MondaySession2EndTime = mondaySession2EndTime;
	}
	public String getMondaySession3Product() {
		return MondaySession3Product;
	}
	public void setMondaySession3Product(String mondaySession3Product) {
		MondaySession3Product = mondaySession3Product;
	}
	public String getMondaySession3Table() {
		return MondaySession3Table;
	}
	public void setMondaySession3Table(String mondaySession3Table) {
		MondaySession3Table = mondaySession3Table;
	}
	public String getMondaySession3Room() {
		return MondaySession3Room;
	}
	public void setMondaySession3Room(String mondaySession3Room) {
		MondaySession3Room = mondaySession3Room;
	}
	public String getMondaySession3StartTime() {
		return MondaySession3StartTime;
	}
	public void setMondaySession3StartTime(String mondaySession3StartTime) {
		MondaySession3StartTime = mondaySession3StartTime;
	}
	public String getMondaySession3EndTime() {
		return MondaySession3EndTime;
	}
	public void setMondaySession3EndTime(String mondaySession3EndTime) {
		MondaySession3EndTime = mondaySession3EndTime;
	}
	public String getMondaySession4Product() {
		return MondaySession4Product;
	}
	public void setMondaySession4Product(String mondaySession4Product) {
		MondaySession4Product = mondaySession4Product;
	}
	public String getMondaySession4Table() {
		return MondaySession4Table;
	}
	public void setMondaySession4Table(String mondaySession4Table) {
		MondaySession4Table = mondaySession4Table;
	}
	public String getMondaySession4Room() {
		return MondaySession4Room;
	}
	public void setMondaySession4Room(String mondaySession4Room) {
		MondaySession4Room = mondaySession4Room;
	}
	public String getMondaySession4StartTime() {
		return MondaySession4StartTime;
	}
	public void setMondaySession4StartTime(String mondaySession4StartTime) {
		MondaySession4StartTime = mondaySession4StartTime;
	}
	public String getMondaySession4EndTime() {
		return MondaySession4EndTime;
	}
	public void setMondaySession4EndTime(String mondaySession4EndTime) {
		MondaySession4EndTime = mondaySession4EndTime;
	}
	public String getTuesdaySession1Product() {
		return TuesdaySession1Product;
	}
	public void setTuesdaySession1Product(String tuesdaySession1Product) {
		TuesdaySession1Product = tuesdaySession1Product;
	}
	public String getTuesdaySession1Table() {
		return TuesdaySession1Table;
	}
	public void setTuesdaySession1Table(String tuesdaySession1Table) {
		TuesdaySession1Table = tuesdaySession1Table;
	}
	public String getTuesdaySession1Room() {
		return TuesdaySession1Room;
	}
	public void setTuesdaySession1Room(String tuesdaySession1Room) {
		TuesdaySession1Room = tuesdaySession1Room;
	}
	public String getTuesdaySession1StartTime() {
		return TuesdaySession1StartTime;
	}
	public void setTuesdaySession1StartTime(String tuesdaySession1StartTime) {
		TuesdaySession1StartTime = tuesdaySession1StartTime;
	}
	public String getTuesdaySession1EndTime() {
		return TuesdaySession1EndTime;
	}
	public void setTuesdaySession1EndTime(String tuesdaySession1EndTime) {
		TuesdaySession1EndTime = tuesdaySession1EndTime;
	}
	public String getTuesdaySession2Product() {
		return TuesdaySession2Product;
	}
	public void setTuesdaySession2Product(String tuesdaySession2Product) {
		TuesdaySession2Product = tuesdaySession2Product;
	}
	public String getTuesdaySession2Table() {
		return TuesdaySession2Table;
	}
	public void setTuesdaySession2Table(String tuesdaySession2Table) {
		TuesdaySession2Table = tuesdaySession2Table;
	}
	public String getTuesdaySession2Room() {
		return TuesdaySession2Room;
	}
	public void setTuesdaySession2Room(String tuesdaySession2Room) {
		TuesdaySession2Room = tuesdaySession2Room;
	}
	public String getTuesdaySession2StartTime() {
		return TuesdaySession2StartTime;
	}
	public void setTuesdaySession2StartTime(String tuesdaySession2StartTime) {
		TuesdaySession2StartTime = tuesdaySession2StartTime;
	}
	public String getTuesdaySession2EndTime() {
		return TuesdaySession2EndTime;
	}
	public void setTuesdaySession2EndTime(String tuesdaySession2EndTime) {
		TuesdaySession2EndTime = tuesdaySession2EndTime;
	}
	public String getTuesdaySession3Product() {
		return TuesdaySession3Product;
	}
	public void setTuesdaySession3Product(String tuesdaySession3Product) {
		TuesdaySession3Product = tuesdaySession3Product;
	}
	public String getTuesdaySession3Table() {
		return TuesdaySession3Table;
	}
	public void setTuesdaySession3Table(String tuesdaySession3Table) {
		TuesdaySession3Table = tuesdaySession3Table;
	}
	public String getTuesdaySession3Room() {
		return TuesdaySession3Room;
	}
	public void setTuesdaySession3Room(String tuesdaySession3Room) {
		TuesdaySession3Room = tuesdaySession3Room;
	}
	public String getTuesdaySession3StartTime() {
		return TuesdaySession3StartTime;
	}
	public void setTuesdaySession3StartTime(String tuesdaySession3StartTime) {
		TuesdaySession3StartTime = tuesdaySession3StartTime;
	}
	public String getTuesdaySession3EndTime() {
		return TuesdaySession3EndTime;
	}
	public void setTuesdaySession3EndTime(String tuesdaySession3EndTime) {
		TuesdaySession3EndTime = tuesdaySession3EndTime;
	}
	public String getTuesdaySession4Product() {
		return TuesdaySession4Product;
	}
	public void setTuesdaySession4Product(String tuesdaySession4Product) {
		TuesdaySession4Product = tuesdaySession4Product;
	}
	public String getTuesdaySession4Table() {
		return TuesdaySession4Table;
	}
	public void setTuesdaySession4Table(String tuesdaySession4Table) {
		TuesdaySession4Table = tuesdaySession4Table;
	}
	public String getTuesdaySession4Room() {
		return TuesdaySession4Room;
	}
	public void setTuesdaySession4Room(String tuesdaySession4Room) {
		TuesdaySession4Room = tuesdaySession4Room;
	}
	public String getTuesdaySession4StartTime() {
		return TuesdaySession4StartTime;
	}
	public void setTuesdaySession4StartTime(String tuesdaySession4StartTime) {
		TuesdaySession4StartTime = tuesdaySession4StartTime;
	}
	public String getTuesdaySession4EndTime() {
		return TuesdaySession4EndTime;
	}
	public void setTuesdaySession4EndTime(String tuesdaySession4EndTime) {
		TuesdaySession4EndTime = tuesdaySession4EndTime;
	}


	public void prepareTemplateBody(){
       InputStream is = null;
       String templateBody="";
       try{
         String sTemplateFileName = "Personalized_Agenda_TemplateP4";
         String sTemplateFolder = VelocityConvertor.getTemplateFolder(PrintingConstants.env_type);
            LoggerHelper.logSystemDebug("Path of the template folder >>>>>>>>>>> "+ sTemplateFolder);
            
          String sTemplatePath = sTemplateFolder + File.separator + sTemplateFileName +".html";
          System.out.println("TemplatePath(1) : " + sTemplatePath);
          
           // String sTemplatePath = "/TrainingReports/WebContent/PrintHome/Templates" + File.separator + sTemplateFileName +".html";
          LoggerHelper.logSystemDebug("Path of the sTemplatePath >>>>>>>>>>>>"+ sTemplatePath);
          String serverName = this.serverName;
          LoggerHelper.logSystemDebug("Server Name >>>>>>>>>>>>"+ serverName);
          // if(serverName != null && serverName.indexOf("wlsdev1.pfizer.com") != -1){
          if(serverName != null && serverName.indexOf("trt-tst.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_INT  + sTemplatePath;
            }
        // else if(serverName != null && serverName.indexOf("wlsstg5.pfizer.com") != -1){
          else if(serverName != null && serverName.indexOf("trt-stg.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_STG  + sTemplatePath;
            }
          //   else if(serverName != null && serverName.indexOf("wlsprd4.pfizer.com") != -1){
          else if(serverName != null && serverName.indexOf("trt.pfizer.com") != -1){
            sTemplatePath = PrintingConstants.APPLICATION_PATH_PROD  + sTemplatePath;
            }
          LoggerHelper.logSystemDebug("Path of the sTemplatePath after applying server name>>>>>>>>>>>>"+ sTemplatePath);
          System.out.println("TemplatePath(2) : " + sTemplatePath);
          
          URL url = new URL(sTemplatePath);
          URLConnection urlConnection = url.openConnection();
          HttpURLConnection connection = (HttpURLConnection)urlConnection;;
          BufferedReader  bf  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          StringBuffer sbr =new StringBuffer();
          StringWriter mailMessage = new StringWriter();
          String strSM="";
          try
          {
            while((strSM = bf.readLine())!=null)
            {
              sbr.append(strSM+" ");
            }
            bf.close();
            this.templateBody=sbr.toString();
          }catch(Exception e){
          LoggerHelper.logSystemError("Class:PersonalizedAgendaBeanP4 Method:prepareTemplateBody--Couldn't locate the Invitation Template in prepareTemplateBody ",e);
          }
          }catch(Exception e) {LoggerHelper.logSystemError("Class:PersonalizedAgendaBeanP4 Method:prepareTemplateBody--Error Reading Email temaplte Body ",e);}

          }
}
