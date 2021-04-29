package com.telegram;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class timeComparing extends telegramBot {
	int n;
	String[]data;
    SendMessage message = new SendMessage();
    public void addData() {
		try {
		 csvData csv = new csvData();
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
    			Date datenow = new Date();
    			String date2 = " "+format.format(datenow);
    			String temp = data[((2*i)+0)+i].replace("/id", "");
    			String temp2 = data[((2*i)+2)+i].replace(temp+"/date", "");
    			System.out.println(date2+" "+"comparing"+temp2);
    			if(temp2.equals(date2)) {
				  String temp3 = data[((2*i)+1)+i].replace(temp+"/data", "").replace("\\n", "\n");
				  System.out.println(temp+"/date"+temp2);
				  message.setChatId(temp);
				  message.setText("Hi you have scheduled reminder:\nData: "+ temp3+"\nDate:"+temp2);
				  System.out.println("date compare success");
				  
				  try {
				        execute(message); 
						deleteData(temp+"/date"+temp2);

				    } catch (TelegramApiException e) {
				        e.printStackTrace();
				    }
	
    			}

    			/*String temp = data[i].replace("/id", "");
    			if(data[((2*i)+2)+i].contains(temp)) {
    			String	temp2 = data[((2*i)+2)+i].replace(temp+"/date", "");
    				Date datenow = new Date();
					String date2 = " "+format.format(datenow);
					System.out.println(date2 +"compare"+temp2);
					if(temp2.equals(date2)) {
					 String temp3 = data[((2*i)+1)+i].replace(temp+"/data", "");
					  message.setChatId(temp);
					  message.setText("Hi you have scheduled reminder Data: "+ temp3+" Date:"+temp2);
					  deleteData(temp+"/date"+temp2);
					  System.out.println("date compare success");
					  try {
					        execute(message); 
					    } catch (TelegramApiException e) {
					        e.printStackTrace();
					    }
					}
    				
    		}  
    				
               */
    	 }
    	
    		System.out.println("finished comparing");
    	}
    	System.out.println("empty data");
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
