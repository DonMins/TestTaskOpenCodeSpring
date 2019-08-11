-- Table: users
CREATE TABLE users (
    id int not null auto_increment ,
                       username VARCHAR(255) NOT NULL PRIMARY KEY,
                       password VARCHAR(255) NOT NULL
);

-- Table: info for ratings
CREATE TABLE ratings (
    id int not null auto_increment,
                            countgame LONG ,
                            allAttempt LONG,
                            username VARCHAR(255) NOT NULL PRIMARY KEY,
                            youNumber VARCHAR(255)
);

-- Table: history
CREATE TABLE history (
    id int auto_increment,
                         username VARCHAR(255) NOT NULL PRIMARY KEY,
                         data LONGTEXT
);

