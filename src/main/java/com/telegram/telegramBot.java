package com.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class telegramBot extends TelegramLongPollingBot {
	toDoList todolist = new toDoList();
    SendMessage message = new SendMessage();
  
	@Override
	public void onUpdateReceived(Update update) {
		 if (update.hasMessage() && update.getMessage().hasText()) {
			 System.out.println(update.getMessage().getText());
			   String x = todolist.todo(update.getMessage());
			
			   System.out.println(x);
		       message.setChatId(update.getMessage().getChatId().toString());
			   if(x!=null) {
				   message.setText(x);
				   System.out.println("yes231");
				   try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
		   
			   if(update.getMessage().getText().contains("/corona")) {
				   
			    	corona china = new corona();
			    	china.coronaChecker(update.getMessage());
			    }
			   if(update.getMessage().getText().equals("/help")) {
					String help = "List of commands:"+"\n/todo add (add data)"+ "\n/todo edit (edit data)"+ "\n/todo remove (remove data)"+ "\n/todo listall (list all the saved data)"+ "\nsend a picture and type /whatanime to get anime name from that screenshot"+ "\n/memegenerator (generate template for a random meme)"+"\n/corona INSERT_COUNTRY_NAME example (united-kingdom,united-states,united-arab-emirates)"+"\n/imlonely to talk with Annie"+"\n/todo quit (quit session based command i.e add,edit,remove,imlonely)";
					message.setText(help);
                    try {
						execute(message);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
			   if(update.getMessage().getText().equals("/memegenerator")) {
				   System.out.println("memetialized");
				   anime meme = new anime();
				   meme.generateMeme(update.getMessage());
			   }
		        String listing =todolist.listall(update.getMessage());
		        if(listing.equals(update.getMessage().getChatId().toString()+"listall")) {
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
		        if(listing.equals("null")) {
		        	message.setText("/todo listall is not a session based command you have to type /todo quit to quit your current session before continuing /todo listall");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
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
	

	private void execute(String help) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "jinxto_bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "1798704202:AAGVgDnvvC_h1QYpCpSN9HhAlhIWtt9YSss";
	}


}
