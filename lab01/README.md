# Модуль лабораторной работы 1

Условие находится в [`assignments/lab01-java-basics.md`](../assignments/lab01-java-basics.md).

В стартовом проекте уже есть:

- класс `CourseToolkit` с одним реализованным методом `isEven`;
- два проходящих JUnit-теста как образец;
- готовая конфигурация Maven.

Запуск из корня репозитория:

```bash
./mvnw -pl lab01 test
```

Для Windows PowerShell:

```powershell
.\mvnw.cmd -pl lab01 test
```

Методы `isPrime`, `isPalindrome` и `average` и тесты к ним студент добавляет самостоятельно в ветке `lab/01-basics`.

В стартовом состоянии намеренно падающих тестов нет. До начала изменений команда должна завершаться сообщением `BUILD SUCCESS`.
