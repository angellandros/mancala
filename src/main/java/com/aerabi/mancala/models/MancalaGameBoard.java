package com.aerabi.mancala.models;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.stream.IntStream;
import org.immutables.value.Value;

@Value.Immutable
public interface MancalaGameBoard {
    ImmutableMap<String, ImmutableList<Integer>> getBoard();

    static ImmutableMancalaGameBoard initial(
            int pitCount, int stoneCount, String player1, String player2) {
        final ImmutableList<Integer> initialState =
                IntStream.rangeClosed(0, pitCount)
                        .map(i -> i == pitCount ? 0 : stoneCount)
                        .boxed()
                        .collect(toImmutableList());
        return ImmutableMancalaGameBoard.builder()
                .putBoard(player1, initialState)
                .putBoard(player2, initialState)
                .build();
    }

    static ImmutableMancalaGameBoard fromList(
            ImmutableList<Integer> pits, String player1, String player2) {
        final int length = pits.size() / 2;
        return ImmutableMancalaGameBoard.builder()
                .putBoard(player1, pits.subList(0, length))
                .putBoard(player2, pits.subList(length, length * 2))
                .build();
    }
}
