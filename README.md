# Credit Service

Микросервис кредитования для учебного банковского проекта. Сервис управляет полным жизненным циклом кредита: каталог кредитных продуктов → заявка на кредит → выдача кредита с аннуитетным графиком платежей → оплата взносов и контроль просрочки.

## Возможности

- **Каталог кредитных продуктов** — просмотр условий продуктов (ставка, минимальная/максимальная сумма и срок)
- **Заявки на кредит** — подача заявки с бизнес-валидацией суммы и срока по условиям продукта, просмотр заявок клиента
- **Выдача кредита** — по одобренной заявке создаётся кредит со статусом `ACTIVE` и автоматически рассчитывается аннуитетный график платежей
- **Оплата взносов** — погашение конкретного платежа из графика; при полном погашении долга кредит закрывается автоматически
- **Контроль просрочки** — ежедневный планировщик отмечает просроченные платежи и переводит кредиты в статус `OVERDUE`
- **OpenAPI документация** — Swagger UI со всеми эндпоинтами

## Технологии

| Компонент | Технология |
|---|---|
| Язык | Java 21 |
| Фреймворк | Spring Boot 4.1.0 (Web MVC, Data JPA, Scheduling) |
| База данных | PostgreSQL 17 |
| Документация | SpringDoc OpenAPI 3.0 (Swagger UI) |
| Маппинг DTO | MapStruct 1.6.3 |
| Бойлерплейт | Lombok |
| Валидация | Jakarta Validation |
| Сборка | Maven |

## Модель предметной области

Сервис работает со схемой `loans` базы данных `bank_db` (общей для всех микросервисов банка):

- **LoanProduct** — кредитный продукт: ставка, лимиты суммы и срока
- **LoanApplication** — заявка на кредит (`NEW` → `IN_PROGRESS` → `APPROVED` / `REJECTED`)
- **Loan** — выданный кредит (`ACTIVE` / `OVERDUE` / `CLOSED` / `RESTRUCTURED` / `CHARGE_OFF`)
- **PaymentSchedule** — строка аннуитетного графика (`FUTURE` / `PAID` / `PARTIALLY_PAID` / `OVERDUE`)

Схема БД создаётся SQL-скриптами (`01_init.sql`, `02_init_data.sql` с тестовыми данными) при первом старте контейнера PostgreSQL; Hibernate работает в режиме `ddl-auto=validate`.

## REST API

Базовый путь: `/api/v1`

### Кредитные продукты

| Метод | Эндпоинт | Описание |
|---|---|---|
| `GET` | `/loan_products?isActive=true` | Список продуктов (по умолчанию только активные) |
| `GET` | `/loan_products/{id}` | Условия конкретного продукта |

### Заявки на кредит

| Метод | Эндпоинт | Описание |
|---|---|---|
| `POST` | `/loan_applications` | Подать заявку (валидация суммы/срока по продукту) |
| `GET` | `/loan_applications/{id}` | Детали заявки |
| `GET` | `/loan_applications/client/{clientId}` | Все заявки клиента |

### Кредиты

| Метод | Эндпоинт | Описание |
|---|---|---|
| `POST` | `/loans/issue` | Выдать кредит по одобренной заявке и сформировать график |
| `GET` | `/loans/{id}` | Детали кредита с графиком платежей |
| `POST` | `/loans/pay_installment/{installmentId}` | Оплатить взнос по графику |

Полная интерактивная документация: `http://localhost:8083/swagger-ui/index.html`, OpenAPI JSON — `/v3/api-docs`.

## Запуск

### Предварительные требования

- JDK 21
- Docker (для PostgreSQL)
- Maven (или `./mvnw`)

### Переменные окружения

Создайте файл `.env` в корне проекта:

```env
PG_USERNAME=db_admin
PG_PASS=your_password
PG_URL=jdbc:postgresql://localhost:5432/bank_db?currentSchema=loans
```

### 1. Запустить базу данных

```bash
docker compose up -d
```

Контейнер PostgreSQL поднимается на порту `5432`, при первом старте выполняются init-скрипты из `src/main/resources` (создание схем и тестовые данные).

### 2. Запустить приложение

```bash
./mvnw spring-boot:run
```

Сервис доступен на `http://localhost:8083`.

### Сборка и тесты

```bash
./mvnw clean package   # сборка JAR
```

### Docker-образ приложения

В репозитории есть многоэтапный `Dockerfile` (сборка Maven → JRE 21):

```bash
docker build -t credit-service .
docker run -p 8083:8083 --env-file .env credit-service
```

## Структура проекта

```
src/main/java/ru/neoflex/practice/credit_service/
├── controllers/     # REST-контроллеры (products, applications, loans)
├── services/        # Бизнес-логика + LoanSchedulerService (планировщик просрочки)
├── repositories/    # Spring Data JPA репозитории
├── models/          # JPA-сущности и enum-статусы
├── dto/             # Объекты передачи данных
├── mappers/         # MapStruct-мапперы сущность ↔ DTO
├── validators/      # Бизнес-валидация заявок
└── exceptions/      # Обработчик ошибок и кастомные исключения
```
