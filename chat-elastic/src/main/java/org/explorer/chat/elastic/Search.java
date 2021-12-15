package org.explorer.chat.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import static org.explorer.chat.elastic.Constants.DATE_FIELD;
import static org.explorer.chat.elastic.Constants.MESSAGE_FIELD;
import static org.explorer.chat.elastic.Constants.USER_FIELD;

public class Search implements ElasticAction {

    private final RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));

    @Override
    public void execute(final Supplier<String> arguments) throws IOException {
        final String keywords = arguments.get();

        final SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);

        final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery(MESSAGE_FIELD, keywords));
        searchRequest.source(sourceBuilder);

        final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        final SearchHits searchHits = searchResponse.getHits();
        for (SearchHit searchHit : searchHits.getHits()) {
            final Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            System.out.print(sourceAsMap.get(USER_FIELD));
            System.out.print(":");
            System.out.print(sourceAsMap.get(DATE_FIELD));
            System.out.print(":");
            System.out.println(sourceAsMap.get(MESSAGE_FIELD));
        }

    }
}
