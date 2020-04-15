package com.antonina.socialsynchro.common.gui.messages;

public abstract class Message {
    public static final int INFINITE = -1;

    private MessageTypeID messageType;
    private String content;
    private int displayTimeMiliseconds;

    public Message(String content) {
        this.content = content;
        this.displayTimeMiliseconds = INFINITE;
    }

    public Message(String content, int displayTimeMiliseconds) {
        this(content);
        this.displayTimeMiliseconds = displayTimeMiliseconds;
    }

    protected void setMessageType(MessageTypeID messageType) {
        this.messageType = messageType;
    }

    public MessageTypeID getMessageType() {
        return messageType;
    }

    public abstract int getIconID();

    public abstract int getColor();
}
