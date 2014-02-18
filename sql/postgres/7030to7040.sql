-- 20130930
ALTER TABLE TURBINE_USER ADD MIGRATE_VERSION INTEGER NOT NULL DEFAULT 0;
UPDATE TURBINE_USER SET MIGRATE_VERSION = 0 ;
ALTER TABLE TURBINE_USER ALTER COLUMN TUTORIAL_FORBID TYPE VARCHAR (64);
-- 20130930

-- 20131111
-----------------------------------------------------------------------------
-- EIP_T_GPDB
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_GPDB
(
    GPDB_ID INTEGER NOT NULL,
    GPDB_NAME TEXT NOT NULL,
    MAIL_FLG VARCHAR (1) NOT NULL,
    CREATE_USER_ID INTEGER NOT NULL,
    CREATE_DATE TIMESTAMP,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY (GPDB_ID)
);

-----------------------------------------------------------------------------
-- EIP_T_GPDB
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_GPDB_ITEM
(
    GPDB_ITEM_ID INTEGER NOT NULL,
    GPDB_ID INTEGER NOT NULL,
    GPDB_ITEM_NAME TEXT NOT NULL,
    TITLE_FLG VARCHAR (1) NOT NULL,
    REQUIRED_FLG VARCHAR (1) NOT NULL,
    TYPE VARCHAR (2) NOT NULL,
    GPDB_KUBUN_ID INTEGER,
    LIST_FLG VARCHAR (1) NOT NULL,
    DETAIL_FLG VARCHAR (1) NOT NULL,
    SIZE_COL INTEGER,
    SIZE_ROW INTEGER,
    LINE INTEGER,
    ORDER_NO INTEGER NOT NULL,
    DEFAULT_SORT_FLG VARCHAR (1) NOT NULL,
    ASC_DESC VARCHAR (4),
    CREATE_USER_ID INTEGER NOT NULL,
    CREATE_DATE TIMESTAMP,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY (GPDB_ITEM_ID)
);

-----------------------------------------------------------------------------
-- EIP_T_GPDB_RECORD
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_GPDB_RECORD
(
    GPDB_RECORD_ID INTEGER NOT NULL,
    GPDB_ID INTEGER NOT NULL,
    GPDB_ITEM_ID INTEGER NOT NULL,
    RECORD_NO INTEGER NOT NULL,
    VALUE TEXT,
    CREATE_USER_ID INTEGER NOT NULL,
    UPDATE_USER_ID INTEGER NOT NULL,
    CREATE_DATE TIMESTAMP,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY (GPDB_RECORD_ID)
);

-----------------------------------------------------------------------------
-- EIP_T_GPDB_RECORD_FILE
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_GPDB_RECORD_FILE
(
    FILE_ID INTEGER NOT NULL,
    OWNER_ID INTEGER,
    GPDB_RECORD_ID INTEGER,
    FILE_NAME VARCHAR (128) NOT NULL,
    FILE_PATH TEXT NOT NULL,
    FILE_THUMBNAIL bytea,
    CREATE_DATE DATE,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY (FILE_ID)
);

-----------------------------------------------------------------------------
-- EIP_M_GPDB_KUBUN
-----------------------------------------------------------------------------

CREATE TABLE EIP_M_GPDB_KUBUN
(
    GPDB_KUBUN_ID INTEGER NOT NULL,
    GPDB_KUBUN_NAME TEXT NOT NULL,
    CREATE_DATE TIMESTAMP,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY (GPDB_KUBUN_ID)
);

-----------------------------------------------------------------------------
-- EIP_M_GPDB_KUBUN_VALUE
-----------------------------------------------------------------------------

CREATE TABLE EIP_M_GPDB_KUBUN_VALUE
(
    GPDB_KUBUN_VALUE_ID INTEGER NOT NULL,
    GPDB_KUBUN_ID INTEGER NOT NULL,
    GPDB_KUBUN_VALUE TEXT NOT NULL,
    ORDER_NO INTEGER NOT NULL,
    CREATE_DATE TIMESTAMP,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY (GPDB_KUBUN_VALUE_ID)
);

CREATE SEQUENCE pk_eip_t_gpdb INCREMENT 20;
CREATE SEQUENCE pk_eip_t_gpdb_item INCREMENT 20;
CREATE SEQUENCE pk_eip_t_gpdb_record INCREMENT 20;
CREATE SEQUENCE pk_eip_t_gpdb_record_file INCREMENT 20;
CREATE SEQUENCE pk_eip_m_gpdb_kubun INCREMENT 20;
CREATE SEQUENCE pk_eip_m_gpdb_kubun_value INCREMENT 20;

ALTER SEQUENCE pk_eip_t_gpdb OWNED BY EIP_T_GPDB.GPDB_ID;
ALTER SEQUENCE pk_eip_t_gpdb_item OWNED BY EIP_T_GPDB_ITEM.GPDB_ITEM_ID;
ALTER SEQUENCE pk_eip_t_gpdb_record OWNED BY EIP_T_GPDB_RECORD.GPDB_RECORD_ID;
ALTER SEQUENCE pk_eip_t_gpdb_record_file OWNED BY EIP_T_GPDB_RECORD_FILE.FILE_ID;
ALTER SEQUENCE pk_eip_m_gpdb_kubun OWNED BY EIP_M_GPDB_KUBUN.GPDB_KUBUN_ID;
ALTER SEQUENCE pk_eip_m_gpdb_kubun_value OWNED BY EIP_M_GPDB_KUBUN_VALUE.GPDB_KUBUN_VALUE_ID;
-- 20131111

