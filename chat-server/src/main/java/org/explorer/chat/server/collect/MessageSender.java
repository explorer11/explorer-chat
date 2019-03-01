package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.solr.SolrSender;

import java.util.concurrent.BlockingQueue;

public final class MessageSender implements Runnable {

    private final SolrSender solrSender = new SolrSender();

    private final BlockingQueue<ChatMessage> queue;
    private boolean stop;

    public MessageSender(final BlockingQueue<ChatMessage> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                final ChatMessage chatMessage = read();
                send(chatMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stop = true;
            }
        }
    }

    ChatMessage read() throws InterruptedException {
        return queue.take();
    }

    private void send(final ChatMessage chatMessage){
        solrSender.send(chatMessage);
    }
}
