package com.crud.tasks.controller;

import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.mapper.CreatedTrelloCard;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.client.TrelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/trello")
public class TrelloController {
    @Autowired
    //private TrelloClient trelloClient;
    private TrelloService trelloService;

    @RequestMapping(method = RequestMethod.GET, value = "getTrelloBoards")
    public List<TrelloBoardDto> getTrelloBoards() {
        //return trelloClient.getTrelloBoards();
        return trelloService.fetchTrelloBoards();
    }

    /*@RequestMapping(method = RequestMethod.GET, value = "getTrelloBoards")
    public void getTrelloBoards() {
        List<TrelloBoardDto> trelloBoards = trelloClient.getTrelloBoards();

        trelloBoards.forEach(trelloBoardDto -> {
            System.out.println(trelloBoardDto.getName() + " - " + trelloBoardDto.getId());
            System.out.println("This board contains lists: ");
            trelloBoardDto.getLists().forEach(trelloList ->
                    System.out.println(trelloList.getName() + " - " + trelloList.getId() + " - " + trelloList.isClosed()));
        });

        System.out.println("Number of Trello boards (trelloBoards): " + trelloBoards.size());
        trelloBoards.stream()
                .filter(s -> s.getName().isEmpty() == false)
                .filter(s -> s.getId().isEmpty() == false)
                .filter(s -> s.getName().toLowerCase().contains("kodilla"))
                .forEach(trelloBoardDto -> System.out.println(trelloBoardDto.getName() + " " + trelloBoardDto.getId()));
    }*/

    @RequestMapping(method = RequestMethod.POST, value = "createTrelloCard")
    public CreatedTrelloCard createTrelloCard(@RequestBody TrelloCardDto trelloCardDto) {
        //return trelloClient.createNewCard(trelloCardDto);
        return trelloService.createTrelloCard(trelloCardDto);
    }
}
