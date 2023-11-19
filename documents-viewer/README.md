## Описание

Фул стек приложение - справочник документов

## Структура

- `backend` - Бэкенд на java.
- `ui` - Фронтенд на react + redux.

## Подготовка

Установите:

- [node](https://nodejs.org) - front
- [openjdk](https://openjdk.java.net) 15 - java бэк
- docker-compose
- docker

## Сборка

### Сборка фронта

```
./gradlew ui:npm_run_build
```

### Сборка бэка

```
./gradlew backend:bootJar
```

### Запуск контейнеров
```
docker-compose up
```

## Использование

- Перейдите по адресу: http://localhost:3006/#/
- Подключитесь к kafka по адресу `localhost:19092`.
- После отправки документа на обработку, прочитайте из kafka сообщение из группы `documents-for-processing`.
- Скопируйте тело сообщения, измените в нем значение `staus.code` на `ACCEPTED`/`REJECTED`,
  и значение `status.name` на `Принято`/`Отклонено` соответственно.
- Отправьте новое сообщение с этим телом в группу `processed-documents`.
- Обновите страницу в браузере.
