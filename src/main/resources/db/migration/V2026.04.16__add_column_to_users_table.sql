alter table users
add column `name` varchar(255) not null after id,
add column `email` varchar(255) unique after username,
add column `phone` varchar(20) after email,
add column `firebase_uid` varchar(255) unique after password,
add column `created_at` timestamp default current_timestamp after firebase_uid,
add column `updated_at` timestamp default current_timestamp on update current_timestamp after created_at;

alter table users
add index idx_users_email (email(5)),
add index idx_users_username (username(5)),
add index idx_users_firebase_uid (firebase_uid(5)),
add index idx_users_name (name(5));