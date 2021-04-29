package com.telegram;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class anime extends telegramBot{
	public PhotoSize getPhoto(Message message) {	        // When receiving a photo, you usually get different sizes of it
	        List<PhotoSize> photos = message.getPhoto();

	        // We fetch the bigger photo
	        return photos.stream()
	                .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
	    }
	public String getFilePath(PhotoSize photo) {
	    Objects.requireNonNull(photo);

	    if (photo.getFilePath()!=null) { // If the file_path is already present, we are done!
	        return photo.getFilePath();
	    } else { // If not, let find it
	        // We create a GetFile method and set the file_id from the photo
	        GetFile getFileMethod = new GetFile();
	        getFileMethod.setFileId(photo.getFileId());
	        try {
	            // We execute the method using AbsSender::execute method.
	            File file = execute(getFileMethod);
	            // We now have the file_path
	            return file.getFilePath();
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return null; // Just in case
	}

	
	public String executed(Message message) {
		return(getFilePath(getPhoto(message)));
	}
	public void connectAnime(String filepath, Message message) {
           try {
        	System.out.println(filepath);
			URL url = new URL("http://trace.moe/api/search?url=https://api.telegram.org/file/bot1798704202:AAGVgDnvvC_h1QYpCpSN9HhAlhIWtt9YSss/"+filepath);
			System.out.println("http://trace.moe/api/search?url=https://api.telegram.org/file/bot1798704202:AAGVgDnvvC_h1QYpCpSN9HhAlhIWtt9YSss/"+filepath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if(responsecode  == HttpURLConnection.HTTP_MOVED_TEMP
		            || responsecode== HttpURLConnection.HTTP_MOVED_PERM){
				System.out.println(responsecode);
		        String newUrl = conn.getHeaderField("Location");
		        url = new URL(newUrl);
		        conn = (HttpURLConnection) new URL(newUrl).openConnection();
				String inline = "";
				Scanner scanner = new Scanner((url.openStream()));
				while(scanner.hasNext()) {
					inline += scanner.nextLine();	
					System.out.println(inline);
				}
				scanner.close();
				JSONParser parse = new JSONParser();
				JSONObject data_obj = (JSONObject) parse.parse(inline);
				JSONArray obj = (JSONArray) data_obj.get("docs");
				JSONObject new_obj = (JSONObject) obj.get(0);
				SendMessage msg = new SendMessage();
				if(new_obj.get("is_adult").toString().equals("true")) {
					msg.setChatId(message.getChatId().toString());
					msg.setText(new_obj.get("title_romaji").toString()+" "+"make sure your door is locked ( ͡° ͜ʖ ͡°)");
					execute(msg);
				  return;	
				}
				msg.setChatId(message.getChatId().toString());
				msg.setText(new_obj.get("title_romaji").toString());
				execute(msg);
			  return;	
			}
			
		   
	      
           }catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
		
	} catch (ProtocolException e) {
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