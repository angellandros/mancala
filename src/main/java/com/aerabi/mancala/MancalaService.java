package com.aerabi.mancala;

import com.aerabi.mancala.models.MancalaGameBoard;

public interface MancalaService {
    MancalaGameBoard newGame(String player1, String player2);

    MancalaGameBoard play(String player, int pitNumber);
}
