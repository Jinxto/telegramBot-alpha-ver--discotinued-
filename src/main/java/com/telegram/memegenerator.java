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

public class memegenerator extends telegramBot {
    public void generateMeme(Message msg) {
    	 try {
 			URL url = new URL("https://api.imgflip.com/get_memes");
 			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 			conn.setRequestMethod("GET");
 			conn.connect();
 			int responsecode = conn.getResponseCode();
 			if(responsecode != 200){
 			  return;	
 			}else {
 				String inline = "";
 				Scanner scanner = new Scanner(url.openStream());
 				while(scanner.hasNext()) {
 					inline += scanner.nextLine();
 					System.out.println(inline);
 				}
 				scanner.close();
 				JSONParser parse = new JSONParser();
 				JSONObject data_obj = (JSONObject) parse.parse(inline);
 				JSONObject memo = (JSONObject) data_obj.get("data");
 				JSONArray obj = (JSONArray) memo.get("memes");
 				System.out.println(obj);
 				int random = (int) (Math.random() * (obj.size()-1));
 				JSONObject new_obj = (JSONObject) obj.get(random);
                SendMessage message = new SendMessage(); 	
                String name = new_obj.get("name").toString();
                String url1 = new_obj.get("url").toString();
                String whole = name+" "+url1;
                System.out.println(whole);
                
                message.setText(whole);
                message.setChatId(msg.getChatId().toString());
                execute(message);
                System.out.println(whole);
		
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
 		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 	 }
    }

