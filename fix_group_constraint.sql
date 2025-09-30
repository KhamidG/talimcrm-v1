-- Удалить уникальное ограничение на group_id в таблице students_entity
ALTER TABLE students_entity DROP CONSTRAINT IF EXISTS students_entity_group_id_key;

-- Проверить результат
SELECT constraint_name, constraint_type 
FROM information_schema.table_constraints 
WHERE table_name = 'students_entity';
