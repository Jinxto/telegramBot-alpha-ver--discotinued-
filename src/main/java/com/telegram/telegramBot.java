package com.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class telegramBot extends TelegramLongPollingBot {
	toDoList todolist = new toDoList();
    SendMessage message = new SendMessage();
	boolean sent;
	@Override
	public void onUpdateReceived(Update update) {
		 if (update.hasMessage() && update.getMessage().hasText()) {
			 System.out.println(update.getMessage().getText());
			   sent = false;
			   String x = todolist.todo(update.getMessage());
		       message.setChatId(update.getMessage().getChatId().toString());
			   if(x!=null) {
				   message.setText(x);
			   }
		   
			   if(update.getMessage().getText().contains("/corona")) {
				   
			    	corona china = new corona();
			    	china.coronaChecker(update.getMessage());
			    }
			   if(update.getMessage().getText().equals("/memegenerator")) {
				   System.out.println("memetialized");
				   memegenerator meme = new memegenerator();
				   meme.generateMeme(update.getMessage());
			   }
		        
		        if(todolist.todo(update.getMessage()).equals(update.getMessage().getChatId().toString()+"listall")) {
		           sent = true;
		           for(int i = 0; i<todolist.listtemp.length;i++) {
		        	   String temp = todolist.listtemp[i].replace(message.getChatId().toString()+"/id"+"|"+message.getChatId().toString()+"/data", "");
		        	   temp = temp.replace("|"+message.getChatId().toString()+"/date", "\nDate:");
		        	   temp = "\nData: "+temp.replace("\\n", "\n");
		        	   message.setText(temp);
		        	   try {
				            execute(message);
				        } catch (TelegramApiException e) {
				            e.printStackTrace();
				        }
		           }
		        }
		        if(sent==false) {
		        	try {
			            execute(message); 
			        } catch (TelegramApiException e) {
			            e.printStackTrace();
			        }
		        }
		     }
		   if (update.hasMessage() && update.getMessage().hasPhoto()) {
			   if(update.getMessage().getCaption()!=null && update.getMessage().getCaption().equals("/whatanime")){
				  anime anim = new anime();
                  anim.connectAnime(anim.executed(update.getMessage()),update.getMessage()); 			
			}
		   }
			
		        
		    }
	

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "bot_name";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "bot_token";
	}


}
