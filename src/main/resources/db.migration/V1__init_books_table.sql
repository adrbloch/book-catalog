drop table if exists books;
create table books (
    id          long primary key auto_increment,
    author      varchar(100) not null,
    title       varchar(100) not null,
    description varchar(100) not null,
    rating      int not null
)
