package com.aerabi.mancala;

import com.aerabi.mancala.models.MancalaGameBoard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MancalaController {
    @GetMapping("/start")
    public MancalaGameBoard start() {
        return MancalaGameBoard.empty();
    }
}
