package commmonholidays.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import commmonholidays.rest.CommonHolidayRequestAPI;
import commmonholidays.rest.CommonHolidayResponseAPI;
import commmonholidays.rest.HolidaysService;
import commmonholidays.services.nager.api.HolidayAPI;
import commmonholidays.services.nager.api.NoCommonHolidaysFoundException;

import static commmonholidays.utils.TestData.DE;
import static commmonholidays.utils.TestData.DK;
import static commmonholidays.utils.TestData.PL;
import static commmonholidays.utils.TestData.listOfDanishHolidaysForYear;
import static commmonholidays.utils.TestData.listOfGermanHolidaysForYear;
import static commmonholidays.utils.TestData.listOfPolishHolidaysForYear;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class HolidaysServiceTest {

	private LocalDate date;

	@Mock
	private OpenAPIService<HolidayAPI> openAPIService;

	private HolidaysService holidaysService;

	@BeforeEach
	void init() {
		holidaysService = new HolidaysServiceImpl(openAPIService);
	}

	@Test
	void testShouldReturnClosestCommonHolidayIncludingToday() throws ExecutionException, InterruptedException {
		int year = 2016;
		date = LocalDate.of(year, 1, 1);
		Mockito.when(openAPIService.getHolidays(any(Integer.class), eq(PL))).thenReturn(listOfPolishHolidaysForYear(year));
		Mockito.when(openAPIService.getHolidays(any(Integer.class), eq(DK))).thenReturn(listOfDanishHolidaysForYear(year));
		CommonHolidayRequestAPI request = CommonHolidayRequestAPI.of(PL, DK, date);
		CommonHolidayResponseAPI response = holidaysService.getCommonHoliday(request);
		assertAll(
				() -> assertEquals("2016-01-01", response.getDate()),
				() -> assertThat(response.getName1(), anyOf(equalTo("1dk"), equalTo("1pl"))),
				() -> assertThat(response.getName2(), anyOf(equalTo("1dk"), equalTo("1pl")))
		);
	}

	@Test
	void testShouldReturnClosestCommonHoliday() throws ExecutionException, InterruptedException {
		int year = 2016;
		date = LocalDate.of(year, 1, 2);
		Mockito.when(openAPIService.getHolidays(any(Integer.class), eq(PL))).thenReturn(listOfPolishHolidaysForYear(year));
		Mockito.when(openAPIService.getHolidays(any(Integer.class), eq(DK))).thenReturn(listOfDanishHolidaysForYear(year));
		CommonHolidayRequestAPI request = CommonHolidayRequestAPI.of(PL, DK, date);
		CommonHolidayResponseAPI response = holidaysService.getCommonHoliday(request);
		assertAll(
				() -> assertEquals("2016-01-04", response.getDate()),
				() -> assertThat(response.getName1(), anyOf(equalTo("3dk"), equalTo("3pl"))),
				() -> assertThat(response.getName2(), anyOf(equalTo("3dk"), equalTo("3pl")))
		);
	}

	@DisplayName("When there are no common holidays in the current year we should search them in the next one")
	@Test
	void testCommonHolidaysForNextYear() throws ExecutionException, InterruptedException {
		date = LocalDate.of(2016, 4, 2);
		Mockito.when(openAPIService.getHolidays(eq(2016), eq(PL))).thenReturn(listOfPolishHolidaysForYear(2016));
		Mockito.when(openAPIService.getHolidays(eq(2017), eq(PL))).thenReturn(listOfPolishHolidaysForYear(2017));
		Mockito.when(openAPIService.getHolidays(eq(2016), eq(DK))).thenReturn(listOfDanishHolidaysForYear(2016));
		Mockito.when(openAPIService.getHolidays(eq(2017), eq(DK))).thenReturn(listOfDanishHolidaysForYear(2017));
		CommonHolidayRequestAPI request = CommonHolidayRequestAPI.of(PL, DK, date);
		CommonHolidayResponseAPI response = holidaysService.getCommonHoliday(request);
		assertAll(
				() -> assertEquals("2017-01-01", response.getDate()),
				() -> assertThat(response.getName1(), anyOf(equalTo("1dk"), equalTo("1pl"))),
				() -> assertThat(response.getName2(), anyOf(equalTo("1dk"), equalTo("1pl")))
		);
	}

	@DisplayName("When there are no common holidays in the current year we should search them in the next one")
	@Test
	void testShouldThrowException() {
		date = LocalDate.of(2016, 4, 2);
		Mockito.when(openAPIService.getHolidays(eq(2016), eq(PL))).thenReturn(listOfPolishHolidaysForYear(2016));
		Mockito.when(openAPIService.getHolidays(eq(2017), eq(PL))).thenReturn(listOfPolishHolidaysForYear(2017));
		Mockito.when(openAPIService.getHolidays(eq(2016), eq(DE))).thenReturn(listOfGermanHolidaysForYear(2016));
		Mockito.when(openAPIService.getHolidays(eq(2017), eq(DE))).thenReturn(listOfGermanHolidaysForYear(2017));
		CommonHolidayRequestAPI request = CommonHolidayRequestAPI.of(PL, DE, date);
		assertThrows(NoCommonHolidaysFoundException.class, () -> holidaysService.getCommonHoliday(request));
	}


}
