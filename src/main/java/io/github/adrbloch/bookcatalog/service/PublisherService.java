package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.model.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher getPublisherById(Long id) {
        logger.info("Get publisher with id: {}", id);

        return returnPublisherIfExistsById(id);
    }

    public Publisher getPublisherByNameAndCity(String name, String city) {
        logger.info("Get Publisher with name: {" + name + "} and city: {" + city + "}");

        return returnPublisherIfExistsByNameAndCity(name, city);
    }

    public List<Publisher> getAllPublishers() {
        logger.info("Get all publishers");

        return publisherRepository.findAll();
    }

    public Publisher createPublisher(Publisher publisher) {
        logger.info("Create publisher...");

        if (publisherRepository
                .findByNameAndCity(publisher.getName(), publisher.getCity())
                .isPresent())
            throw new ResourceAlreadyExistsException("Publisher already exists!");
        else
            return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(Long id, Publisher publisher) {
        logger.info("Update publisher with id: {}", id);

        Publisher publisherToUpdate = returnPublisherIfExistsById(id);
        publisherToUpdate.setName(publisher.getName());
        publisherToUpdate.setCity(publisher.getCity());

        return publisherRepository.save(publisherToUpdate);
    }

    public Publisher deletePublisherById(Long id) {
        logger.warn("Delete publisher with id: {}", id);

        Publisher publisherToDelete = returnPublisherIfExistsById(id);
        publisherRepository.deleteById(id);

        return publisherToDelete;
    }

    Publisher returnPublisherIfExistsById(Long id) {

        Optional<Publisher> publisherById = publisherRepository.findById(id);
        if (publisherById.isEmpty()) {
            throw new ResourceNotFoundException("Publisher with id: {" + id + "} not found!");
        } else
            return publisherById.get();
    }

    Publisher returnPublisherIfExistsByNameAndCity(String name, String city) {

        if (publisherRepository.findByNameAndCity(name, city).isEmpty()) {
            throw new ResourceNotFoundException("Publisher with name: {" + name + "} and city: {" + city + "} not found!");
        } else
            return publisherRepository.findByNameAndCity(name, city).get();
    }


}




