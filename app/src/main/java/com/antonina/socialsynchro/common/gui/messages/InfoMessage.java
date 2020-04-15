package com.antonina.socialsynchro.common.gui.messages;

public class InfoMessage extends Message {
    public InfoMessage(String content) {
        super(content);
        setMessageType(MessageTypeID.Info);
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
