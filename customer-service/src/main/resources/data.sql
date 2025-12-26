-- Sample Customers for Development/Testing
INSERT INTO customers (first_name, last_name, email, phone, address, city, state, zip_code, status, created_at, updated_at)
VALUES 
('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St', 'New York', 'NY', '10001', 'ACTIVE', NOW(), NOW()),
('Jane', 'Smith', 'jane.smith@example.com', '2345678901', '456 Oak Ave', 'Los Angeles', 'CA', '90001', 'ACTIVE', NOW(), NOW()),
('Bob', 'Johnson', 'bob.johnson@example.com', '3456789012', '789 Pine Rd', 'Chicago', 'IL', '60601', 'ACTIVE', NOW(), NOW()),
('Alice', 'Williams', 'alice.williams@example.com', '4567890123', '321 Elm St', 'Houston', 'TX', '77001', 'INACTIVE', NOW(), NOW()),
('Charlie', 'Brown', 'charlie.brown@example.com', '5678901234', '654 Maple Dr', 'Phoenix', 'AZ', '85001', 'ACTIVE', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();