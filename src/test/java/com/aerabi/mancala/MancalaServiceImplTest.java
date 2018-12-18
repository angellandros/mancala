package com.aerabi.mancala;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.aerabi.mancala.models.Cell;
import com.aerabi.mancala.models.ImmutableMancalaGameBoard;
import org.junit.Test;

public class MancalaServiceImplTest {
    private static final String PLAYER1 = "p1";
    private static final String PLAYER2 = "p2";

    @Test
    public void testNewGame() {
        MancalaService service = new MancalaServiceImpl();
        final ImmutableMancalaGameBoard board = service.newGame(PLAYER1, PLAYER2);
        final int length = board.getLength();
        assertThat(board.getBoard().keySet()).containsExactly(PLAYER1, PLAYER2);
        assertThat(board.getDran()).isEqualTo(PLAYER1);
        assertThat(board.getBoard().get(PLAYER1)).endsWith(Cell.of(length, 0).withIsBigPit(true));
        assertThat(board.getBoard().get(PLAYER1).subList(0, length))
                .allMatch(cell -> !cell.isBigPit());
    }

    @Test
    public void testPlayArgumentChecks() {
        MancalaService service = new MancalaServiceImpl();
        service.newGame(PLAYER1, PLAYER2);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.play(PLAYER2, 0))
                .withMessageContaining("turn");
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> service.play(PLAYER1, -1));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.play(PLAYER1, 6));
        service.play(PLAYER1, 0);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.play(PLAYER1, 0))
                .withMessageContaining("empty");
    }
}
