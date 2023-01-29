package org.naboo.utenti;

public class ModelTableUtenti {
    private int chat_id;
    private String nickname;
    private String ban;

    public ModelTableUtenti(int chat_id, String nickname, String ban) {
        this.chat_id = chat_id;
        this.nickname = nickname;
        this.ban = ban;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String linkefeed) {
        this.nickname = linkefeed;
    }
    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }
}
