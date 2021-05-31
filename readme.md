# Holiday Information service
Goal of application is to try to find the closest common holidays that happen in 2 particular countries. 
Application exposes 1 endpoint for finding those holidays with both country codes and start date accepted as a header params. Sample response:
```json
{
    "date": "2021-11-11",
    "name1": "Narodowe Święto Niepodległości",
    "name2": "Veterans Day"
}
```
Application was written with spring-boot 2.5. It fetches list of coutry holidays from nager.at open API https://date.nager.at/api/. 
Endpoint to it is provided in properties file and it should be fairly easy to abstract connection and introduce another open API service, as most of classes used in the connection contain generics.  

## How to run
- mvn clean install
- mvn spring-boot:run
- or java -jar (path to jar file)
- simple response: curl http://localhost:8080/holiday -H "1cc: PL" -H "2cc: US" -H "from-date: 2021-05-31"

## TODO
To implement some edge cases, like situation when I can get multiple holidays from one country. In such case what should be the input?
Currently in HolidayCalendar class I try to filter out such potential cases and make sure common holidays means single holidays for each country.

