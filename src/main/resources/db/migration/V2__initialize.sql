drop table if exists users_roles;
drop table if exists users;
drop table if exists roles;


create table users (
  id                    int IDENTITY,
  username              VARCHAR(50) not null UNIQUE,
  password              VARCHAR(80) not null,
  first_name            VARCHAR(50),
  last_name             VARCHAR(50),
  email                 varchar(50) NOT NULL,
  PRIMARY KEY (id)
);


create table roles (
  id                    int IDENTITY,
  name                  VARCHAR(50) not null,
  primary key (id)
);


create table users_roles (
  user_id               INT NOT NULL,
  role_id               INT NOT NULL
--   primary key (user_id, role_id),
--   FOREIGN KEY (user_id)
--   REFERENCES users (id) ,
--   FOREIGN KEY (role_id)
--   REFERENCES roles (id)
);

--
INSERT INTO roles (name)
VALUES
('ROLE_EMPLOYEE'),('ROLE_MANAGER'),('ROLE_ADMIN');

insert into users (username, password, first_name, last_name, email)
values
('admin','$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i','admin','admin','admin@gmail.com'),
('user','$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i','user','user','user@gmail.com');

insert into users_roles (user_id, role_id)
values
(1, 1),
(1, 2),
(1, 3),
(2, 1);