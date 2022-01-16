-- User schema

-- !Ups

create table `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` TEXT NOT NULL,
  `tel` TEXT NOT NULL,
  `email` TEXT NOT NULL
)

-- !Downs
drop table `user`
