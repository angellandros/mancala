package com.aerabi.mancala.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Cell {
    int getIndex();

    int getStones();

    boolean isBigPit();

    boolean isLastUpdated();

    static ImmutableCell of(int index, int stones) {
        return ImmutableCell.builder()
                .index(index)
                .stones(stones)
                .isBigPit(false)
                .isLastUpdated(false)
                .build();
    }

    default ImmutableCell plusStones(int stones) {
        return ImmutableCell.builder()
                .index(getIndex())
                .stones(getStones() + stones)
                .isBigPit(isBigPit())
                .isLastUpdated(isLastUpdated())
                .build();
    }
}
