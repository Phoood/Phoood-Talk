package com.phooodstudio.phooodtalk.model;

import java.util.ArrayList;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines a singular conversation between two parties
 * TODO possibly extend implementation to multiple accounts
 */
public class Conversation {

    private Account mAccount1;
    private Account mAccount2;

    private ArrayList<Message> mMessages;

    public Account getAccount1() {
        return mAccount1;
    }

    public void setAccount1(Account account1) {
        mAccount1 = account1;
    }

    public Account getAccount2() {
        return mAccount2;
    }

    public void setAccount2(Account account2) {
        mAccount2 = account2;
    }

    public ArrayList<Message> getMessages() {
        return mMessages;
    }

    public void setMessages(ArrayList<Message> messages) {
        mMessages = messages;
    }
}
