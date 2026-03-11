-- Удаляем избыточную колонку is_virtual
ALTER TABLE users DROP COLUMN IF EXISTS is_virtual;
