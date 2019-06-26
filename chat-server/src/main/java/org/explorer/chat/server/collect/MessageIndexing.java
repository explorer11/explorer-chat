package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageIndexing {

    private final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(100);
    private final MessageCollector messageCollector = new MessageCollector(queue);
    private final MessageSender messageSender;

    MessageIndexing(final MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public MessageIndexing() {
        this.messageSender = new MessageSender(queue);
    }

    public void start(){
        messageSender.setQueue(queue);
        final ExecutorService collectorExecutorService = Executors.newSingleThreadExecutor();
        collectorExecutorService.submit(messageSender);
    }

    public void write(final ChatMessage chatMessage){
        messageCollector.write(chatMessage);
    }
}
