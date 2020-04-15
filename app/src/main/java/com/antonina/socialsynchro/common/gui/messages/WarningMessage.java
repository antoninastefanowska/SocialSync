package com.antonina.socialsynchro.common.gui.messages;

public class WarningMessage extends Message {
    public WarningMessage(String content) {
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
