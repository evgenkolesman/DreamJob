
CREATE TABLE IF NOT EXISTS post (
                      id SERIAL PRIMARY KEY,
                      name TEXT,
                      description TEXT,
                      created TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidates (
    id SERIAL PRIMARY KEY ,
    name TEXT
);
