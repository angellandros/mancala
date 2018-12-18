package com.aerabi.mancala;

import com.aerabi.mancala.models.ImmutableCell;
import com.aerabi.mancala.models.ImmutableMancalaGameBoard;
import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MancalaController {

    private final MancalaService service;

    public MancalaController() {
        service = new MancalaServiceImpl();
    }

    @PostMapping("/start")
    public String start(@RequestParam final String player1, @RequestParam final String player2) {
        return representableGameBoard(service.newGame(player1, player2));
    }

    @PutMapping("/play")
    public String play(@RequestParam final String player, @RequestParam final int index) {
        return representableGameBoard(service.play(player, index));
    }

    private String representableGameBoard(ImmutableMancalaGameBoard board) {
        return board.getBoard()
                .entrySet()
                .stream()
                .map(
                        entry ->
                                formatRow(
                                        entry.getValue(),
                                        entry.getKey(),
                                        entry.getKey().equals(board.getDran())))
                .reduce("", String::concat);
    }

    private String formatRow(ImmutableList<ImmutableCell> row, String player, boolean dran) {
        StringBuilder builder = new StringBuilder();
        row.forEach(cell -> builder.append(String.format("%-2d ", cell.getStones())));
        builder.append(player);
        if (dran) {
            builder.append(" <==");
        }
        builder.append("\n");
        return builder.toString();
    }
}
