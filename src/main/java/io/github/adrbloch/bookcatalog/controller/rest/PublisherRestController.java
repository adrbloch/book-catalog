package io.github.adrbloch.bookcatalog.controller.rest;


import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishersRest")
public class PublisherRestController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherRestController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/all")
    ResponseEntity<List<Publisher>> viewAll() {
        return new ResponseEntity<>(publisherService.getAllPublishers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Publisher> viewPublisherById(@PathVariable Long id) {
        return new ResponseEntity<>(publisherService.getPublisherById(id), HttpStatus.OK);
    }

    @GetMapping("/{name}/{city}")
    ResponseEntity<Publisher> viewPublisherByNameAndCity(@PathVariable String name, @PathVariable String city) {
        return new ResponseEntity<>(publisherService.getPublisherByNameAndCity(name, city), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Publisher> createPublisher(@RequestBody Publisher newPublisher) {
        return new ResponseEntity<>(publisherService.createPublisher(newPublisher), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher publisherToUpdate, @PathVariable Long id) {
        return new ResponseEntity<>(publisherService.updatePublisher(id, publisherToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Publisher> deletePublisher(@PathVariable Long id) {
        return new ResponseEntity<>(publisherService.deletePublisherById(id),HttpStatus.OK);

    }

}
