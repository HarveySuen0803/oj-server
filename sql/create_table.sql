create database if not exists oj;

use oj;

create table if not exists user (
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null,
    user_password varchar(512)                           not null,
    union_id      varchar(256)                           null,
    mp_open_id    varchar(256)                           null,
    user_name     varchar(256)                           null,
    user_avatar   varchar(1024)                          null,
    user_profile  varchar(512)                           null,
    user_role     varchar(256) default 'user'            not null,
    create_time   datetime     default current_timestamp not null,
    update_time   datetime     default current_timestamp not null on update current_timestamp,
    is_delete     tinyint      default 0                 not null,
    index idx_unionid (union_id)
) collate = utf8mb4_unicode_ci;

create table if not exists question (
    id           bigint auto_increment primary key  not null,
    user_id      bigint                             not null,
    title        varchar(512)                       null,
    content      text                               null,
    tags         varchar(1024)                      null,
    answer       text     default 0                 null,
    submit_num   int      default 0                 not null,
    accepted_num int      default 0                 not null,
    judge_case   text                               null,
    judge_config text                               null,
    thumb_num    int      default 0                 not null,
    favour_num   int      default 0                 not null,
    create_time  datetime default current_timestamp not null,
    update_time  datetime default current_timestamp not null on update current_timestamp,
    is_delete    tinyint  default 0                 not null,
    index idx_user_id (user_id)
) collate = utf8mb4_unicode_ci;

create table if not exists question_submit (
    id          bigint auto_increment comment 'id' primary key,
    question_id bigint                             not null,
    user_id     bigint                             not null,
    language    varchar(128)                       not null,
    code        text                               not null,
    judge_info  text                               not null,
    status      int      default 0                 not null,
    create_time datetime default current_timestamp not null,
    update_time datetime default current_timestamp not null on update current_timestamp,
    is_delete   tinyint  default 0                 not null,
    index idx_question_id (question_id),
    index idx_user_id (user_id)
) collate = utf8mb4_unicode_ci