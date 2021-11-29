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
import java.util.function.Supplier;

import static org.explorer.chat.elastic.Constants.INDEX_NAME;
import static org.explorer.chat.elastic.Constants.USER_FIELD;

class Index implements ElasticAction {

    private final RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));

    @Override
    public void execute(final Supplier<String> supplier) throws IOException {
        final List<ChatMessage> messages = this.getMessages(new String[]{supplier.get()});
        this.send(messages);
    }

    List<ChatMessage> getMessages(final String[] args) throws IOException {
        final Path messagesPath = Arguments.messagesPath(args);

        final MessageStore messageStore = new MessageStore(messagesPath);

        return messageStore.findAll();
    }

    private void send(List<ChatMessage> messages) throws IOException {

        for (int i = 0; i < messages.size(); i++) {
            final ChatMessage chatMessage = messages.get(i);
            final IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(i))
                    .source(USER_FIELD, chatMessage.getFromUserMessage(),
                            "date", chatMessage.getInstant(),
                            "message", chatMessage.getMessage());

            client.index(indexRequest, RequestOptions.DEFAULT);
        }

        client.close();
    }
}
