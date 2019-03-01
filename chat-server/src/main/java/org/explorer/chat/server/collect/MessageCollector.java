package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;

import java.util.concurrent.BlockingQueue;

public final class MessageCollector {

    private final BlockingQueue<ChatMessage> queue;

    public MessageCollector(final BlockingQueue<ChatMessage> queue) {
        this.queue = queue;
    }

    public void write(final ChatMessage chatMessage){
        try {
            this.queue.put(chatMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
