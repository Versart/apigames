create table users(
    id bigint auto_increment,
    login varchar(100) not null unique,
    password varchar(100) not null unique,
    role varchar(255) not null,
    primary key(id)
);