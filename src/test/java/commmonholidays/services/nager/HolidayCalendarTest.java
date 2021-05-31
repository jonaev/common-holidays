package commmonholidays.services.nager;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static commmonholidays.utils.TestData.listOfDanishHolidaysForYear;
import static commmonholidays.utils.TestData.listOfPolishHolidaysForYear;
import static org.junit.jupiter.api.Assertions.*;

public class HolidayCalendarTest {

	@Test
	void testCalendarConstructor() {
		int year = 2016;
		HolidayCalendar polishCalendar = new HolidayCalendar(listOfPolishHolidaysForYear(year));
		LocalDate date = LocalDate.of(year, 1, 1);
		assertAll(
				() -> assertEquals(3, polishCalendar.size()),
				() -> assertTrue(polishCalendar.containsKey(date)),
				() -> assertEquals(date, polishCalendar.get(date)[0].getDate())
		);
	}

	@Test
	void testCalendarsMerge() {
		int year = 2016;
		HolidayCalendar polishCalendar = new HolidayCalendar(listOfPolishHolidaysForYear(year));
		HolidayCalendar secondCalendar = new HolidayCalendar(listOfDanishHolidaysForYear(year));
		HolidayCalendar merged = polishCalendar.merge(secondCalendar);
		LocalDate date = LocalDate.of(year, 1, 1);
		assertAll(
				() -> assertEquals(4, merged.size()),
				() -> assertEquals(2, merged.get(date).length)
		);
	}
}
