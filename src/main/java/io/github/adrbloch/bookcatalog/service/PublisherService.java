package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    public static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher getPublisher(Long id) throws ResourceNotFoundException {
        logger.info("Get publisher with id: {}", id);
       return checkIfExistsAndReturnPublisher(id);
    }

    public List<Publisher> getAllPublishers() {
        logger.info("Get all publishers");
        return publisherRepository.findAll();
    }

    public Publisher createPublisher(Publisher publisher) {
        logger.info("Create publisher...");
        return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(Publisher publisher, Long id) throws ResourceNotFoundException {
        logger.info("Update publisher with id: {}", id);

        Publisher publisherToUpdate = checkIfExistsAndReturnPublisher(id);
        publisherToUpdate.setName(publisher.getName());
        publisherToUpdate.setCity(publisher.getCity());
        return publisherRepository.save(publisherToUpdate);
    }

    public Publisher deletePublisher(Long id) throws ResourceNotFoundException {
        logger.warn("Delete publisher with id: {}", id);
        Publisher publisherToDelete = checkIfExistsAndReturnPublisher(id);
        publisherRepository.deleteById(id);
        return publisherToDelete;
    }

    private Publisher checkIfExistsAndReturnPublisher(Long id) {
        if (publisherRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Publisher with id {" + id + "} not found!");
        } else return publisherRepository.findById(id).get();
    }


}




