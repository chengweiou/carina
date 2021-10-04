set search_path = carina;

insert into person(id, name, imgsrc, unread, createAt, updateAt) values
    (1, 'ou', '', 0, '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (2, 'chiu', '', 1, '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (3, 'jasmine', '', 0, '2019-01-01T00:00:00', '2019-01-01T00:00:00')
;

insert into room(type, personIdListString, createAt, updateAt) values
    ('SOLO', '1,2', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('GROUP', '1,2,3', '2019-01-01T00:00:00', '2019-01-01T00:00:00')
;

insert into personRoomRelate(personId, roomId, name, imgsrc, unread, lastMessage, createAt, updateAt) values
    (1, 1, 'chiu', '', 0, 'lastmessage 1', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (2, 1, 'ou', '', 1, 'lastmessage 1', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (1, 2, 'backyard', '', 0, 'lastmessage 1', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (2, 2, 'backyard', '', 0, 'lastmessage 1', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (3, 2, 'backyard', '', 0, 'lastmessage 1', '2019-01-01T00:00:00', '2019-01-01T00:00:00')
;

insert into history(roomId, personId, senderId, type, v, unread, createAt, updateAt) values
    (1, 1, 1, 'TEXT', 'v 1', false, '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (1, 2, 1, 'TEXT', 'v 2', true, '2019-01-01T00:00:00', '2019-01-01T00:00:00')
;

insert into friend(personId, targetId, createAt, updateAt) values
    (1, 2, '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (2, 1, '2019-01-01T00:00:00', '2019-01-01T00:00:00')
;
