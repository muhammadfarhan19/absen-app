-- UP
CREATE TABLE IF NOT EXISTS attendance (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  date DATE NOT NULL,
  check_in TIME NOT NULL,
  check_out TIME NULL,
  status ENUM('hadir', 'terlambat') NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- DOWN
-- DROP TABLE attendance;
