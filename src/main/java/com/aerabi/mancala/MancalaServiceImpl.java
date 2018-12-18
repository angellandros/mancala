package com.aerabi.mancala;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.aerabi.mancala.models.Cell;
import com.aerabi.mancala.models.ImmutableMancalaGameBoard;
import com.aerabi.mancala.models.MancalaGameBoard;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class MancalaServiceImpl implements MancalaService {
    private Map<String, ImmutableMancalaGameBoard> games;
    private Map<String, String> playerMapping;

    public MancalaServiceImpl() {
        games = new HashMap<>();
        playerMapping = new HashMap<>();
    }

    @Override
    public MancalaGameBoard newGame(String player1, String player2) {
        String id = UUID.randomUUID().toString();
        ImmutableMancalaGameBoard board = MancalaGameBoard.initial(6, 6, player1, player2);
        games.put(id, board);
        playerMapping.put(player1, id);
        playerMapping.put(player2, id);
        return board;
    }

    @Override
    public MancalaGameBoard play(String player, int pitIndex) {
        checkArgument(playerMapping.containsKey(player), "No game initialized for player {}", player);
        final String id = playerMapping.get(player);
        final ImmutableMancalaGameBoard board = games.get(id);
        checkArgument(board.getBoard().get(player).get(pitIndex) != 0, "Chosen pit is empty");
        final String opponent = getOpponent(board, player);

        final Range<Integer> distributeRange = getDistributeRange(board, player, pitIndex);
        final ImmutableList<Integer> linear =
                ImmutableList.<Integer>builder()
                        .addAll(board.getBoard().get(player))
                        .addAll(board.getBoard().get(opponent))
                        .build();
        final ImmutableList<Integer> newLinear =
                IntStream.range(0, linear.size())
                        .boxed()
                        .map(index -> Cell.of(index, linear.get(index)))
                        // Empty the chosen pit
                        .map(
                                cell ->
                                        cell.withStones(
                                                cell.getIndex() == pitIndex ? 0 : cell.getStones()))
                        // Distribute the picked stones to the following pits
                        .map(
                                cell ->
                                        cell.plusStones(
                                                distributeRange.contains(cell.getIndex()) ? 1 : 0))
                        .map(Cell::getStones)
                        .collect(toImmutableList());
        ImmutableMancalaGameBoard newBoard = MancalaGameBoard.fromList(newLinear, player, opponent);
        games.put(id, newBoard);
        return newBoard;
    }

    private Range<Integer> getDistributeRange(
            final ImmutableMancalaGameBoard board, final String player, final int index) {
        final int pitStonesCount = board.getBoard().get(player).get(index);
        return Range.openClosed(index, index + pitStonesCount);
    }

    private String getOpponent(final ImmutableMancalaGameBoard board, final String player) {
        return board.getBoard()
                .keySet()
                .stream()
                .filter(s -> !s.equals(player))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }
}
