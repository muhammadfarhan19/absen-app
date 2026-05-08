CREATE DATABASE IF NOT EXISTS kepegawaian_db;
USE kepegawaian_db;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('admin', 'karyawan') NOT NULL DEFAULT 'karyawan'
);

CREATE TABLE IF NOT EXISTS employees (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  gaji_pokok DECIMAL(15, 2) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS attendance (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  date DATE NOT NULL,
  check_in TIME NOT NULL,
  check_out TIME NULL,
  status ENUM('hadir', 'terlambat') NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS salary (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  month VARCHAR(7) NOT NULL, -- Format: YYYY-MM
  total_gaji DECIMAL(15, 2) NOT NULL,
  total_potongan DECIMAL(15, 2) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample admin
INSERT IGNORE INTO users (id, name, email, password, role) VALUES 
(1, 'Super Admin', 'admin@mail.com', '$2b$10$3zR1M6hR7E0F7d0QhO7gR.a6x/d8/hM5i0I/h4m5M7pA7Y1j1XW.i', 'admin'); -- pass: admin123
