package com.antonina.socialsynchro.common.gui.messages;

public class ErrorMessage extends Message {
    public ErrorMessage(String content) {
        super(content);
        setMessageType(MessageTypeID.Warning);
    }

    @Override
    public int getIconID() {
        return 0;
    }

    @Override
    public int getColor() {
        return 0;
    }
}
