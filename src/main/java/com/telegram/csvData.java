package com.telegram;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.telegram.telegrambots.meta.api.objects.Message;


public class csvData {
	boolean found = false; //saving edit model
	Scanner x;

	public csvData() throws IOException {
	}
	public void csvInsertData(todoModel tdm, Message message, String path, String category) throws IOException {
		try {
			FileWriter fw = new FileWriter(path,true);
			BufferedWriter bw = new BufferedWriter(fw); // extension of filewriter make sure words are efficient written on the file
			PrintWriter pw = new PrintWriter(bw);
			if(category == "/todo add") {
			pw.println(tdm.getId()+"|"+tdm.getData()+"|"+tdm.getDate());
			pw.flush();
			pw.close();
			}
		}
		catch(Exception E)
		{	
		}
}
	public  void csvDeleteData(String path) {
		
	}
	public String [] csvListAll(String path, Message message) {
		ArrayList<String> record = new ArrayList<String>();
		String ID = ""; String data = ""; String date = ""; String record1 = "";
		try {
			Scanner x;
			x = new Scanner(new File(path));
			x.useDelimiter("[|\n]");
			while(x.hasNext()) {
				ID = x.next();
				if(ID.equals(message.getChatId().toString()+"/id")) {
					data = x.next();
					date = x.next();
					record1 = ID + "|"+data + "|"+ date;
					record.add(record1);
					found = true;
				}
				else {
					x.next();
					x.next();
				}
			}
		}catch(Exception e) {
			System.out.println("error listall");
			
		}
		String[] recordArray = new String[record.size()];
		record.toArray(recordArray);
		return recordArray;
		
	}
	public boolean csvChecking(String path, Message message, String d4te) {
		String ID = ""; String data = ""; String date = ""; 
		try {
			x = new Scanner(new File(path));
			x.useDelimiter("[|\n]");
			while(x.hasNext()) {
				ID = x.next();
				if(ID.equals(message.getChatId().toString()+"/id")) {
					data = x.next();
					date = x.next();
					if(date.equals(d4te)) {
						found = true;
					}
				}
				else {
					x.next();
					x.next();
				}
			}
		}catch(Exception e) {
			
		}
		
		return found;
}   
	public String deleteData(String filepath,String removeTerm) {
		String tempFile = "temp.txt";
		File oldFile = new File(filepath);
		File newFile = new File(tempFile);
		String ID = ""; String data= ""; String date="";
		try {
			FileWriter fw = new FileWriter(tempFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			x = new Scanner(new File(filepath));
			x.useDelimiter("[|\n]");
			
			while(x.hasNext()) {
				ID = x.next();
				data = x.next();
				date = x.next();
				if(!date.equals(removeTerm)) {
					pw.println(ID +"|"+data+"|"+date);
				}
			}
			x.close();
			pw.flush();
			pw.close();
			oldFile.delete();
			File dump = new File(filepath);
			newFile.renameTo(dump);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		
	return "yes";
} 
public todoModel csvList(String path, String select, Message message) {
	String ID = ""; String data = ""; String date = ""; 
	try {
		Scanner x;
		x = new Scanner(new File(path));
		x.useDelimiter("[|\n]");
		todoModel  tmd = new todoModel();
		while(x.hasNext()) {
			ID = x.next();
			data = x.next();
			date = x.next();
			if(ID.equals(message.getChatId().toString()+"/id") && date.equals(select)) {
			
				tmd.setId(ID);
				tmd.setData(data);
				tmd.setDate(date);
				return tmd;
			}
		}
		
	}catch(Exception e) {
		
	}
	return null;

}
public String editData(String filepath, String editTerm,String selectTerm, Message message) {
	String tempFile = "edit.txt";
	File oldFile = new File(filepath);
	File newFile = new File(tempFile);
	String Id= ""; String data = ""; String date = "";
	try {
		FileWriter fw = new FileWriter(tempFile,true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		x = new Scanner(new File(filepath));
		x.useDelimiter("[|\n]");
		while(x.hasNext()) {
			Id= x.next();
			data= x.next();
			date = x.next();
			if(date.equals(selectTerm)) {
				pw.println(Id + "|" +editTerm + "|"+date);
			}else {
			pw.println(Id+"|"+data+"|"+date);
			}
		}
		x.close();
		pw.flush();
		pw.close();
		oldFile.delete();
		File dump =new File(filepath);
		newFile.renameTo(dump);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "edited";
}
public String editDate(String filepath, String editTerm,String selectTerm, Message message) {
	String tempFile = "edit.txt";
	File oldFile = new File(filepath);
	File newFile = new File(tempFile);
	String Id= ""; String data = ""; String date = "";
	try {
		FileWriter fw = new FileWriter(tempFile,true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		x = new Scanner(new File(filepath));
		x.useDelimiter("[|\n]");
		while(x.hasNext()) {
			Id= x.next();
			data= x.next();
			date = x.next();
			if(date.equals(selectTerm)) {
				pw.println(Id + "|" +data + "|"+editTerm);

			}else {
			pw.println(Id+"|"+data+"|"+date);
			}
		
		}
		x.close();
		pw.flush();
		pw.close();
		oldFile.delete();
		File dump =new File(filepath);
		newFile.renameTo(dump);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "edited";
}
public String [] csvListAllCustom(String path) {
	ArrayList<String> record = new ArrayList<String>();
	String ID = ""; String data = ""; String date = ""; 
	try {
		Scanner x;
		x = new Scanner(new File(path));
		x.useDelimiter("[|\n]");
		while(x.hasNext()) {
			ID = x.next();
			data = x.next();
			date = x.next();
				record.add(ID);
				record.add(data);
				record.add(date);
				found = true;
		}
	}catch(Exception e) {
		
	}
	String[] recordArray = new String[record.size()];
	record.toArray(recordArray);
	return recordArray;
}
}
