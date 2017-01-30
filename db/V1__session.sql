create table if not exists `session`
(
`id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
`session_id` VARCHAR(254) NOT NULL,
`session` TEXT NOT NULL,
`state` TEXT NOT NULL
);