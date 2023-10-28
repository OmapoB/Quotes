![image](https://github.com/OmapoB/Quotes/assets/61049551/bc24ad82-6bcb-4d07-a294-31eb092171cd)![image](https://github.com/OmapoB/Quotes/assets/61049551/6dd4d034-fd26-4aa0-a440-c84101ce667c)Композ ап сначала
Потом инит запрос кидаешь на localhost:8080/init0 и localhost:8080/init1
подождать минут 10 если есть время, если нет то хотябы минуту.
Дальше localhost:8000/positions
Поиск только по тикеру(остальные на бэке есть на фронте впадлу делать было)
Нажать на позицию если выйдет позиция(без гуи просто json вернет)
Все пути бэка:
localhost:8080/positions/{id} получение позиции по uid
localhost:8080/positions/sectors/{sector} получение по секторам
localhost:8080/positions/types/{type}?p=n&el=m получение по типу(только акции пиши, потому что облигации и прочие инструменты не подтягиваются потому, что долго, но все работает я проверял), 
где n номер страницы, m количество элементов на странице

localhost:8080/positions/currencies/{crurency} получение по типу валюты
localhost:8080/positions/tickers/{ticker} получение по тикеру
localhost:8080/positions/names/{name} получение по названию
localhost:8080/positions/nominal/{price} получение по цене
localhost:8080/positions/get_all?p=n&els=m получение всех позиций, где n номер страницы, m количество элементов на странице

localhost:8080/history?p=n&els=m получение истории по всем инструментам, где n номер страницы, m количество элементов на странице
localhost:8080/history/diff в теле прередать LocalDate start, end, String uid чтобы получить процент прибыли
localhost:8080/histroy/price_histrory_between в теле передать uid, start, stop, чтобы получить историю в промежутке от start до stop
