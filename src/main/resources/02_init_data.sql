-- Запускать ПОСЛЕ 01_init.sql (структура таблиц должна уже существовать)

-- clients

INSERT INTO clients.client_statuses (id, value, description) VALUES
                                                                 ('a0000000-0000-0000-0000-000000000001', 'ACTIVE',   'Активный клиент'),
                                                                 ('a0000000-0000-0000-0000-000000000002', 'BLOCKED',  'Заблокирован'),
                                                                 ('a0000000-0000-0000-0000-000000000003', 'ARCHIVED', 'В архиве'),
                                                                 ('a0000000-0000-0000-0000-000000000004', 'PENDING',  'Ожидает проверки');

INSERT INTO clients.clients (id, first_name, last_name, middle_name, passport_number, phone, email, birth_date, address, created_at, updated_at) VALUES
                                                                                                                                                     ('b0000000-0000-0000-0000-000000000001', 'Иван', 'Иванов', 'Петрович',
                                                                                                                                                      '1234 567890', '+79001234567', 'ivanov@mail.ru',
                                                                                                                                                      '1990-05-15', 'г. Москва, ул. Ленина, д. 1', now(), now()),
                                                                                                                                                     ('b0000000-0000-0000-0000-000000000002', 'Мария', 'Петрова', 'Сергеевна',
                                                                                                                                                      '9876 543210', '+79007654321', 'petrova@mail.ru',
                                                                                                                                                      '1985-03-22', 'г. Санкт-Петербург, пр. Невский, д. 10', now(), now()),
                                                                                                                                                     ('b0000000-0000-0000-0000-000000000003', 'Алексей', 'Сидоров', NULL,
                                                                                                                                                      '1111 222233', '+79111234567', NULL,
                                                                                                                                                      '2000-11-01', NULL, now(), now());

INSERT INTO clients.clients (id, first_name, last_name, middle_name, passport_number, phone, email, birth_date, address, created_at, updated_at) VALUES
    ('b0000000-0000-0000-0000-000000000004', 'Борис', 'Борисов', 'Борисович',
     '3456 234567', '+79990000000', 'bbborisov@corp.ru',
     NULL, 'г. Москва, ул. Корпоративная, д. 5', now(), now());

INSERT INTO clients.client_status_history (id, client_id, status_id, changed_at) VALUES
                                                                                     (gen_random_uuid(), 'b0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', now() - interval '29 days'),
                                                                                     (gen_random_uuid(), 'b0000000-0000-0000-0000-000000000002', 'a0000000-0000-0000-0000-000000000002', now() - interval '10 days'),
                                                                                     (gen_random_uuid(), 'b0000000-0000-0000-0000-000000000003', 'a0000000-0000-0000-0000-000000000004', now() - interval '1 days'),
                                                                                     (gen_random_uuid(), 'b0000000-0000-0000-0000-000000000004', 'a0000000-0000-0000-0000-000000000001', now() - interval '60 days');

-- accounts

INSERT INTO accounts.account_types (id, description, name) VALUES
                                                               ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Current account', 'CURRENT'),
                                                               ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Savings account', 'SAVINGS'),
                                                               ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Deposit account', 'DEPOSIT'),
                                                               ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Credit account', 'CREDIT');

INSERT INTO accounts.currencies (id, code, name, symbol) VALUES
                                                             ('11111111-1111-1111-1111-111111111111', 'RUB', 'Russian Ruble', '₽'),
                                                             ('22222222-2222-2222-2222-222222222222', 'USD', 'US Dollar', '$'),
                                                             ('33333333-3333-3333-3333-333333333333', 'EUR', 'Euro', '€'),
                                                             ('44444444-4444-4444-4444-444444444444', 'GBP', 'British Pound', '£'),
                                                             ('55555555-5555-5555-5555-555555555555', 'JPY', 'Japanese Yen', '¥'),
                                                             ('66666666-6666-6666-6666-666666666666', 'CNY', 'Chinese Yuan', '¥'),
                                                             ('77777777-7777-7777-7777-777777777777', 'CHF', 'Swiss Franc', 'CHF'),
                                                             ('88888888-8888-8888-8888-888888888888', 'KZT', 'Kazakh Tenge', '₸');

