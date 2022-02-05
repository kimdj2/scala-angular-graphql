-- User schema

-- !Ups

create table `POSTS` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `TITLE` TEXT NOT NULL,
  `CONTENT` TEXT NOT NULL
)

-- !Downs
drop table `POSTS`
