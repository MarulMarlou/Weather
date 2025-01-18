WeatherService
Проект WeatherService предназначен для получения прогноза погоды от API Яндекс.Погода. Он позволяет пользователю вводить координаты места и получать текущую температуру, а также рассчитывать среднюю температуру за определённый период.
Функциональность
GET-запрос к API: Отправляет запрос к API Яндекс.Погода с указанными координатами и лимитом дней.
Вывод JSON: Печатает весь ответ от API в формате JSON.
Текущая температура: Извлекает и выводит текущую температуру из раздела fact.
Средняя температура: Рассчитывает и выводит среднюю температуру за указанный период.
Требования
Java 21: Проект написан на Java 21.
Gson: Использует библиотеку Gson для парсинга JSON.
API-ключ Яндекс.Погода: Требуется валидный API-ключ для работы с сервисом Яндекс.Погода.
Установка
Клонирование репозитория:
bash
git clone https://github.com/your-repo/WeatherService.git
Добавление зависимости Gson:
Если вы используете Maven, добавьте следующую зависимость в файл pom.xml:
xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
Если вы используете Gradle, добавьте следующую строку в файл build.gradle:
groovy
implementation 'com.google.code.gson:gson:2.10.1'
Настройка API-ключа:
Замените API_KEY в файле WeatherService.java на ваш валидный API-ключ Яндекс.Погода.
Использование
Запуск приложения:
Запустите класс WeatherService.
Введите широту, долготу и лимит дней для прогноза.
Вывод данных:
Программа выведет весь ответ от API и рассчитанную среднюю температуру.
Примечания
Убедитесь, что координаты вводятся в формате десятичных чисел с точкой (.).
Проверьте диапазон координат: широта от -90 до 90, долгота от -180 до 180.