INSERT INTO accounts.accounts (id, account_number, account_status, balance, created_at, created_by, owner_id, owner_type, updated_at, updated_by, version, account_type_id, currency_id) VALUES
                                                                                                                                                                                             ('fb226e43-b9ea-49d7-9147-980c3ebc503b', '40817810000000000001', 'ACTIVE', 10500.25,
                                                                                                                                                                                              '2026-07-01 17:50:58.19505', 'system',
                                                                                                                                                                                              'b0000000-0000-0000-0000-000000000001', 'INDIVIDUAL',
                                                                                                                                                                                              '2026-07-01 17:50:58.19505', 'system', 0,
                                                                                                                                                                                              'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222'),
                                                                                                                                                                                             ('241b093f-366e-4f3f-845c-76f604a010f4', '40817810000000000002', 'ACTIVE', 9800000.00,
                                                                                                                                                                                              '2026-07-01 17:50:58.19505', 'system',
                                                                                                                                                                                              'b0000000-0000-0000-0000-000000000004', 'CORPORATE',
                                                                                                                                                                                              '2026-07-01 17:50:58.19505', 'system', 0,
                                                                                                                                                                                              'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111');

INSERT INTO accounts.accounts (id, account_number, account_status, balance, created_at, created_by, owner_id, owner_type, updated_at, updated_by, version, account_type_id, currency_id) VALUES
    ('c1000000-0000-0000-0000-000000000001', '40817810000000000003', 'ACTIVE', 250000.00,
     now(), 'system',
     'b0000000-0000-0000-0000-000000000002', 'INDIVIDUAL',
     now(), 'system', 0,
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111');

INSERT INTO accounts.account_status_history (id, changed_at, changed_by, new_status, old_status, reason, account_id) VALUES
                                                                                                                         ('98afe153-a5d2-4afc-a4ee-6e49f1152859', '2026-07-01 17:50:58.19799', 'system', 'ACTIVE', 'ACTIVE', 'Initial status', 'fb226e43-b9ea-49d7-9147-980c3ebc503b'),
                                                                                                                         ('f83afa4b-0c41-4402-9e9e-723286e28ba1', '2026-07-01 17:50:58.19799', 'system', 'ACTIVE', 'ACTIVE', 'Initial status', '241b093f-366e-4f3f-845c-76f604a010f4'),
                                                                                                                         (gen_random_uuid(), now(), 'system', 'ACTIVE', 'ACTIVE', 'Initial status', 'c1000000-0000-0000-0000-000000000001');

-- loans

INSERT INTO loans.loan_application_statuses (code, description) VALUES
                                                                    ('NEW', 'Новая заявка'),
                                                                    ('IN_PROGRESS', 'На проверке/скоринге'),
                                                                    ('APPROVED', 'Одобрена'),
                                                                    ('REJECTED', 'Отклонена');

INSERT INTO loans.loan_statuses (code, description) VALUES
                                                        ('ACTIVE', 'Кредит выдан и активен'),
                                                        ('OVERDUE', 'По кредиту имеется просроченная задолженность'),
                                                        ('CLOSED', 'Кредит полностью погашен'),
                                                        ('RESTRUCTURED', 'Условия кредита изменены (реструктуризация)'),
                                                        ('CHARGE_OFF', 'Долг признан безнадежным и списан');

INSERT INTO loans.payment_statuses (code, description) VALUES
                                                           ('FUTURE', 'Запланирован'),
                                                           ('PAID', 'Оплачен полностью'),
                                                           ('PARTIALLY_PAID', 'Оплачен частично'),
                                                           ('OVERDUE', 'Просрочен');

INSERT INTO loans.loan_products (id, name, interest_rate, min_amount, max_amount, min_term_months, max_term_months) VALUES
                                                                                                                        ('d1000000-0000-0000-0000-000000000001', 'Потребительский кредит', 14.5, 50000.00, 1000000.00, 3, 60),
                                                                                                                        ('d1000000-0000-0000-0000-000000000002', 'Ипотека Новостройка', 8.00, 1000000.00, 15000000.00, 12, 360),
                                                                                                                        ('d1000000-0000-0000-0000-000000000003', 'Микрозайм Наличными', 24.9, 10000.00, 100000.00, 1, 12),
                                                                                                                        ('d1000000-0000-0000-0000-000000000004', 'Автокредит Драйв', 16.5, 300000.00, 5000000.00, 12, 84),
                                                                                                                        ('d1000000-0000-0000-0000-000000000005', 'Семейная Ипотека', 6.0, 500000.00, 12000000.00, 36, 360),
                                                                                                                        ('d1000000-0000-0000-0000-000000000006', 'Образовательный с господдержкой', 3.0, 20000.00, 500000.00, 6, 120),
                                                                                                                        ('d1000000-0000-0000-0000-000000000007', 'Премиум Прайм', 11.2, 2000000.00, 30000000.00, 6, 84);

INSERT INTO loans.loan_applications (id, client_id, product_id, requested_amount, requested_term_months, status_code, created_at, updated_at) VALUES
    ('e1000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'd1000000-0000-0000-0000-000000000001',
     300000.00, 24, 'APPROVED', now() - interval '20 days', now() - interval '18 days');

INSERT INTO loans.loans (id, application_id, account_id, amount, balance_owed, interest_rate, start_date, end_date, status_code, created_at) VALUES
    ('f1000000-0000-0000-0000-000000000001', 'e1000000-0000-0000-0000-000000000001', 'fb226e43-b9ea-49d7-9147-980c3ebc503b',
     300000.00, 280000.00, 14.5, CURRENT_DATE - interval '18 days', CURRENT_DATE + interval '24 months', 'ACTIVE', now() - interval '18 days');

INSERT INTO loans.payment_schedule (id, loan_id, payment_date, total_payment, principal_payment, interest_payment, status, created_at, updated_at) VALUES
                                                                                                                                                       (gen_random_uuid(), 'f1000000-0000-0000-0000-000000000001', CURRENT_DATE - interval '1 month', 14500.00, 10870.00, 3630.00, 'FUTURE', now() - interval '18 days', now() - interval '1 month'),
                                                                                                                                                       (gen_random_uuid(), 'f1000000-0000-0000-0000-000000000001', CURRENT_DATE, 14500.00, 10998.00, 3502.00, 'PAID', now() - interval '18 days', NULL);

-- transactions

INSERT INTO transactions.transaction_types (id, description, type) VALUES
                                                                       (1, 'Выдача кредита', 'LOAN_DISBURSEMENT'),
                                                                       (2, 'Погашение кредита', 'LOAN_REPAYMENT'),
                                                                       (3, 'Внесение денежных средств через кассу', 'DEPOSITING_FUNDS'),
                                                                       (4, 'Погашение процентов', 'INTEREST_PAYMENT'),
                                                                       (5, 'Выплата неустойки', 'PENALTY_REPAYMENT'),
                                                                       (6, 'Получение денежных средств в кассе', 'OBTAINING_FUNDS');

INSERT INTO transactions.transactions (id, credit_id, debit_id, sum, date, transaction_types_id) VALUES
    (gen_random_uuid(), NULL, 'fb226e43-b9ea-49d7-9147-980c3ebc503b', 300000.00, now() - interval '18 days', 1);

INSERT INTO transactions.transactions (id, credit_id, debit_id, sum, date, transaction_types_id) VALUES
    (gen_random_uuid(), 'fb226e43-b9ea-49d7-9147-980c3ebc503b', NULL, 14500.00, now() - interval '1 month', 2);

INSERT INTO transactions.transactions (id, credit_id, debit_id, sum, date, transaction_types_id) VALUES
    (gen_random_uuid(), 'c1000000-0000-0000-0000-000000000001', '241b093f-366e-4f3f-845c-76f604a010f4', 5000.00, now() - interval '3 days', 3);