# SpringBootLab
 
REST API на Spring Boot для управления пользователями и их книгами. Данные хранятся в памяти приложения
(без базы данных).
 
## Что умеет
 
- Создавать пользователя вместе с его книгами одним запросом.
- Получать пользователя (идентификаторы его книг), обновлять и удалять пользователя вместе с книгами.
- Проверять входные данные (имя и возраст пользователя, название, автор и число страниц книги).
- Возвращать понятное сообщение об ошибке, если пользователь или книга не найдены.
- Логировать запросы и передавать идентификатор запроса (заголовок `rqid`) в лог через MDC.
- Публиковать документацию API в Swagger UI.


## Стек
 
| Компонент | Версия / назначение |
|---|---|
| Java | 17 |
| Spring Boot | 2.7.3 (`spring-boot-starter-web`, `spring-boot-starter-actuator`) |
| MapStruct | преобразование между DTO и сущностями |
| Lombok | сокращение шаблонного кода |
| springdoc-openapi | Swagger UI |
| Maven | сборка (есть `mvnw`) |
 
## Запуск
 
Нужны JDK 17 и Maven (либо обёртка `mvnw` из репозитория).
 
```bash
git clone https://github.com/Paoak/SpringBootLab.git
cd SpringBootLab
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```
 
Приложение стартует на порту `8091` с контекстным путём `/app`:
 
- API: `http://localhost:8091/app/api/v1/user/...`
- Swagger UI: `http://localhost:8091/app/swagger-ui.html`
- Проверка состояния: `http://localhost:8091/app/actuator/health`
## API
 
Базовый путь: `/app/api/v1/user`. Формат обмена: JSON.
 
| Метод | Путь | Что делает |
|---|---|---|
| POST | `/create` | создать пользователя с книгами |
| PUT | `/update/{userId}` | обновить пользователя; если передан список книг, прежние книги заменяются новыми |
| GET | `/get/{userId}` | получить пользователя и идентификаторы его книг |
| DELETE | `/delete/{userId}` | удалить пользователя и все его книги |
 
Для `POST /create` обязателен заголовок `rqid` (идентификатор запроса, он попадает в лог).
 
### Пример: создание
 
```bash
curl -X POST http://localhost:8091/app/api/v1/user/create \
  -H "Content-Type: application/json" \
  -H "rqid: test-request-1" \
  -d '{
        "userRequest": { "fullName": "Иван Петров", "title": "reader", "age": 30 },
        "bookRequests": [
          { "title": "Идиот", "author": "Ф. М. Достоевский", "pageCount": 640 },
          { "title": "Мастер и Маргарита", "author": "М. А. Булгаков", "pageCount": 480 }
        ]
      }'
```
 
Ответ:
 
```json
{ "userId": 1, "booksIdList": [1, 2] }
```
 
### Другие запросы
 
```bash
# получить
curl http://localhost:8091/app/api/v1/user/get/1
 
# обновить (в теле можно передать только изменяемые поля пользователя)
curl -X PUT http://localhost:8091/app/api/v1/user/update/1 \
  -H "Content-Type: application/json" \
  -d '{ "userRequest": { "age": 31 } }'
 
# удалить
curl -X DELETE http://localhost:8091/app/api/v1/user/delete/1
```
 
### Ошибки
 
Если пользователь или книга не найдены, возвращается ответ с сообщением:
 
```json
{ "errorMessage": "User with ID = 100 not found" }
```
 
## Структура проекта
 
```
src/main/java/com/edu/ulab/app/
├── web/            # контроллер, запросы/ответы, фильтр, обработчик ошибок, Swagger-константы
├── facade/         # UserDataFacade: сценарии «пользователь + книги»
├── service/        # бизнес-логика (UserService, BookService и реализации)
├── storage/        # хранилища в памяти (HashMap + счётчик идентификаторов)
├── validation/     # проверка данных пользователя и книги
├── mapper/         # MapStruct-мапперы (DTO <-> Entity, Request -> DTO)
├── dto/, entity/   # модели данных
├── exception/      # NotFoundException, NotValidException
└── config/         # конфигурация Swagger
```
 
Запрос проходит слои так: `UserController` → `UserDataFacade` → `UserService` / `BookService` → хранилище.
 
## Ограничения и планы
 
- Данные хранятся в `HashMap` и пропадают при перезапуске; хранилище не рассчитано на параллельные запросы.
- Ошибка «не найдено» сейчас возвращается со статусом 400, а не 404. Для `NotValidException` отдельного обработчика
  пока нет, поэтому при невалидных данных Spring вернёт стандартную ошибку 500.
- Автотестов в проекте пока нет. Планирую добавить тесты на JUnit 5 и `MockMvc` (создание, получение несуществующего
  пользователя, невалидный запрос).
- Следующий шаг: подключить PostgreSQL через Spring Data JPA.
