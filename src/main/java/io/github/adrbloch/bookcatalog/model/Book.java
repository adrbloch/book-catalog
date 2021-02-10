package io.github.adrbloch.bookcatalog.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @ManyToOne
    private Author author;

    @NotBlank(message = "Book title must not be empty")
    private String title;

    @Valid
    @ManyToOne
    private Publisher publisher;

    @Range(min = 1000, max = 2100, message ="Invalid publication year")
    @NotNull(message = "Invalid publication year")
    private Integer publicationYear;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    public Book() {
    }

    public Book(Author author, String title, Publisher publisher, Integer publicationYear) {
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
