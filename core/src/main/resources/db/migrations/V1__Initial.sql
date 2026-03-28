CREATE TABLE servers (
    id UUID PRIMARY KEY,
    profile_name varchar(255) NOT NULL,
    server_name varchar(255) NOT NULL,
    installation_location text NOT NULL
)