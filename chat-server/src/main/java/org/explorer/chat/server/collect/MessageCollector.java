package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;

import java.util.concurrent.BlockingQueue;

final class MessageCollector {

    private final BlockingQueue<ChatMessage> queue;

    MessageCollector(final BlockingQueue<ChatMessage> queue) {
        this.queue = queue;
    }

    void write(final ChatMessage chatMessage){
        try {
            this.queue.put(chatMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
