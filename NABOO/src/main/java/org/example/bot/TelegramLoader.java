package org.example.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramLoader {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new SerieALiveNewsBot());
        } catch (TelegramApiRequestException e) {
            // gestione errore in registrazione
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
