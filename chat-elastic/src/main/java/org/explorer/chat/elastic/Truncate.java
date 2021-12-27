package org.explorer.chat.elastic;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Supplier;

import static org.explorer.chat.elastic.Constants.INDEX_NAME;

class Truncate implements ElasticAction {

    private static final Logger logger = LoggerFactory.getLogger(Truncate.class);

    @Override
    public void execute(final Supplier<String> supplier) throws IOException {

        final DeleteByQueryRequest request = new DeleteByQueryRequest(INDEX_NAME);
        request.setQuery(QueryBuilders.matchAllQuery());

        final ActionListener<BulkByScrollResponse> listener = new ActionListener<>() {
            @Override
            public void onResponse(BulkByScrollResponse bulkResponse) {
                logger.info("{} documents have been deleted", bulkResponse.getDeleted());
                logger.info("The operation has ended with status {}", bulkResponse.getStatus());
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("", e);
            }
        };

        ElasticClient.INSTANCE.client().deleteByQueryAsync(request, RequestOptions.DEFAULT, listener);
    }
}
