package com.project.kodesalon.service.board.query;

import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;
import com.project.kodesalon.service.dto.BoardQueryDto;
import com.project.kodesalon.service.dto.response.BoardImageResponse;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
public class BoardQueryService {

    private final BoardRepository boardRepository;

    public BoardQueryService(final BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public MultiBoardSelectResponse selectBoards(final Long lastBoardId, final int size) {
        List<BoardFlatQueryDto> flats = boardRepository.selectQueryBoards(lastBoardId, size);
        LinkedHashMap<BoardQueryDto, List<BoardImageResponse>> boardQueryDtos = convertFrom(flats);
        List<BoardSelectResponse> boards = mapToBoardsFrom(boardQueryDtos);
        return new MultiBoardSelectResponse(boards, size);
    }

    private List<BoardSelectResponse> mapToBoardsFrom(final LinkedHashMap<BoardQueryDto, List<BoardImageResponse>> boardQueryDtos) {
        return boardQueryDtos.entrySet()
                .stream()
                .map(boardQueryDto -> new BoardSelectResponse(boardQueryDto.getKey(), boardQueryDto.getValue()))
                .collect(Collectors.toList());
    }

    private LinkedHashMap<BoardQueryDto, List<BoardImageResponse>> convertFrom(final List<BoardFlatQueryDto> flats) {
        return flats.stream()
                .collect(groupingBy(BoardQueryDto::new, LinkedHashMap::new,
                        filtering(flat -> flat.getImageUrl() != null && flat.getImageId() != null,
                                mapping(BoardImageResponse::new, toList()))));
    }
}
