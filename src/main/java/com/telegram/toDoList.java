package com.telegram;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

public class toDoList{
	ArrayList<session> state = new ArrayList<session>();
	ArrayList<String> data= new ArrayList<String>();
	ArrayList<todoModel> editor = new ArrayList<todoModel>();
    String[] listtemp;
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
public toDoList() {
	System.out.println("test");
	//run method here
}
    
	public String todo(Message message) {
	 String chatId= message.getChatId().toString();
	 if(message.getText().equals("/todo add")) {
		String mess = "Data add mode initialized";
		System.out.println(state.size()+"test");
		for(int k=0; k<state.size(); k++) {
			System.out.println(state.get(k));
			System.out.println("yes");
			if(state.get(k).getChatId().equals(chatId)) {
				mess = "Leaving data "+state.get(k).getCategory()+" initializing data add mode!";
				state.remove(k);
				System.out.println(mess);
				System.out.println("yes2");
				break;
			}
		}
		if(editor.size()>0) {
		   for(int i = 0; i<editor.size();i++) {
			   if(editor.get(i).getId().equals(chatId+"/id")) {
				  editor.remove(i);
				}
			}
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
		session s = new session();
		Date datenow = new Date();
		String formater = format.format(datenow);
		s.setChatId(chatId);
		s.setCategory("add");
		s.setDate(formater);
		state.add(s);
		
		return mess;
	 }
	if(message.getText().contains("/add")) {
		String temp = message.getText().replace("/add","").replace("|", "");
        temp = StringEscapeUtils.escapeJava(temp);
		System.out.println(temp);
		for(int k = 0; k<state.size(); k++) {
			if(state.get(k).getCategory().equals("add")&& state.get(k).getChatId().equals(chatId)){
				if(!temp.isEmpty()) {
					data.add(chatId+"/id");
					data.add(chatId+"/data"+temp);
					System.out.println(temp+"test");
					data.add(chatId+"/date");
					Date datenow = new Date();
					String formater = format.format(datenow);
					state.get(k).setCategory("reminder");
					state.get(k).setDate(formater);
					temp = temp.replace("\\n", "\n");
					System.out.println(temp);
					return "Data is temporary stored! Please continue by inserting command: \n/reminder date [Example: /reminder 24/02/2020 18:56] \nFor Data: " + temp;
				}
				return "Empty parameter";
			}
		}
		return "Invalid command";
	}
	if(message.getText().contains("/reminder")) {
		String temp = message.getText().replace("/reminder", "").replaceAll("\\s+", " ");
		for(int k = 0; k<state.size(); k++) {
			if(state.get(k).getCategory().equals("reminder")&& state.get(k).getChatId().equals(chatId)) {
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
											state.remove(k);
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
				return "Empty Parameter try again";
			}
		}
		return "invalid command try again";
	}
	if(message.getText().equals("/todo remove")) {
		String mess = "Data removal mode initialized, type /remove INSERT_DATE_HERE to delete data based on the date";
		
		for(int k = 0; k<state.size();k++) {
			if(state.get(k).getChatId().equals(chatId)) {
				mess = "Leaving data "+state.get(k).getCategory()+" initializing data remove mode";
				state.remove(k);
				break;
			}
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
		session s = new session();
		Date datenow = new Date();
		String formater = format.format(datenow);
		s.setChatId(chatId);
		s.setCategory("remove");
		s.setDate(formater);
		state.add(s);
		return mess;
	}
	if(message.getText().contains("/remove")) {
        String param = message.getText().replace("/remove", "").replaceAll("\\s+", " ");
        for(int k=0; k<state.size(); k++) {
        	if(state.get(k).getCategory().equals("remove")&& state.get(k).getChatId().equals(chatId)) {
        		if(param!=null) {	
    				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    				try {
    					format.parse(param);
    					try {
    						csvData csv = new csvData();
    						if(csv.csvList("test.txt", chatId+"/date"+param, message)!=null) {
    					    	csv.deleteData("test.txt", chatId+"/date"+param);
    					    	state.remove(k);
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
        }
		
		return "Invalid command";
	} 
	
	if(message.getText().equals("/todo quit")) {
		String mess = "Session closed";
        for(int k =0 ; k<state.size();k++) {       
           if(state.get(k).getChatId().equals(chatId)) {
        	   mess = state.get(k).getCategory()+" sesssion closed";
        	   state.remove(k);
        	   break;
           }
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
		String mess = "Intializing data edit mode!";
		for(int k = 0 ; k<state.size();k++) {
			if(state.get(k).getChatId().equals(chatId)) {
				mess = "Leaving data "+state.get(k).getCategory()+" mode, initializing data edit mode";
				System.out.println(state.get(k).getChatId());
				state.remove(k);
				break;
			}
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
		session s = new session();
		Date datenow = new Date();
		String formater = format.format(datenow);
		s.setChatId(chatId);
		s.setCategory("edit");
		s.setDate(formater);
		state.add(s);
		return mess;
	}if(message.getText().contains("/select")) {
		String temp = message.getText().replace("/select", "").replaceAll("\\s+", " ");
		for(int k =0; k<state.size(); k++) {
		 if(state.get(k).getCategory().equals("edit") && state.get(k).getChatId().equals(chatId)) {
			 System.out.println(state.get(k).getChatId()+state.get(k).getCategory());
			 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				try {
					format.parse(temp);
					
					try {
						csvData csv = new csvData();
						if(csv.csvList("test.txt", chatId+"/date"+temp, message)!=null) {
							editor.add(csv.csvList("test.txt", chatId+"/date"+temp, message));
							Date datenow = new Date();
							String formater = format.format(datenow);
							state.get(k).setDate(formater);
							state.get(k).setCategory("changing");
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
		}
		return "Invalid Command";
	}
	if(message.getText().contains("/data")) {
        String temp = message.getText().replace("/data", "").replace("|", "");
        temp = StringEscapeUtils.escapeJava(temp);
        for(int k = 0; k<state.size();k++) {
          if(state.get(k).getCategory().equals("changing")&& state.get(k).getChatId().equals(chatId)) {
       	   if(temp!="") {
 			  temp= StringUtils.normalizeSpace( temp );				
 		      try {
 				  csvData csv = new csvData();
 				  for(int i=0; i<editor.size(); i++) {
 					  if(editor.get(i).getId().equals(chatId+"/id")) {
 					     csv.editData("test.txt", chatId+"/data"+temp,editor.get(i).getDate(), message);
                         state.remove(k);
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
        	
        }
		 return "Invalid command";
	}
	if(message.getText().contains("/date")) {
	        String temp = message.getText().replace("/date", "").replaceAll("\\s+", " ");
			for(int k =0; k< state.size(); k++) {
				if(state.get(k).getCategory().equals("changing")&& state.get(k).getChatId().equals(chatId)) {
					if(temp!="") {
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	                    try {
							format.parse(temp);
							
							try {
								csvData csv = new csvData();
								for(int i=0; i<editor.size(); i++) {
									if(editor.get(i).getId().equals(chatId+"/id")) {
									   csv.editDate("test.txt", chatId+"/date"+temp,editor.get(i).getDate(), message);
                                       state.remove(k);
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
			}
			return "Invalid command";
		}
	if(message.getText().equals("/imlonely")) {
		String message1 = "deepLearning mode activated, while talking with the bot please wait for a few seconds, this is because the bot runs on javascript and the api itself is very slow";
		for(int k = 0; k<state.size();k++) {
			if(state.get(k).getChatId().equals(chatId)) {
				message1 = "Leaving "+state.get(k).getCategory()+" initialzing deeplearning mode, while talking to the bot, please wait for it to"
						+ "load because the bot is run on javascript and that specific api is quite slow so sorry for the inconvenience";
				state.remove(k);
			}
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
		session s = new session();
		Date datenow = new Date();
		String formater = format.format(datenow);
		s.setCategory("deeplearning");
		s.setChatId(chatId);
		s.setDate(formater);
		state.add(s);
		return message1;
		
	}
		//if error change the chatbot.js filepath and npm install cleverbot-free on the machine
	if(message.hasText()) {
		for(int k = 0; k<state.size();k++) {
			if(state.get(k).getCategory().equals("deeplearning")&& state.get(k).getChatId().equals(chatId)) {
				ProcessBuilder b = new ProcessBuilder("node", "chatbot.js",message.getText().toString());
 		        try {
 		        	Process process = b.start();
 		        	String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8); 		           
						return output;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				
			      
		    
			   }
			}
		}
	
	
       return null;
	}
	public String listall(Message message) {
	String chatId = message.getChatId().toString();
	if(message.getText().equals("/todo listall")) {
		for(int k = 0; k<state.size(); k++) {
			System.out.println(state.get(k).getChatId());
			if(state.get(k).getChatId().equals(chatId)) {
				
		       return "null";				
			}
		}
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
	return null;
	}
}

