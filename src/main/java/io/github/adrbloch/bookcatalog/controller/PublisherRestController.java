package io.github.adrbloch.bookcatalog.controller;


import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.service.AuthorService;
import io.github.adrbloch.bookcatalog.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherRestController {

    private PublisherService publisherService;

    @Autowired
    public PublisherRestController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/all")
    ResponseEntity<List<Publisher>> viewAll() {
        return new ResponseEntity<>(publisherService.getAllPublishers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Publisher> viewPublisher(@PathVariable Long id) {
        return new ResponseEntity<>(publisherService.getPublisher(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Publisher> createPublisher(@RequestBody Publisher newPublisher) {
        return new ResponseEntity<>(publisherService.createPublisher(newPublisher), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher newPublisher, @PathVariable Long id) {
        return new ResponseEntity<>(publisherService.updatePublisher(newPublisher, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Publisher> deletePublisher(@PathVariable Long id) {
        return new ResponseEntity<>(publisherService.deletePublisher(id),HttpStatus.OK);

    }

}
