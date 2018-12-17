package com.aerabi.mancala.models;

import com.google.common.collect.ImmutableList;
import org.immutables.value.Value;

@Value.Immutable
public interface MancalaGameBoard {
    ImmutableList<Integer> getBlack();
    ImmutableList<Integer> getWhite();

    static MancalaGameBoard empty() {
        return ImmutableMancalaGameBoard.builder()
                .addBlack(0, 0, 0, 0, 0, 0, 0)
                .addWhite(0, 0, 0, 0, 0, 0, 0)
                .build();
    }
}
