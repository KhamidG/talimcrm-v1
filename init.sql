-- Initialization script for TalimCRM database
-- This script runs when the database is first created

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE talimcrm_db TO talimcrm_user;

-- Optional: Create some initial data or indexes here
-- Example:
-- CREATE INDEX IF NOT EXISTS idx_students_status ON students_entity(status);
-- CREATE INDEX IF NOT EXISTS idx_groups_teacher ON groups_entity(teacher_id);

-- Log initialization
DO $$
BEGIN
    RAISE NOTICE 'TalimCRM database initialized successfully';
END $$;
