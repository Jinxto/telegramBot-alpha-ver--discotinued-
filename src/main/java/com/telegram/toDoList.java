package com.telegram;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;

public class toDoList {
	Stack<String> state= new Stack<String>();
	ArrayList<String> data= new ArrayList<String>();
	ArrayList<todoModel> editor = new ArrayList<todoModel>();
    String[] listtemp;

    
	public String todo(Message message) {
	 String chatId= message.getChatId().toString();
	 if(message.getText().equals("/todo add")) {
		int add = state.indexOf(chatId+"add");
		int reminder = state.indexOf(chatId+"reminder");
        int remove = state.indexOf(chatId+"remove");
		int edit = state.indexOf(chatId+"edit");
        int changing = state.indexOf(chatId+"changing");
      System.out.println("/todo add opened");
		String mess = "Data add mode initialized, type /add YOUR_DATA_HERE to continue!";	
		if(add>-1) {
			state.remove(add);
			mess = "Data add mode reinitialized!";
		}
		if(reminder>-1) {
			state.remove(reminder);
			mess = "Data add mode reinitialized!";
		}
		if(remove>-1) {
			state.remove(remove);
			mess = "Leaving data removal mode, initializing data add mode!";
		}
		if(edit>-1) {
			state.remove(edit);
			mess = "Leaving data edit mode, initializing data add mode!";
		}
		if(changing>-1) {
			state.remove(changing);
			mess = "Leaving data edit mode, initializing data add mode!";

		}
		for(int i = 0; i<data.size(); i++) {
			if(data.get(i).contains(chatId+"/id")) {
				data.remove(i);
			}
			if(data.get(i).contains(chatId+"/data")) {
				data.remove(i);
			}
			if(data.get(i).contains(chatId+"/date")) {
				data.remove(i);
			}
		} 
		state.add(chatId+"add");
		return mess;
	 }
	if(message.getText().contains("/add")) {
		int add = state.indexOf(chatId+"add");
		String temp = message.getText().replace("/add","");
		temp= StringUtils.normalizeSpace( temp );				
		System.out.println("yes");
		if(add>-1) {
			if(!temp.isEmpty()) {
				data.add(chatId+"/id");
				data.add(chatId+"/data"+temp);
				data.add(chatId+"/date");
				state.add(chatId+"reminder");
				state.remove(chatId+"add");
				System.out.println("added");
				return "Data is temporary stored! Please continue by inserting command /reminder date [Example: /reminder 24/02/2020 18:56] for Data: " + temp;
			}
			return "Empty parameter";
		}
		
		return "Invalid command";
	}
	if(message.getText().contains("/reminder")) {
		int reminder = state.indexOf(chatId+"reminder");
		String temp = message.getText().replace("/reminder", "").replaceAll("\\s+", " ");
		if(reminder>-1) {
		   if(!temp.isEmpty()) {
			  try{
				  SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				  format.parse(temp);
				  todoModel tdm = new todoModel();
					 try {
						   csvData csv = new csvData();
						   if(!csv.csvChecking("test.txt", message,chatId+"/date"+temp)) {
							  for(int i=0; i<data.size();i++) {
								 if(data.get(i).contains(chatId+"/id")) {  
									tdm.setId(data.get(i));
									data.remove(i);								
									}
								  if(data.get(i).contains(chatId+"/data")){
									tdm.setData(data.get(i));
									data.remove(i);
									}
								  if(data.get(i).contains(chatId+"/date")){ 
									data.remove(i);								
									tdm.setDate(chatId+"/date"+temp);
									csv.csvInsertData(tdm, message, "test.txt", "/todo add");
									state.remove(reminder);
									return "Data is saved and you will be reminded soon!"; 
									}	
								 }
							}
							return "There is an existing data with existing date. If you wish to edit that data head to /todo edit";
							  } catch (IOException e) {
								  e.printStackTrace();
							  }
					
					}catch(ParseException e){
					    return "Incorrect date format example: {DD/MM/YYYY HH:MM} try again!";
				    }  
			}
			
		}
		return "invalid command try again";
	}
	if(message.getText().equals("/todo remove")) {
		int add = state.indexOf(chatId+"add");
		int reminder = state.indexOf(chatId+"reminder"); 
        int remove = state.indexOf(chatId+"remove");
        int edit = state.indexOf(chatId+"edit");
        int changing = state.indexOf(chatId+"changing");
		String mess = "Data removal mode initialized, type /remove INSERT_DATE_HERE to delete data based on the date";	
		if(add>-1) {
			state.remove(add);
			mess = "Leaving data add mode, intializing data removal mode!";
		}
		if(reminder>-1) {
			state.remove(reminder);
			mess = "Leaving data add mode, intializing data removal mode!";
		}
		if(remove>-1) {
			state.remove(remove);
			mess = "Data removal mode reinitialized!";
		}
		if(edit>-1) {
			state.remove(edit);
			mess = "Leaving data edit mode, intializing data removal mode!";
		}
		if(changing>-1) {
			state.remove(changing);
			mess = "Leaving data add mode, intializing data removal mode!";
		}
		state.add(chatId+"remove");
		return mess;
	}
	if(message.getText().contains("/remove")) {
        int remove = state.indexOf(chatId+"remove");
        String param = message.getText().replace("/remove", "").replaceAll("\\s+", " ");
		if(remove>-1) {
			if(param!=null) {	
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				try {
					format.parse(param);
					try {
						csvData csv = new csvData();
						if(csv.csvList("test.txt", chatId+"/date"+param, message)!=null) {
					    	csv.deleteData("test.txt", chatId+"/date"+param);
					    	state.remove(remove);
						    return "Data removed!";	
						}
					    return "";
					} catch (IOException e) {
						e.printStackTrace();
						return "No data found or it maybe auto removed when reminder system activates";
					}
				} catch (ParseException e) {
					return "Incorrect date format example: {DD/MM/YYYY HH:MM} try again!";
				}
			}
			return "Empty parameter";
		}
		return "Invalid command";
	} 
	if(message.getText().equals("/todo listall")) {
		int add = state.indexOf(chatId+"add");
		int reminder = state.indexOf(chatId+"reminder");
        int remove = state.indexOf(chatId+"remove");
        int edit = state.indexOf(chatId+"edit");
        int changing = state.indexOf(chatId+"changing");
        int total = add+reminder+remove+edit+changing;
        if(total==-5) {
		try {
			csvData cas = new csvData();
			String[] temp =cas.csvListAll("test.txt", message);
			if(cas.found) {
				listtemp = temp; 
				return message.getChatId().toString()+"listall";
			}
			return "no data";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        return "/todo listall is not a session based command you have to type /todo quit to quit your current session before continuing /todo listall";
	}
	if(message.getText().equals("/todo quit")) {
		int add = state.indexOf(chatId+"add");
		int reminder = state.indexOf(chatId+"reminder");
        int remove = state.indexOf(chatId+"remove");
        int edit = state.indexOf(chatId+"edit");
        int changing = state.indexOf(chatId+"changing");

		String mess = "Session closed";
		if(add>-1) {
			state.remove(add);
		}
		if(reminder>-1) {
			state.remove(reminder);
		}
		if(remove>-1) {
			state.remove(remove);
		}
		if(edit>-1) {
			state.remove(edit);
		}
		if(changing>-1) {
			state.remove(changing);
		}
		if(data.size()>0) {
		for(int i = 0; i<data.size(); i++) {
			if(data.get(i).contains(chatId+"/id")) {
				data.remove(i);
			}
			if(data.get(i).contains(chatId+"/data")) {
				data.remove(i);
			}
			if(data.get(i).contains(chatId+"/date")) {
				data.remove(i);
			}
		} 
	}
	    if(editor.size()>0) {
		for(int i = 0; i<editor.size();i++) {
			if(editor.get(i).getId().equals(chatId+"/id")) {
			   editor.remove(i);
			}
		}
	}
		
	return mess;
	}
	if(message.getText().equals("/todo edit")) {
		int add = state.indexOf(chatId+"add");
		int reminder = state.indexOf(chatId+"reminder"); 
        int remove = state.indexOf(chatId+"remove");
        int edit = state.indexOf(chatId+"edit");
        int changing = state.indexOf(chatId+"changing"); 
		String mess = "Data edit mode initialized, type /select DATE with format DD/MM/YYYY HH:MM to select the data based on the date!";	
	
		if(add>-1) {
			state.remove(add);
			mess = "Leaving data add mode, intializing data edit mode!";
		}
		if(reminder>-1) {
			state.remove(reminder);
			mess = "Leaving data add mode, intializing data edit mode!";
		}
		if(remove>-1) {
			state.remove(remove);
			mess = "Leaving data add mode, intializing data edit mode!";
		}
		if(edit>-1) {
			state.remove(edit);
			mess = "Leaving data add mode, intializing data edit mode!";
		}
		if(changing>-1) {
			state.remove(changing);
			mess = "Data edit mode reinitialized!";
		}
		if(editor.size()>0) {
			for(int i = 0; i<editor.size();i++) {
				if(editor.get(i).getId().equals(chatId+"/id")) {
					editor.remove(i);
				}
			}
		}
		state.add(chatId+"edit");
		return mess;
	}if(message.getText().contains("/select")) {
		int edit = state.indexOf(chatId+"edit");
		if(edit>-1) {
			String temp = message.getText().replace("/select", "").replaceAll("\\s+", " ");
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				format.parse(temp);
				
				try {
					csvData csv = new csvData();
					if(csv.csvList("test.txt", chatId+"/date"+temp, message)!=null) {
						editor.add(csv.csvList("test.txt", chatId+"/date"+temp, message));
						state.remove(edit);
						state.add(chatId+"changing");
						return "Data selected choose /data INSERT_DATA_HERE to change your data OR /date INSERT_DATE_HERE with format DD/MM/YYYY HH:MM to change your date";
					}
                    return "Data not found";
				} catch (IOException e) {
					e.printStackTrace();	
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				return "invalid date format try again";
			}
		}
		return "Invalid Command";
	}
	if(message.getText().contains("/data")) {
        int changing = state.indexOf(chatId+"changing");
        String temp = message.getText().replace("/data", "");
		if(changing>-1) {
		   if(temp!="") {
			  temp= StringUtils.normalizeSpace( temp );				
		      try {
				  csvData csv = new csvData();
				  for(int i=0; i<editor.size(); i++) {
					  if(editor.get(i).getId().equals(chatId+"/id")) {
					     csv.editData("test.txt", chatId+"/data"+temp,editor.get(i).getDate(), message);
                         state.remove(changing);
				         return "Data changed";
				  }
				} 
				return "Error";
			   }catch (IOException e) {
				
				e.printStackTrace();
			   }
			}
			return "Empty parameter";
		}
		 return "Invalid command";
	}
	if(message.getText().contains("/date")) {
	   int changing = state.indexOf(chatId+"changing");
	        String temp = message.getText().replace("/date", "").replaceAll("\\s+", " ");
			temp = temp.trim().replaceAll(" +", " ");
			if(changing>-1) {
				if(temp!="") {
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
						format.parse(temp);
						
						try {
							csvData csv = new csvData();
							for(int i=0; i<editor.size(); i++) {
								if(editor.get(i).getId().equals(chatId+"/id")) {
								   csv.editDate("test.txt", chatId+"/date"+temp,editor.get(i).getDate(), message);
			                       state.remove(changing);
								   return "Date changed";
								}
							}
							return "error";
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (ParseException e1) {
						e1.printStackTrace();
						return "Invalid date format try again!";
					}
				}
				return "Empty parameter";
			}
			return "Invalid command";
		}      
       return null;
	}

}