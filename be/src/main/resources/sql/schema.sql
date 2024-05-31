CREATE TABLE user
(
    name       varchar(255) PRIMARY KEY,
    image_path varchar(1000)
);

CREATE TABLE milestone
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        varchar(255) NOT NULL,
    description varchar(255),
    end_date    DATE,
    is_open     TINYINT(1) default 1 NOT NULL
);

CREATE TABLE label
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             varchar(255) NOT NULL,
    background_color varchar(20)  NOT NULL,
    text_color       varchar(20)  NOT NULL,
    description      varchar(255)
);

CREATE TABLE issue
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    milestone_id BIGINT,
    reporter     VARCHAR(255) NOT NULL,
    created_at   DATETIME     NOT NULL,
    is_open      TINYINT(1) default 1 NOT NULL,
    foreign key (milestone_id) references milestone (id),
    foreign key (reporter) references user (name)
);

CREATE TABLE comment
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_id   BIGINT       NOT NULL,
    reporter   varchar(255) NOT NULL,
    created_at DATETIME     NOT NULL,
    contents   text         NOT NULL,
    foreign key (issue_id) references issue (id),
    foreign key (reporter) references user (name)
);

CREATE TABLE issue_assignee
(
    issue_id  BIGINT       NOT NULL,
    user_name varchar(255) NOT NULL,
    PRIMARY KEY (issue_id, user_name),
    foreign key (issue_id) references issue (id),
    foreign key (user_name) references user (name)
);

CREATE TABLE issue_label
(
    issue_id BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    PRIMARY KEY (issue_id, label_id),
    foreign key (issue_id) references issue (id),
    foreign key (label_id) references label (id)
);