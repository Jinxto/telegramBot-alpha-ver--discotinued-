package com.telegram;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class corona extends telegramBot{
    SendMessage message = new SendMessage();

	 public coronaModel coronad(String country) {
		 try {
			URL url = new URL("https://api.covid19api.com/summary");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if(responsecode != 200){
			  return null;	
			}else {
				String inline = "";
				Scanner scanner = new Scanner(url.openStream());
				while(scanner.hasNext()) {
					inline += scanner.nextLine();
				}
				scanner.close();
				JSONParser parse = new JSONParser();
				JSONObject data_obj = (JSONObject) parse.parse(inline);
				JSONArray obj = (JSONArray) data_obj.get("Countries");
				for(int i = 0; i<obj.size();i++) {
					JSONObject new_obj = (JSONObject) obj.get(i);
					if(new_obj.get("Slug").toString().equals(country)) {
						coronaModel coron = new coronaModel();
						coron.setCountry(country);
						coron.setNewConfirmed(new_obj.get("NewConfirmed").toString());
						coron.setTotalConfirmed(new_obj.get("TotalConfirmed").toString());
						coron.setNewDeaths(new_obj.get("NewDeaths").toString());
						coron.setTotalDeaths(new_obj.get("TotalDeaths").toString());
						coron.setNewRecovered(new_obj.get("NewRecovered").toString());
						coron.setTotalrecovered(new_obj.get("TotalRecovered").toString());
						coron.setDate(new_obj.get("Date").toString());
						System.out.println(coron.getCountry());
						return coron;
					}
				}
				return null;
				
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 

	 }
	 public void coronaChecker(Message message) {
		String temp = message.getText().replace("/corona ", "").toLowerCase();
		this.message.setChatId(message.getChatId().toString());
		if(temp.equals("help")) {
		this.message.setText("Type /corona then select the countries! If countries has 2 names I.E usa uk uae just type united-states, united-kingdom, united-arab-emirates")	; 
		try {
			execute(this.message);
			return;
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//\n for next line
		}
		if(temp!=" " || temp!="") {
		coronaModel coron = coronad(temp);
		if(coron!=null) {
			System.out.println("sent");
			this.message.setText("Country: "+coron.getCountry()+" \nNew cases: "+coron.getNewConfirmed()+" \nTotal cases: "+coron.getTotalConfirmed()+" \nNew deaths: "+ coron.getNewDeaths()+" \nTotal deaths: "+ coron.getTotalDeaths()+" \nNew recovered: "+ coron.getNewRecovered()+" \nTotal recovered: "+ coron.getTotalrecovered()+" \nDate: "+ coron.getDate());
			try {
	            execute(this.message); 
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
			return;
		}
		this.message.setText("No country found");
		 try {
	            execute(this.message); 
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
		 return;
	 }
		 this.message.setText("Empty parameter");
		 try {
	            execute(this.message); 
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
		 return;
	}
}