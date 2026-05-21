-- UP
ALTER TABLE salary 
ADD COLUMN bpjs_kesehatan DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
ADD COLUMN bpjs_ketenagakerjaan DECIMAL(15, 2) NOT NULL DEFAULT 0.00;

-- DOWN
ALTER TABLE salary 
DROP COLUMN bpjs_kesehatan,
DROP COLUMN bpjs_ketenagakerjaan;
