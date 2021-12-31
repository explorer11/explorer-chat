package org.explorer.chat.elastic;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import static org.explorer.chat.elastic.Constants.DATE_FIELD;
import static org.explorer.chat.elastic.Constants.MESSAGE_FIELD;
import static org.explorer.chat.elastic.Constants.USER_FIELD;

public class Search implements ElasticAction {

    private static final Logger logger = LoggerFactory.getLogger(Search.class);

    @Override
    public void execute(final Supplier<String> supplier) throws IOException {

        final SearchRequest searchRequest = new SearchRequest(Constants.INDEX_NAME);

        final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = matchQueryBuilder(supplier);
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);

        final SearchResponse searchResponse = ElasticClient.INSTANCE.client()
                .search(searchRequest, RequestOptions.DEFAULT);

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

    MatchQueryBuilder matchQueryBuilder(final Supplier<String> supplier) {
        System.out.print("keywords>");
        final String keywords = supplier.get();
        System.out.println("the keywords are " + keywords);

        System.out.print("operator (OR, AND, default OR)>");
        final String inputOperator = supplier.get();

        Operator operator;
        try {
            operator = Operator.fromString(inputOperator);
        } catch (final IllegalArgumentException e) {
            logger.warn("", e);
            operator = Operator.OR;
        }

        System.out.println("the operator is " + operator);

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(MESSAGE_FIELD, keywords);
        matchQueryBuilder.operator(operator);
        return matchQueryBuilder;
    }
}
