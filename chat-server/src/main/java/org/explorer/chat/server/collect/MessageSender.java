package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.solr.SolrSender;

import java.util.concurrent.BlockingQueue;

class MessageSender implements Runnable {

    private final SolrSender solrSender = new SolrSender();

    private BlockingQueue<ChatMessage> queue;
    private boolean stop;

    void setQueue(final BlockingQueue<ChatMessage> queue) {
        this.queue = queue;
    }

    MessageSender() {
    }

    MessageSender(final BlockingQueue<ChatMessage> queue) {
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

    void send(final ChatMessage chatMessage){
        solrSender.save(chatMessage);
    }
}
