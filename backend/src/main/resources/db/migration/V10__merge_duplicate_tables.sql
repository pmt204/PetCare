-- Add medical_record_id column to invoices table
ALTER TABLE invoices ADD COLUMN medical_record_id bigint REFERENCES medical_records(id) ON DELETE SET NULL;

-- Drop bills table (since invoices will serve as the billing info)
DROP TABLE IF EXISTS bills CASCADE;

-- Add missing columns to test_results to accommodate lab_results properties
ALTER TABLE test_results ADD COLUMN file_name varchar(255);
ALTER TABLE test_results ADD COLUMN mime_type varchar(100);
ALTER TABLE test_results ADD COLUMN note varchar(1000);

-- Migrate data from lab_results into test_results
INSERT INTO test_results (
    medical_record_id, 
    test_name, 
    file_name, 
    pdf_url, 
    mime_type, 
    result, 
    create_at, 
    update_at, 
    status
)
SELECT 
    medical_record_id, 
    title, 
    file_name, 
    file_url, 
    mime_type, 
    note, 
    created_at, 
    updated_at, 
    'COMPLETED' 
FROM lab_results;

-- Drop lab_results table
DROP TABLE IF EXISTS lab_results CASCADE;
