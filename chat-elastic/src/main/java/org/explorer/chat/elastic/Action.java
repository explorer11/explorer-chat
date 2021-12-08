package org.explorer.chat.elastic;

enum Action {

    INDEX(":index", new Index()),
    SEARCH(":search", new Search()),
    USER_AGGREGATION(":aggregate", new UserAggregation()),
    EXIT(":exit", new EmptyAction());

    Action(final String command, final ElasticAction elasticAction) {
        this.command = command;
        this.elasticAction = elasticAction;
    }

    private final String command;
    private final ElasticAction elasticAction;

    static Action from(String command) {

        for (final Action action : Action.values()) {
            if(action.command.equals(command)) {
                return action;
            }
        }

        return EXIT;
    }

    ElasticAction getElasticAction() {
        return elasticAction;
    }
}
