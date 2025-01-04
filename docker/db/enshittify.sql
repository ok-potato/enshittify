create table releases (
	id uuid primary key,
	title varchar(500)
);

create index idx_release_title on releases(title);

create table tracks (
	release_id uuid,
	number int,
	title varchar(500),
	length_seconds int,
	primary key (release_id, number),
	foreign key (release_id) references releases on delete cascade
);

create index idx_track_title on tracks(title);

create table artists (
	id uuid primary key,
	name varchar(500)
);

create index idx_artist_name on artists(name);

create table release_features (
	artist_id uuid,
	release_id uuid,
	primary key (artist_id, release_id),
	foreign key (artist_id) references artists,
	foreign key (release_id) references releases on delete cascade
);

create table track_features (
	artist_id uuid,
	release_id uuid,
	number int,
	primary key (artist_id, release_id, number),
	foreign key (artist_id) references artists,
	foreign key (release_id, number) references tracks(release_id, number) on delete cascade
);