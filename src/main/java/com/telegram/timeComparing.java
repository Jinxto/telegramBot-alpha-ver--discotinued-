package com.telegram;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class timeComparing extends telegramBot {
	String[]data;
    SendMessage message = new SendMessage();
    public void addData() {
    	 csvData csv;
		try {
			csv = new csvData();
	        data = csv.csvListAllCustom("test.txt");  
	        System.out.println("data collected");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("no data to compare");
		}
    }public void compare() {
    	if(data.length>=1) {
    	    SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		for(int i=0; i<data.length/3; i++) {
    			String temp = data[i].replace("/id", "");
    			if(data[i+2].contains(temp)) {
    			String	temp2 = data[i+2].replace(temp+"/date", "");
    				Date datenow = new Date();
					String date2 = " "+format.format(datenow);
					if(temp2.equals(date2)) {
					 String temp3 = data[i+1].replace(temp+"/data", "");
					  message.setChatId(temp);
					  message.setText("Hi you have scheduled reminder Data: "+ temp3+" Date: "+temp2);
					  deleteData(temp+"/date"+temp2);
					  
					  data[i+2]=data[i+1];
					  System.out.println("date compare success");
					  try {
					        execute(message); 
					    } catch (TelegramApiException e) {
					        e.printStackTrace();
					    }
					}
    				
    		}    		
    	 }
    		System.out.println("finished comparing");
    	}
    }
    public void deleteData(String date) {
   	 csvData csv;
		try {
			csv = new csvData();
			csv.deleteData("test.txt", date);
	        	
		} catch (IOException e) {
			e.printStackTrace();
		}

	
}
}
