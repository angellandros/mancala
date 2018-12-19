package com.aerabi.mancala;

import com.aerabi.mancala.models.ImmutableMancalaGameBoard;
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
    public ImmutableMancalaGameBoard start(
            @RequestParam final String player1, @RequestParam final String player2) {
        return service.newGame(player1, player2);
    }

    @PutMapping("/play")
    public ImmutableMancalaGameBoard play(
            @RequestParam final String player, @RequestParam final int index) {
        return service.play(player, index);
    }
}
