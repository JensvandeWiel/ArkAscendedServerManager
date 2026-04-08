ALTER TABLE "servers"
    DROP COLUMN "server_name";

ALTER TABLE "servers"
    ADD COLUMN "settings" JSON NOT NULL;