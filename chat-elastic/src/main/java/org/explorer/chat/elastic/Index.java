package org.explorer.chat.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.config.Arguments;
import org.explorer.chat.data.MessageStore;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Index {

    private static final String INDEX_NAME = "message";

    private final RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));

    public static void main(String[] args) throws IOException {
        final Index index = new Index();

        final List<ChatMessage> messages = index.getMessages(args);

        index.execute(messages);
    }

    List<ChatMessage> getMessages(final String[] args) throws IOException {
        final Path messagesPath = Arguments.messagesPath(args);

        final MessageStore messageStore = new MessageStore(messagesPath);

        return messageStore.findAll();
    }

    private void execute(List<ChatMessage> messages) throws IOException {

        /*final DeleteRequest indexRequest = new DeleteRequest(
                INDEX_NAME,
                "1");*/

        for (int i = 0; i < messages.size(); i++) {
            final ChatMessage chatMessage = messages.get(i);
            final IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(i))
                    .source("user", chatMessage.getFromUserMessage(),
                            "date", chatMessage.getInstant(),
                            "message", chatMessage.getMessage());

            client.index(indexRequest, RequestOptions.DEFAULT);
        }

        client.close();
    }
}
