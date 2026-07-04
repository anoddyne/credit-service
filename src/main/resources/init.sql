-- Удаляем таблицы, если они существуют (чтобы скрипт запускать много раз)
DROP TABLE IF EXISTS payment_schedule CASCADE;
DROP TABLE IF EXISTS loan_applications CASCADE;
DROP TABLE IF EXISTS loans CASCADE;
DROP TABLE IF EXISTS loan_products CASCADE;

DROP TABLE IF EXISTS loan_statuses CASCADE;
DROP TABLE IF EXISTS loan_application_statuses CASCADE;
DROP TABLE IF EXISTS payment_statuses CASCADE;

-- Таблицы-справочники статусов
CREATE TABLE loan_statuses (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE loan_application_statuses (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE payment_statuses (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

-- Наполнение таблиц-справочников
INSERT INTO loan_application_statuses(code, description) VALUES
('NEW', 'Новая заявка'),
('IN_PROGRESS', 'На проверке/скоринге'),
('APPROVED', 'Одобрена'),
('REJECTED', 'Отклонена');

INSERT INTO loan_statuses(code, description) VALUES
('ACTIVE', 'Кредит выдан и активен'),
('OVERDUE', 'По кредиту имеется просроченная задолженность'),
('CLOSED', 'Кредит полностью погашен'),
('RESTRUCTURED', 'Условия кредита изменены (реструктуризация)'),
('CHARGE_OFF', 'Долг признан безнадежным и списан');

INSERT INTO payment_statuses(code, description) VALUES
('NEW', 'Новая заявка'),
('IN_PROGRESS', 'На проверке/скоринге'),
('APPROVED', 'Одобрена'),
('REJECTED', 'Отклонена');

-- Создание основных таблиц
CREATE TABLE loan_products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    interest_rate NUMERIC(5, 2) NOT NULL,
    min_amount NUMERIC(15, 2) NOT NULL,
    max_amount NUMERIC(15, 2) NOT NULL,
    min_term_months INT NOT NULL,
    max_term_months INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE loan_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL,
    product_id UUID NOT NULL,
    requested_amount NUMERIC(15, 2) NOT NULL,
    requested_term_months INT NOT NULL,
    status_code VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_applications_product FOREIGN KEY (product_id) REFERENCES loan_products(id),
    CONSTRAINT fk_application_status FOREIGN KEY (status_code) REFERENCES loan_application_statuses(code)
);

CREATE TABLE loans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID UNIQUE NOT NULL,
    account_id UUID NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    balance_owed NUMERIC(15, 2) NOT NULL,
    interest_rate NUMERIC(5, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status_code VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_loans_applications FOREIGN KEY (application_id) REFERENCES loan_applications(id),
    CONSTRAINT fk_loans_status FOREIGN KEY (status_code) REFERENCES loan_statuses(code)
);

CREATE TABLE payment_schedule (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_id UUID NOT NULL,
    payment_date DATE NOT NULL,
    total_payment NUMERIC(15, 2) NOT NULL,
    principal_payment NUMERIC(15, 2) NOT NULL,
    interest_payment NUMERIC(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_schedule_loan FOREIGN KEY (loan_id) REFERENCES loans(id),
    CONSTRAINT fk_payment_status FOREIGN KEY (status) REFERENCES payment_statuses(code)
);

-- Тестовые данные для кредитных продуктов
INSERT INTO loan_products (name, interest_rate, min_amount, max_amount, min_term_months, max_term_months) VALUES
('Потребительский кредит', 14.5, 50000.00, 1000000.00, 3, 60),
('Ипотека Новостройка', 8.00, 1000000.00, 15000000.00, 12, 360),
('Микрозайм Наличными', 24.9, 10000.00, 100000.00, 1, 12),
('Автокредит Драйв', 16.5, 300000.00, 5000000.00, 12, 84),
('Семейная Ипотека', 6.0, 500000.00, 12000000.00, 36, 360),
('Образовательный с господдержкой', 3.0, 20000.00, 500000.00, 6, 120),
('Премиум Прайм', 11.2, 2000000.00, 30000000.00, 6, 84);
