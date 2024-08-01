CREATE TABLE "user_profile" (
    id                              TEXT NOT NULL PRIMARY KEY,
    email                           TEXT NOT NULL UNIQUE,
    password                        TEXT NOT NULL,
    first_name                      TEXT,
    birthday_date                   DATE,
    create_time                     TIMESTAMP WITH TIME ZONE NOT NULL,
    update_time                     TIMESTAMP WITH TIME ZONE NOT NULL
);
