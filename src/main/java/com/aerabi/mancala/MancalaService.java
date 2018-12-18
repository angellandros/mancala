package com.aerabi.mancala;

import com.aerabi.mancala.models.ImmutableMancalaGameBoard;

public interface MancalaService {
    ImmutableMancalaGameBoard newGame(String player1, String player2);

    ImmutableMancalaGameBoard play(String player, int pitNumber);
}
