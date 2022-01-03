package org.explorer.chat.elastic;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
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
        QueryBuilder matchQueryBuilder = queryBuilder(supplier);
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

    QueryBuilder queryBuilder(final Supplier<String> supplier) {

        SearchType searchType = type(supplier);

        System.out.print("query>");
        final String query = supplier.get();
        System.out.println("the query is " + query);

        switch (searchType) {
            case phrase -> {
                return QueryBuilders.matchPhraseQuery(MESSAGE_FIELD, query);
            }
            case string -> {
                return QueryBuilders.queryStringQuery(query);
            }
            default -> {
                final MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(MESSAGE_FIELD, query);
                final Operator operator = operator(supplier);
                matchQueryBuilder.operator(operator);
                return matchQueryBuilder;
            }
        }
    }

    private SearchType type(final Supplier<String> supplier) {
        System.out.print("type (match, phrase, default match)>");
        final String inputType = supplier.get();

        SearchType searchType;
        try {
            searchType = SearchType.valueOf(inputType);
        } catch (final IllegalArgumentException e) {
            logger.warn("", e);
            searchType = SearchType.match;
        }

        System.out.println("the type is " + searchType);

        return searchType;
    }

    private Operator operator(final Supplier<String> supplier) {
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

        return operator;
    }

    private enum SearchType {
        match,
        phrase,
        string
    }
}
