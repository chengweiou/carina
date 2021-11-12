set search_path = carina;

DROP TABLE IF EXISTS person;
CREATE TABLE person (
    id bigserial NOT NULL,
    name character varying NOT NULL,
    imgsrc character varying NOT NULL,
    unread integer NOT NULL,
    createAt timestamp with time zone NOT NULL,
    updateAt timestamp with time zone NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS room;
CREATE TABLE room (
    id bigserial NOT NULL,
    type text NOT NULL,
    personIdListString text NOT NULL,
    createAt timestamp with time zone NOT NULL,
    updateAt timestamp with time zone NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS personRoomRelate;
CREATE TABLE personRoomRelate (
    id bigserial NOT NULL,
    roomId bigserial NOT NULL,
    personId bigserial NOT NULL,
    name character varying NOT NULL,
    imgsrc character varying NOT NULL,
    unread integer NOT NULL,
    lastMessage text NOT NULL,
    lastMessageAt timestamp with time zone  NOT NULL,
    createAt timestamp with time zone NOT NULL,
    updateAt timestamp with time zone NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS history;
CREATE TABLE history (
    id bigserial NOT NULL,
    roomId bigserial NOT NULL,
    personId bigserial NOT NULL,
    senderId bigserial NOT NULL,
    type character varying NOT NULL,
    v text NOT NULL,
    unread boolean NOT NULL,
    createAt timestamp with time zone NOT NULL,
    updateAt timestamp with time zone NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS friend;
CREATE TABLE friend (
    id bigserial NOT NULL,
    personId bigserial NOT NULL,
    targetId bigserial NOT NULL,
    createAt timestamp with time zone NOT NULL,
    updateAt timestamp with time zone NOT NULL,
    PRIMARY KEY (id)
);
