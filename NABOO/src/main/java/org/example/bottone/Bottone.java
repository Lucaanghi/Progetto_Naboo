package org.example.bottone;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Bottone extends InlineKeyboardButton {

    private int id_bottone;

    public int getId_bottone() {
        return id_bottone;
    }

    public void setId_bottone(int id_bottone) {
        this.id_bottone = id_bottone;
    }

}
