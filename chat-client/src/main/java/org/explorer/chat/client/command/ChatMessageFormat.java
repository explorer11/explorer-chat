package org.explorer.chat.client.command;

import org.explorer.chat.common.ChatMessage;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

final class ChatMessageFormat {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static String formatSentence(final ChatMessage chatMessage) {

        final String userMessage = chatMessage.getFromUserMessage() + " : " + chatMessage.getMessage();

        if(chatMessage.getInstant() == null) {
            return userMessage;
        }

        final String formattedInstant =
                chatMessage.getInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(FORMATTER);

        return formattedInstant + " : " + userMessage;
    }
}
