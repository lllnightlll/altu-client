# altu-client

Android-клиент для [altu-server](https://github.com/lllnightlll/altu-server): E2E-чат store-and-forward. Сервер видит только nonce и ciphertext, ключи остаются на устройстве.

Jetpack Compose, Material3, портрет. Палитра `#070809` / `#ACADAC` / `#EC407A`.

## Что умеет

- Регистрация: tag + аватар, `POST /register` с Ed25519 public key
- Чаты в Room, заметки (`notes`) — чат с самим собой, на сервер не уходят
- Контакт через QR (`userId`, `tag`, `publicKey`)
- Отправка: ChaCha20-Poly1305, подпись `X-Timestamp` + `X-Signature`
- Inbox раз в 5 с: `GET /messages/{userId}` → сохранить → `PATCH /receivedMessages`
- Непрочитанные сбрасываются при открытии чата
- Исходящие сначала в Room, повтор `POST /send` если сеть упала
- Адрес сервера в Settings (переживает удаление аккаунта)
- Delete account чистит Room, аватар, сессию и Ed25519 seed

Незакрытое — в [docs/tasks.md](docs/tasks.md).

## Стек

| | |
| --- | --- |
| minSdk / targetSdk | 24 / 37 |
| UI | Compose, Navigation |
| DB | Room 2.8.4 + KSP |
| Сеть | OkHttp |
| Крипто | BouncyCastle: Ed25519, X25519, HKDF-SHA256, ChaCha20-Poly1305 |
| QR | ZXing encode, CameraX + ML Kit scan |
| Identity | seed в файле, обёртка AES-GCM через Android Keystore |

## Крипто и API

Тело сообщения на сервер: `nonce` + `ciphertext`. Общий секрет — ECDH X25519 (ключ из Ed25519), HKDF info `altu-message-v1`. AAD: `from|to|chat`.

Подпись: `$timestamp\n$rawBody` (для inbox — `GET /messages/{userId}`). Окно timestamp на сервере — 5 минут; повтор исходящих подписывается заново.

| Метод | Путь |
| --- | --- |
| POST | `/register` |
| POST | `/send` |
| GET | `/messages/{userId}` |
| PATCH | `/receivedMessages` |

Контактов на сервере нет: ключ собеседника только из QR. Пока QR не считан, входящее не расшифруется.

## Запуск

Нужен полный JDK со `jlink` (JRE расширения Red Hat Java в Cursor его не содержит). Если Gradle ругается на `jlink.exe does not exist`, укажи JDK в `~/.gradle/gradle.properties`:

```properties
org.gradle.java.home=C:/Program Files/Android/Android Studio/jbr
```

Сервер: порт `8081`.

- Эмулятор: `http://10.0.2.2:8081` (уже default)
- Телефон: `http://<LAN-IP-ПК>:8081` в Settings → Server → Save

Cleartext HTTP разрешён в манифесте.

```bash
./gradlew :app:compileDebugKotlin
```

В IDE — Run на эмулятор или устройство. Камера нужна только вкладке Scan.

## Данные на устройстве

| | |
| --- | --- |
| Room | `altu.db` — users, messages_queue |
| Session | SharedPreferences `altu_session` |
| Server URL | SharedPreferences `altu_server` |
| Identity | `files/altu_ed25519.box` |
| Аватар | `files/avatars/` |

`delivered_at == null` у входящих — непрочитано, у исходящих — ещё не принял сервер.

## Структура

```
app/src/main/java/com/example/altu/
  Auth/          регистрация, правила tag
  api/           OkHttp, подпись, URL
  crypto/        identity, ECDH, AEAD, envelope
  DataBase/      Room, ChatRepository, очередь исходящих
  NewContact/    QR encode/scan
  ChatBar/       список и экран чата
  Settings/
  Profile/       сессия, аватар
```
