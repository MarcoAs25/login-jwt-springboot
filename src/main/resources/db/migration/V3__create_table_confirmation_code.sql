CREATE TABLE IF NOT EXISTS confirmation_code(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    token VARCHAR(255) NULL DEFAULT NULL,
    code_type VARCHAR(30) NOT NULL,
    expiry_date TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);