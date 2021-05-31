package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.model.board.domain.dto.BoardCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", method = {RequestMethod.GET})
public class BoardController {

    @PostMapping(value = "/boards", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody final BoardCreateRequest boardCreateRequest) {
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
