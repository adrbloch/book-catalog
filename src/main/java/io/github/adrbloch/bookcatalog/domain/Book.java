package io.github.adrbloch.bookcatalog.domain;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @NotBlank(message = "Book title must not be empty")
    private String title;

    @Valid
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Min(value = 1800, message = "Publication year must be >1900")
    @Max(value = 2021 , message = "Publication year must be <=2021")
    private long publicationYear;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    public Book() {
    }

    public Book(Author author, String title, Publisher publisher, long publicationYear) {
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

    public long getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(long publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
