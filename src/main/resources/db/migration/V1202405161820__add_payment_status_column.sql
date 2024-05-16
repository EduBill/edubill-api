ALTER TABLE payment_history
    ADD COLUMN payment_status enum ('PAID','UNPAID');