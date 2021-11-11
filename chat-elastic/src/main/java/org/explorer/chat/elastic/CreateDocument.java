package org.explorer.chat.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.time.Instant;

public class CreateDocument {

    public static void main(String[] args) throws IOException {
        final CreateDocument createDocument = new CreateDocument();
        createDocument.execute();
    }

    private void execute() throws IOException {
        final RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        final IndexRequest indexRequest = new IndexRequest("message")
                .id("1")
                .source("user", "joel",
                        "date", Instant.now(),
                        "message", "hello");

        client.index(indexRequest, RequestOptions.DEFAULT);

        client.close();
    }
}
