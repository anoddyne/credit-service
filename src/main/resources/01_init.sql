-- Дроп схемы дропает все таблицы. Разбивка таблиц на схемы по микросервисам

DROP SCHEMA IF EXISTS clients CASCADE;
DROP SCHEMA IF EXISTS accounts CASCADE;
DROP SCHEMA IF EXISTS loans CASCADE;
DROP SCHEMA IF EXISTS transactions CASCADE;

CREATE SCHEMA clients;
CREATE SCHEMA accounts;
CREATE SCHEMA loans;
CREATE SCHEMA transactions;

-- clients

CREATE TABLE clients.clients (
                                 id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 first_name      VARCHAR(255)        NOT NULL,
                                 last_name       VARCHAR(255)        NOT NULL,
                                 middle_name     VARCHAR(255),
                                 passport_number VARCHAR(255) UNIQUE NOT NULL,
                                 phone           VARCHAR(50)  UNIQUE NOT NULL,
                                 email           VARCHAR(255) UNIQUE,
                                 birth_date      DATE,
                                 address         VARCHAR(255),
                                 created_at      TIMESTAMP           NOT NULL,
                                 updated_at      TIMESTAMP
);

CREATE TABLE clients.client_statuses (
                                         id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         value       VARCHAR(100) UNIQUE NOT NULL,
                                         description VARCHAR(255)
);

CREATE TABLE clients.client_status_history (
                                               id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                               client_id  UUID      NOT NULL REFERENCES clients.clients(id) ON DELETE CASCADE,
                                               status_id  UUID      NOT NULL REFERENCES clients.client_statuses(id),
                                               changed_at TIMESTAMP NOT NULL
);

-- accounts

CREATE TABLE accounts.account_types (
                                        id          UUID PRIMARY KEY,
                                        description TEXT,
                                        name        VARCHAR(255) NOT NULL,
                                        CONSTRAINT account_types_name_check CHECK (
                                            (name)::text = ANY (ARRAY['CURRENT', 'SAVINGS', 'DEPOSIT', 'CREDIT']::text[])
),
    CONSTRAINT account_types_name_key UNIQUE (name)
);

CREATE TABLE accounts.currencies (
                                     id     UUID PRIMARY KEY,
                                     code   VARCHAR(3)   NOT NULL,
                                     name   VARCHAR(255) NOT NULL,
                                     symbol VARCHAR(255),
                                     CONSTRAINT currencies_code_key UNIQUE (code)
);

CREATE TABLE accounts.accounts (
                                   id               UUID PRIMARY KEY,
                                   account_number   VARCHAR(255)   NOT NULL,
                                   account_status   VARCHAR(255),
                                   balance          NUMERIC(19,2)  NOT NULL,
                                   created_at       TIMESTAMP(6),
                                   created_by       VARCHAR(255),
                                   owner_id         UUID           NOT NULL,
                                   owner_type       VARCHAR(255)   NOT NULL,
                                   updated_at       TIMESTAMP(6),
                                   updated_by       VARCHAR(255),
                                   version          BIGINT         NOT NULL,
                                   account_type_id  UUID REFERENCES accounts.account_types(id),
                                   currency_id      UUID REFERENCES accounts.currencies(id),
                                   CONSTRAINT accounts_account_status_check CHECK (
                                       (account_status)::text = ANY (ARRAY['ACTIVE', 'BLOCKED', 'FROZEN', 'CLOSED']::text[])
),
    CONSTRAINT accounts_owner_type_check CHECK (
        (owner_type)::text = ANY (ARRAY['INDIVIDUAL', 'CORPORATE']::text[])
    ),
    CONSTRAINT accounts_account_number_key UNIQUE (account_number),
    CONSTRAINT fk_accounts_owner FOREIGN KEY (owner_id) REFERENCES clients.clients(id)
);

CREATE TABLE accounts.account_status_history (
                                                 id         UUID PRIMARY KEY,
                                                 changed_at TIMESTAMP(6),
                                                 changed_by VARCHAR(255),
                                                 new_status VARCHAR(255),
                                                 old_status VARCHAR(255),
                                                 reason     TEXT,
                                                 account_id UUID REFERENCES accounts.accounts(id),
                                                 CONSTRAINT account_status_history_new_status_check CHECK (
                                                     (new_status)::text = ANY (ARRAY['ACTIVE', 'BLOCKED', 'FROZEN', 'CLOSED']::text[])
),
    CONSTRAINT account_status_history_old_status_check CHECK (
        (old_status)::text = ANY (ARRAY['ACTIVE', 'BLOCKED', 'FROZEN', 'CLOSED']::text[])
    )
);

-- loans

CREATE TABLE loans.loan_statuses (
                                     code        VARCHAR(50) PRIMARY KEY,
                                     description VARCHAR(255) NOT NULL
);

