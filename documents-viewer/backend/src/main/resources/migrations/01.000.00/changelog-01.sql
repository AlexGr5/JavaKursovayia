CREATE TABLE document
(
    id           BIGSERIAL PRIMARY KEY,
    type         VARCHAR NOT NULL,
    organization VARCHAR NOT NULL,
    description  VARCHAR NOT NULL,
    patient      VARCHAR NOT NULL,
    date         DATE    NOT NULL,
    status_code  VARCHAR NOT NULL,
    status_name  VARCHAR NOT NULL
);
