package com.aerabi.mancala;

import static com.google.common.collect.ImmutableList.toImmutableList;

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
    public MancalaGameBoard play(String player, int pitNumber) {
        final ImmutableMancalaGameBoard board = games.get(playerMapping.get(player));
        final int pitStonesCount = board.getBoard().get(player).get(pitNumber);
        final Range<Integer> distributeRange =
                Range.openClosed(pitNumber, pitNumber + pitStonesCount);
        final String opponent =
                board.getBoard()
                        .keySet()
                        .stream()
                        .filter(s -> !s.equals(player))
                        .findAny()
                        .orElseThrow(IllegalStateException::new);
        final ImmutableList<Integer> linear =
                ImmutableList.<Integer>builder()
                        .addAll(board.getBoard().get(player))
                        .addAll(board.getBoard().get(opponent))
                        .build();
        return MancalaGameBoard.fromList(
                IntStream.range(0, linear.size())
                        .boxed()
                        .map(index -> linear.get(index) + (distributeRange.contains(index) ? 1 : 0))
                        .collect(toImmutableList()),
                player,
                opponent);
    }
}