CREATE INDEX eip_t_gpdb_record_record_no_index ON EIP_T_GPDB_RECORD (RECORD_NO);

INSERT INTO EIP_M_GPDB_KUBUN VALUES (1, '都道府県', now(), now());
SELECT setval('pk_eip_m_gpdb_kubun',1);

INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (1, 1, '北海道', 1, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (2, 1, '青森県', 2, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (3, 1, '岩手県', 3, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (4, 1, '宮城県', 4, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (5, 1, '秋田県', 5, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (6, 1, '山形県', 6, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (7, 1, '福島県', 7, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (8, 1, '茨城県', 8, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (9, 1, '栃木県', 9, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (10, 1, '群馬県', 10, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (11, 1, '埼玉県', 11, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (12, 1, '千葉県', 12, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (13, 1, '東京都', 13, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (14, 1, '神奈川県', 14, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (15, 1, '新潟県', 15, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (16, 1, '富山県', 16, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (17, 1, '石川県', 17, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (18, 1, '福井県', 18, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (19, 1, '山梨県', 19, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (20, 1, '長野県', 20, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (21, 1, '岐阜県', 21, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (22, 1, '静岡県', 22, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (23, 1, '愛知県', 23, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (24, 1, '三重県', 24, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (25, 1, '滋賀県', 25, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (26, 1, '京都府', 26, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (27, 1, '大阪府', 27, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (28, 1, '兵庫県', 28, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (29, 1, '奈良県', 29, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (30, 1, '和歌山県', 30, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (31, 1, '鳥取県', 31, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (32, 1, '島根県', 32, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (33, 1, '岡山県', 33, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (34, 1, '広島県', 34, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (35, 1, '山口県', 35, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (36, 1, '徳島県', 36, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (37, 1, '香川県', 37, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (38, 1, '愛媛県', 38, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (39, 1, '高知県', 39, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (40, 1, '福岡県', 40, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (41, 1, '佐賀県', 41, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (42, 1, '長崎県', 42, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (43, 1, '熊本県', 43, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (44, 1, '大分県', 44, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (45, 1, '宮崎県', 45, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (46, 1, '鹿児島県', 46, now(), now());
INSERT INTO EIP_M_GPDB_KUBUN_VALUE VALUES (47, 1, '沖縄県', 47, now(), now());
SELECT setval('pk_eip_m_gpdb_kubun_value',47);


-----------------------------------------------------------------------------
-- EIP_T_WIKI_CATEGORY
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_WIKI_CATEGORY
(
    CATEGORY_ID INTEGER NOT NULL,
    USER_ID INTEGER NOT NULL,
    UPDATE_USER_ID INTEGER NOT NULL,
    CATEGORY_NAME VARCHAR (64) NOT NULL,
    CREATE_DATE DATE,
    UPDATE_DATE TIMESTAMP,
    PRIMARY KEY(CATEGORY_ID)
);

-----------------------------------------------------------------------------
-- EIP_T_WIKI
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_WIKI
(
    WIKI_ID INTEGER NOT NULL,
    WIKI_NAME VARCHAR (64) NOT NULL,
    PARENT_ID INTEGER DEFAULT 0,
    CATEGORY_ID INTEGER,
    NOTE TEXT,
    CREATE_USER_ID INTEGER,
    UPDATE_USER_ID INTEGER,
    CREATE_DATE DATE,
    UPDATE_DATE TIMESTAMP,
    FOREIGN KEY (CATEGORY_ID) REFERENCES EIP_T_WIKI_CATEGORY (CATEGORY_ID) ON DELETE CASCADE,
    PRIMARY KEY(WIKI_ID)
);

-----------------------------------------------------------------------------
-- EIP_T_WIKI_FILE
-----------------------------------------------------------------------------

CREATE TABLE EIP_T_WIKI_FILE
(
    FILE_ID INTEGER NOT NULL,
    OWNER_ID INTEGER,
    WIKI_ID INTEGER,
    FILE_NAME VARCHAR (128) NOT NULL,
    FILE_PATH TEXT NOT NULL,
    FILE_THUMBNAIL bytea,
    CREATE_DATE DATE,
    UPDATE_DATE TIMESTAMP,
    FOREIGN KEY (WIKI_ID) REFERENCES EIP_T_WIKI (WIKI_ID) ON DELETE CASCADE,
    PRIMARY KEY (FILE_ID)
);

CREATE SEQUENCE pk_eip_t_wiki INCREMENT 20;
CREATE SEQUENCE pk_eip_t_wiki_category INCREMENT 20;
CREATE SEQUENCE pk_eip_t_wiki_file INCREMENT 20;

ALTER SEQUENCE pk_eip_t_wiki_category OWNED BY EIP_T_WIKI_CATEGORY.CATEGORY_ID;
ALTER SEQUENCE pk_eip_t_wiki OWNED BY EIP_T_WIKI.WIKI_ID;
ALTER SEQUENCE pk_eip_t_wiki_file OWNED BY EIP_T_WIKI_FILE.FILE_ID;

INSERT INTO EIP_T_WIKI_CATEGORY VALUES(1,0,0,'未分類',now(), now());
SELECT setval('pk_eip_t_wiki_category',1);

CREATE INDEX eip_t_wiki_wiki_name_parent_id_index ON EIP_T_WIKI (WIKI_NAME, PARENT_ID);
CREATE INDEX eip_t_file_wiki_id_index ON EIP_T_WIKI_FILE (WIKI_ID);
