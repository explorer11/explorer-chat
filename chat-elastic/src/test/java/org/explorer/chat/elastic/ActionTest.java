package org.explorer.chat.elastic;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionTest {

    @Test
    public void shouldFindIndexFromString() {
        assertThat(Action.from(":index")).isEqualTo(Action.INDEX);
    }

    @Test
    public void shouldFindExitFromString() {
        assertThat(Action.from(":exit")).isEqualTo(Action.EXIT);
    }

    @Test
    public void shouldFindAggregationFromString() {
        assertThat(Action.from(":aggregate")).isEqualTo(Action.USER_AGGREGATION);
    }

    @Test
    public void shouldReturnExitForNotExistingCommand() {
        assertThat(Action.from("notExisting")).isEqualTo(Action.EXIT);
    }
}