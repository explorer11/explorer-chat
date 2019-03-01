package org.explorer.chat.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.explorer.chat.common.ChatMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SolrSender {

    private final SolrClient solrClient = new HttpSolrClient.Builder(
            "http://localhost:8983/solr/messages").build();

    public void send(ChatMessage chatMessage){

        final SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id",
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        solrInputDocument.addField("user", chatMessage.getFromUserMessage());
        solrInputDocument.addField("message", chatMessage.getMessage());

        try {
            solrClient.add(solrInputDocument);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }
}
