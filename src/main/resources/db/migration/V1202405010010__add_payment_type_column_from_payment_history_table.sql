ALTER TABLE payment_history
    ADD COLUMN payment_type ENUM('CASH', 'CREDIT_CARD', 'BANK_TRANSFER');
