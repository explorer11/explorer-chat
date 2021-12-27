package org.explorer.chat.elastic;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.function.Supplier;

import static org.explorer.chat.elastic.Constants.USER_FIELD;

class UserAggregation implements ElasticAction {

    @Override
    public void execute(final Supplier<String> supplier) throws IOException {
        final SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);

        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        final String BY_USER = "by_user";
        final TermsAggregationBuilder aggregation = AggregationBuilders
                .terms(BY_USER)
                .field(USER_FIELD);
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);

        final SearchResponse searchResponse = ElasticClient.INSTANCE.client()
                .search(searchRequest, RequestOptions.DEFAULT);

        final Aggregations aggregations = searchResponse.getAggregations();
        final Terms byUserAggregation = aggregations.get(BY_USER);

        byUserAggregation.getBuckets().forEach(bucket -> {
                System.out.print(bucket.getKey());
                System.out.print(":");
                System.out.println(bucket.getDocCount());
        });
	}
}
