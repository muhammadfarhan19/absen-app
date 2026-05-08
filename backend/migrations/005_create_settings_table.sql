-- UP
CREATE TABLE IF NOT EXISTS settings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  setting_key VARCHAR(50) NOT NULL UNIQUE,
  setting_value VARCHAR(100) NOT NULL,
  description VARCHAR(255)
);

-- DOWN
-- DROP TABLE settings;
