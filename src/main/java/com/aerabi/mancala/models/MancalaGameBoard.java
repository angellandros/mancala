package com.aerabi.mancala.models;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.Map;
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

    /** Has the game ended */
    boolean isFinished();

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
                .isFinished(false)
                .build();
    }

    static ImmutableMancalaGameBoard terminal(ImmutableMap<String, Integer> scores, int pitCount) {
        final ImmutableMap<String, ImmutableList<ImmutableCell>> terminalBoard =
                scores.entrySet()
                        .stream()
                        .collect(
                                toImmutableMap(
                                        Map.Entry::getKey,
                                        entry ->
                                                IntStream.rangeClosed(0, pitCount)
                                                        .boxed()
                                                        .map(
                                                                index ->
                                                                        Cell.of(
                                                                                index,
                                                                                index == pitCount
                                                                                        ? entry
                                                                                                .getValue()
                                                                                        : 0))
                                                        .map(
                                                                cell ->
                                                                        cell.getIndex() == pitCount
                                                                                ? cell.withIsBigPit(
                                                                                        true)
                                                                                : cell)
                                                        .collect(toImmutableList())));
        final String winner =
                scores.entrySet()
                        .stream()
                        .max(Comparator.comparingInt(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .orElse("-");
        return ImmutableMancalaGameBoard.builder()
                .board(terminalBoard)
                .dran(winner)
                .length(pitCount)
                .isFinished(false)
                .build();
    }

    static ImmutableMancalaGameBoard fromList(
            ImmutableList<ImmutableCell> pits, String player1, String player2) {
        final int lengthPlus = pits.size() / 2;
        final ImmutableList<ImmutableCell> player1Row = pits.subList(0, lengthPlus);
        final ImmutableList<ImmutableCell> player2Row = pits.subList(lengthPlus, lengthPlus * 2);
        final ImmutableMap<String, ImmutableList<ImmutableCell>> map =
                ImmutableMap.of(player1, player1Row, player2, player2Row);
        final ImmutableMap<String, Integer> totalStonesInSmallPits =
                map.entrySet()
                        .stream()
                        .collect(
                                toImmutableMap(
                                        Map.Entry::getKey,
                                        entry ->
                                                entry.getValue()
                                                        .stream()
                                                        .filter(cell -> !cell.isBigPit())
                                                        .mapToInt(Cell::getStones)
                                                        .sum()));
        final boolean finished = totalStonesInSmallPits.values().contains(0);
        if (finished) {
            ImmutableMap<String, Integer> scores =
                    map.entrySet()
                            .stream()
                            .collect(
                                    toImmutableMap(
                                            Map.Entry::getKey,
                                            entry ->
                                                    entry.getValue()
                                                            .stream()
                                                            .filter(Cell::isBigPit)
                                                            .mapToInt(ImmutableCell::getStones)
                                                            .sum()))
                            .entrySet()
                            .stream()
                            .collect(
                                    toImmutableMap(
                                            Map.Entry::getKey,
                                            entry ->
                                                    entry.getValue()
                                                            + totalStonesInSmallPits.get(
                                                                    entry.getKey())));
            return terminal(scores, lengthPlus - 1);
        }
        return ImmutableMancalaGameBoard.builder()
                .board(map)
                .dran(pits.get(lengthPlus - 1).isLastUpdated() ? player1 : player2)
                .length(lengthPlus - 1)
                .isFinished(false)
                .build();
    }
}