CREATE TABLE loans.loan_application_statuses (
                                                 code        VARCHAR(50) PRIMARY KEY,
                                                 description VARCHAR(255) NOT NULL
);

CREATE TABLE loans.payment_statuses (
                                        code        VARCHAR(50) PRIMARY KEY,
                                        description VARCHAR(255) NOT NULL
);

CREATE TABLE loans.loan_products (
                                     id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     name             VARCHAR(255)   NOT NULL,
                                     interest_rate    NUMERIC(5, 2)  NOT NULL,
                                     min_amount       NUMERIC(15, 2) NOT NULL,
                                     max_amount       NUMERIC(15, 2) NOT NULL,
                                     min_term_months  INT            NOT NULL,
                                     max_term_months  INT            NOT NULL,
                                     is_active        BOOLEAN DEFAULT TRUE,
                                     created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE loans.loan_applications (
                                         id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         client_id              UUID NOT NULL,
                                         product_id             UUID NOT NULL,
                                         requested_amount       NUMERIC(15, 2) NOT NULL,
                                         requested_term_months  INT NOT NULL,
                                         status_code            VARCHAR(50) NOT NULL,
                                         created_at             TIMESTAMP NOT NULL DEFAULT NOW(),
                                         updated_at             TIMESTAMP,
                                         CONSTRAINT fk_applications_product FOREIGN KEY (product_id) REFERENCES loans.loan_products(id),
                                         CONSTRAINT fk_application_status FOREIGN KEY (status_code) REFERENCES loans.loan_application_statuses(code),
                                         CONSTRAINT fk_applications_client FOREIGN KEY (client_id) REFERENCES clients.clients(id)
);

CREATE TABLE loans.loans (
                             id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             application_id  UUID UNIQUE NOT NULL,
                             account_id      UUID NOT NULL,
                             amount          NUMERIC(15, 2) NOT NULL,
                             balance_owed    NUMERIC(15, 2) NOT NULL,
                             interest_rate   NUMERIC(5, 2) NOT NULL,
                             start_date      DATE NOT NULL,
                             end_date        DATE NOT NULL,
                             status_code     VARCHAR(50) NOT NULL,
                             created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                             CONSTRAINT fk_loans_applications FOREIGN KEY (application_id) REFERENCES loans.loan_applications(id),
                             CONSTRAINT fk_loans_status FOREIGN KEY (status_code) REFERENCES loans.loan_statuses(code),
                             CONSTRAINT fk_loans_account FOREIGN KEY (account_id) REFERENCES accounts.accounts(id)
);

CREATE TABLE loans.payment_schedule (
                                        id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        loan_id             UUID NOT NULL,
                                        payment_date        DATE NOT NULL,
                                        total_payment       NUMERIC(15, 2) NOT NULL,
                                        principal_payment   NUMERIC(15, 2) NOT NULL,
                                        interest_payment    NUMERIC(15, 2) NOT NULL,
                                        status              VARCHAR(50) NOT NULL,
                                        created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
                                        updated_at          TIMESTAMP,
                                        CONSTRAINT fk_schedule_loan FOREIGN KEY (loan_id) REFERENCES loans.loans(id),
                                        CONSTRAINT fk_payment_status FOREIGN KEY (status) REFERENCES loans.payment_statuses(code)
);

-- transactions

CREATE TABLE transactions.transaction_types (
                                                id          BIGINT GENERATED BY DEFAULT AS IDENTITY (
                                                    INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1
                ),
                                                description VARCHAR(255),
                                                type        VARCHAR(255),
                                                CONSTRAINT transaction_types_pkey PRIMARY KEY (id),
                                                CONSTRAINT transaction_types_type_check CHECK (
                                                    type::text = ANY (ARRAY[
                                                    'LOAN_DISBURSEMENT', 'LOAN_REPAYMENT', 'DEPOSITING_FUNDS',
                                                    'INTEREST_PAYMENT', 'PENALTY_REPAYMENT', 'OBTAINING_FUNDS'
                                                    ]::text[])
)
    );

CREATE TABLE transactions.transactions (
                                           id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           credit_id             UUID,
                                           debit_id              UUID,
                                           sum                   NUMERIC(38,2),
                                           date                  TIMESTAMP(6),
                                           transaction_types_id  BIGINT,
                                           CONSTRAINT fk_transactions_type FOREIGN KEY (transaction_types_id)
                                               REFERENCES transactions.transaction_types(id) MATCH SIMPLE
                                               ON UPDATE NO ACTION ON DELETE NO ACTION,
                                           CONSTRAINT fk_transactions_credit FOREIGN KEY (credit_id) REFERENCES accounts.accounts(id),
                                           CONSTRAINT fk_transactions_debit FOREIGN KEY (debit_id) REFERENCES accounts.accounts(id)
);