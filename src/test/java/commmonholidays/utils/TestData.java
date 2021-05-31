package commmonholidays.utils;

import java.time.LocalDate;

import commmonholidays.services.nager.api.HolidayAPI;

public class TestData {
	public static final String PL = "pl";
	public static final String DK = "dk";
	public static final String DE = "de";

	public static HolidayAPI[] listOfDanishHolidaysForYear(int year) {
		return new HolidayAPI[]{
				HolidayAPI.of(LocalDate.of(year, 1, 1), "1dk", "1dk", DK),
				HolidayAPI.of(LocalDate.of(year, 1, 2), "2dk", "2dk", DK),
				HolidayAPI.of(LocalDate.of(year, 1, 4), "3dk", "3dk", DK)};
	}

	public static HolidayAPI[] listOfPolishHolidaysForYear(int year) {
		return new HolidayAPI[]{
				HolidayAPI.of(LocalDate.of(year, 1, 1), "1pl", "1pl", PL),
				HolidayAPI.of(LocalDate.of(year, 1, 3), "2pl", "2pl", PL),
				HolidayAPI.of(LocalDate.of(year, 1, 4), "3pl", "3pl", PL)};
	}

	public static HolidayAPI[] listOfGermanHolidaysForYear(int year) {
		return new HolidayAPI[]{
				HolidayAPI.of(LocalDate.of(year, 3, 1), "1de", "1de", DE),
				HolidayAPI.of(LocalDate.of(year, 5, 3), "2de", "2de", DE),
				HolidayAPI.of(LocalDate.of(year, 11, 4), "3de", "3de", DE)};
	}
}
