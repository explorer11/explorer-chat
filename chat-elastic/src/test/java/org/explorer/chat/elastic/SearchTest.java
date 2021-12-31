package org.explorer.chat.elastic;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.explorer.chat.elastic.Constants.MESSAGE_FIELD;

public class SearchTest {

    private final Search search = new Search();

    @Test
    public void shouldBuildQueryWithDefaultOperator() {
        final String keywords = "bob";
        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return keywords;
                }
                return "";
            }
        };

        final MatchQueryBuilder matchQueryBuilder = search.matchQueryBuilder(supplier);

        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }

    @Test
    public void shouldBuildQueryWithOperatorAND() {
        final String keywords = "bob";
        final String andOperator = "AND";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return keywords;
                }
                return andOperator;
            }
        };

        final MatchQueryBuilder matchQueryBuilder = search.matchQueryBuilder(supplier);

        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.AND);
    }

    @Test
    public void shouldBuildQueryWithOperatorOR() {
        final String keywords = "bob";
        final String orOperator = "OR";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return keywords;
                }
                return orOperator;
            }
        };

        final MatchQueryBuilder matchQueryBuilder = search.matchQueryBuilder(supplier);

        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }

    @Test
    public void shouldBuildQueryWithWrongOperatorInput() {
        final String keywords = "bob";
        final String orOperator = "wrong";

        final Supplier<String> supplier = new Supplier<>() {
            int i = 0;

            @Override
            public String get() {
                if(i == 0) {
                    i++;
                    return keywords;
                }
                return orOperator;
            }
        };

        final MatchQueryBuilder matchQueryBuilder = search.matchQueryBuilder(supplier);

        assertThat(matchQueryBuilder.value()).isEqualTo(keywords);
        assertThat(matchQueryBuilder.fieldName()).isEqualTo(MESSAGE_FIELD);
        assertThat(matchQueryBuilder.operator()).isEqualTo(Operator.OR);
    }
}