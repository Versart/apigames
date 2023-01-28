alter table game add column company_id bigint not null;

alter table game add foreign key(company_id) references company(id);