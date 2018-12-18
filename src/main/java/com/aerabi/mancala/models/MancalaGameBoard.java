package com.aerabi.mancala.models;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.stream.IntStream;
import org.immutables.value.Value;

@Value.Immutable
public interface MancalaGameBoard {
    /** Game board as a map from player to their pits */
    ImmutableMap<String, ImmutableList<ImmutableCell>> getBoard();

    /** Wer ist dran? */
    String getDran();

    /** Number of small pits for each player */
    int getLength();

    static ImmutableMancalaGameBoard initial(
            int pitCount, int stoneCount, String player1, String player2) {
        final ImmutableList<ImmutableCell> initialState =
                IntStream.rangeClosed(0, pitCount)
                        .boxed()
                        .map(index -> Cell.of(index, index == pitCount ? 0 : stoneCount))
                        .map(cell -> cell.getIndex() == pitCount ? cell.withIsBigPit(true) : cell)
                        .collect(toImmutableList());
        return ImmutableMancalaGameBoard.builder()
                .putBoard(player1, initialState)
                .putBoard(player2, initialState)
                .dran(player1)
                .length(pitCount)
                .build();
    }

    static ImmutableMancalaGameBoard fromList(
            ImmutableList<ImmutableCell> pits, String player1, String player2) {
        final int length = pits.size() / 2;
        return ImmutableMancalaGameBoard.builder()
                .putBoard(player1, pits.subList(0, length))
                .putBoard(player2, pits.subList(length, length * 2))
                .dran(pits.get(length - 1).isLastUpdated() ? player1 : player2)
                .length(length)
                .build();
    }
}
