package org.explorer.chat.elastic;

import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.SpanNearQueryBuilder;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.explorer.chat.elastic.Constants.MESSAGE_FIELD;

public class SearchTest {

    private final Search search = new Search();

    @Test
    public void shouldBuildMatchQueryWithDefaultOperator() {
        final String type = "match";
        final String keywords = "bob";
        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else if(i == 1) {
                    i++;
                    return keywords;
                }
                return "";
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(MatchQueryBuilder.class);

        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) queryBuilder;
        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }

    @Test
    public void shouldBuildMatchQueryWithOperatorAND() {
        final String type = "match";
        final String keywords = "bob";
        final String andOperator = "AND";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else if(i == 1) {
                    i++;
                    return keywords;
                }
                return andOperator;
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(MatchQueryBuilder.class);

        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) queryBuilder;
        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.AND);
    }

    @Test
    public void shouldBuildMatchQueryWithOperatorOR() {
        final String type = "match";
        final String keywords = "bob";
        final String orOperator = "OR";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else if(i == 1) {
                    i++;
                    return keywords;
                }
                return orOperator;
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(MatchQueryBuilder.class);

        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) queryBuilder;
        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }

    @Test
    public void shouldBuildMatchQueryWithWrongOperatorInput() {
        final String type = "match";
        final String keywords = "bob";
        final String orOperator = "wrong";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else if(i == 1) {
                    i++;
                    return keywords;
                }
                return orOperator;
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(MatchQueryBuilder.class);

        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) queryBuilder;
        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }

    @Test
    public void shouldBuildMatchQueryAsDefaultType() {
        final String type = "default";
        final String keywords = "bob";
        final String orOperator = "wrong";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else if(i == 1) {
                    i++;
                    return keywords;
                }
                return orOperator;
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(MatchQueryBuilder.class);

        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) queryBuilder;
        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }

    @Test
    public void shouldBuildPhraseQuery() {
        final String type = "phrase";
        final String keywords = "apache lucene";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else {
                    return keywords;
                }
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(MatchPhraseQueryBuilder.class);

        final MatchPhraseQueryBuilder matchQueryBuilder = (MatchPhraseQueryBuilder) queryBuilder;
        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
    }

    @Test
    public void shouldBuildSpanQuery() {
        final String type = "span";
        final String queryString = "apache lucene";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else {
                    return queryString;
                }
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(SpanNearQueryBuilder.class);

        final SpanNearQueryBuilder spanNearQueryBuilder = (SpanNearQueryBuilder) queryBuilder;
        assertThat(spanNearQueryBuilder.slop()).isEqualTo(3);
        assertThat(spanNearQueryBuilder.clauses()).hasSize(2);

        assertThat(spanNearQueryBuilder.clauses().get(0)).isExactlyInstanceOf(SpanTermQueryBuilder.class);
        final SpanTermQueryBuilder spanTermQueryBuilder0 = (SpanTermQueryBuilder) spanNearQueryBuilder.clauses().get(0);
        assertThat(spanTermQueryBuilder0.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(spanTermQueryBuilder0.value()).isEqualTo("apache");

        assertThat(spanNearQueryBuilder.clauses().get(1)).isExactlyInstanceOf(SpanTermQueryBuilder.class);
        final SpanTermQueryBuilder spanTermQueryBuilder1 = (SpanTermQueryBuilder) spanNearQueryBuilder.clauses().get(1);
        assertThat(spanTermQueryBuilder1.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(spanTermQueryBuilder1.value()).isEqualTo("lucene");
    }

    @Test
    public void shouldBuildStringQuery() {
        final String type = "string";
        final String queryString = "message:(hello OR bonjour)";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return type;
                } else {
                    return queryString;
                }
            }
        };

        final QueryBuilder queryBuilder = search.queryBuilder(supplier);

        assertThat(queryBuilder).isExactlyInstanceOf(QueryStringQueryBuilder.class);

        final QueryStringQueryBuilder queryStringQueryBuilder = (QueryStringQueryBuilder) queryBuilder;
        assertThat(queryStringQueryBuilder.queryString()).isEqualTo(queryString);
    }
}