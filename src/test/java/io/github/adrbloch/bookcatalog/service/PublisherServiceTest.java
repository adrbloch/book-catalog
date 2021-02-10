package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.model.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;
    @InjectMocks
    private PublisherService publisherService;


    private Publisher initializePublisher() {

        Publisher publisher = new Publisher("SuperNowa", "Warsaw");
        publisher.setId(1L);
        return publisher;
    }

    @Test
    void createAlreadyExistingPublisherThrowsException() {

        //given
        var publisher = new Publisher();
        given(publisherRepository.findByNameAndCity(publisher.getName(), publisher.getCity())).willReturn(Optional.of(publisher));

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> publisherService.createPublisher(publisher));
    }

    @Test
    void returnPublisherAfterCreate() {

        //given
        Publisher publisher = initializePublisher();
        given(publisherRepository.findByNameAndCity(publisher.getName(), publisher.getCity())).willReturn(Optional.empty());
        given(publisherRepository.save(publisher)).willReturn(publisher);

        //when
        Publisher createdPublisher = publisherService.createPublisher(publisher);

        //then
        assertEquals(1L, createdPublisher.getId());
        assertEquals("SuperNowa", createdPublisher.getName());
        assertEquals("Warsaw", createdPublisher.getCity());


    }

    @Test
    void returnPublisherAfterUpdate() {

        //given
        Publisher publisher = initializePublisher();
        given(publisherRepository.findById(publisher.getId())).willReturn(Optional.of(publisher));
        given(publisherRepository.save(publisher)).willReturn(publisher);

        //when
        Publisher updatedPublisher = publisherService.updatePublisher(publisher.getId(), publisher);

        //then
        assertEquals(1L, updatedPublisher.getId());
        assertEquals("SuperNowa", updatedPublisher.getName());
        assertEquals("Warsaw", updatedPublisher.getCity());
    }

    @Test
    void returnPublisherAfterDelete() {

        //given
        Publisher publisher = initializePublisher();
        given(publisherRepository.findById(publisher.getId())).willReturn(Optional.of(publisher));

        //when
        Publisher deletedPublisher = publisherService.deletePublisherById(publisher.getId());

        //then
        assertEquals(1L, deletedPublisher.getId());
        assertEquals("SuperNowa", deletedPublisher.getName());
        assertEquals("Warsaw", deletedPublisher.getCity());
    }

    @Test
    void ifPublisherNotExistsByIdThrowException() {

        //given
        var publisher = new Publisher();
        Long id = publisher.getId();
        given(publisherRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> publisherService.returnPublisherIfExistsById(id));
    }

    @Test
    void returnPublisherIfExistsById() {

        //given
        Publisher publisher = initializePublisher();
        Long id = publisher.getId();
        given(publisherRepository.findById(id)).willReturn(Optional.of(publisher));

        //when
        Publisher checkedPublisher = publisherService.returnPublisherIfExistsById(id);

        //then
        assertEquals(1L, checkedPublisher.getId());
        assertEquals("SuperNowa", checkedPublisher.getName());
        assertEquals("Warsaw", checkedPublisher.getCity());
    }

    @Test
    void ifPublisherNotExistsByNameAndCityThrowException() {

        //given
        Publisher publisher = new Publisher();
        String publisherName = publisher.getName();
        String publisherCity = publisher.getCity();
        given(publisherRepository.findByNameAndCity(publisherName, publisherCity)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> publisherService
                .returnPublisherIfExistsByNameAndCity(publisherName, publisherCity));
    }

    @Test
    void returnPublisherIfExistsByNameAndCity() {

        //given
        Publisher publisher = initializePublisher();
        String publisherName = publisher.getName();
        String publisherCity = publisher.getCity();
        given(publisherRepository.findByNameAndCity(publisherName, publisherCity)).willReturn(Optional.of(publisher));

        //when
        Publisher checkedPublisher = publisherService.returnPublisherIfExistsByNameAndCity(publisherName, publisherCity);

        //then
        assertEquals(1L, checkedPublisher.getId());
        assertEquals("SuperNowa", checkedPublisher.getName());
        assertEquals("Warsaw", checkedPublisher.getCity());
    }

}