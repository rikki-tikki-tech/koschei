CREATE TABLE "user_profile" (
    id                              VARCHAR(16) NOT NULL PRIMARY KEY,
    email                           VARCHAR(64) NOT NULL UNIQUE,
    password                        VARCHAR(256) NOT NULL,
    first_name                      VARCHAR(16),
    birthday_date                   DATE,
    create_time                     TIMESTAMP WITH TIME ZONE NOT NULL,
    update_time                     TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX user_profile_email_unique ON "user_profile"(email);
