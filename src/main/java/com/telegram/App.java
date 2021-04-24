package com.telegram;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App  {
	public static void main(String[]args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi((Class<? extends BotSession>) DefaultBotSession.class);
            botsApi.registerBot(new telegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        timeComparing compare = new timeComparing();
        try {
            while (true) {
                compare.addData();
                compare.compare();                
                Thread.sleep(30* 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
  
    }
	}


