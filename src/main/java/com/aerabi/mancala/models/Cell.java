package com.aerabi.mancala.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Cell {
    int getIndex();

    int getStones();

    static ImmutableCell of(int index, int stones) {
        return ImmutableCell.builder().index(index).stones(stones).build();
    }

    default ImmutableCell plusStones(int stones) {
        return ImmutableCell.builder().index(getIndex()).stones(getStones() + stones).build();
    }
}
