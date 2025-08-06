INSERT INTO useradmindb.USERS (
                    id
                  , account_id
                  , password
                  , user_name
                  , resident_registration_number
                  , phone_number
                  , city
                  , address
                  , role_type
                  , CREATE_DATE
                  , MODIFY_DATE
)
SELECT
    1
     , 'admin'
     , '$2a$10$F4fmGkbOZTsuaI1RD4VGVutBZ.7RXdPPuWkMrWny40SIFNEy1nuTO'  -- 1234
     , '관리자'
     , 'PEOwtJjeJTk5ddNy+v3EQw=='                       -- 000000-0000000
     , '010-0000-0000'
     , '서울특별시'
     , '강남구 테헤란로 510'
     , 'ADMIN'
     , CURRENT_TIMESTAMP()
     , CURRENT_TIMESTAMP()
    WHERE NOT EXISTS (
    SELECT  1
    FROM    useradmindb.USERS
    WHERE   id = 1
)
;