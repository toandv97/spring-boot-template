create if not exists table post_categories (
	id bigint not null auto_increment,
	name varchar(256) not null,
	slug varchar(256) not null,
	description varchar(255),
	status enum ('ACTIVE','INACTIVE'),
	created_at datetime(6),
	updated_at datetime(6),
	created_by bigint not null,
	updated_by bigint,
	primary key (id)
	unique key (slug)
	index (slug(10)),
) engine=InnoDB charset=utf8mb4 collate=utf8mb4_unicode_ci;

create if not exists table posts (
	id bigint not null auto_increment,
	title varchar(256) not null,
	slug varchar(256) not null,
	content TEXT,
	status enum ('ACTIVE','INACTIVE'),
	category_id bigint not null,
	created_at datetime(6),
	updated_at datetime(6),
	created_by bigint not null,
	updated_by bigint,
	primary key (id)
	unique key (slug),
	index (category_id),
	index (slug(10)),
) engine=InnoDB charset=utf8mb4 collate=utf8mb4_unicode_ci;
