package commmonholidays.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import commmonholidays.rest.CommonHolidayRequestAPI;
import commmonholidays.rest.CommonHolidayResponseAPI;
import commmonholidays.rest.HolidaysService;
import commmonholidays.services.nager.HolidayCalendar;
import commmonholidays.services.nager.api.HolidayAPI;
import commmonholidays.services.nager.api.NoCommonHolidaysFoundException;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class HolidaysServiceImpl implements HolidaysService {

	private final OpenAPIService<HolidayAPI> openAPIService;

	public HolidaysServiceImpl(OpenAPIService openAPIService) {
		this.openAPIService = openAPIService;
	}

	@Override
	public CommonHolidayResponseAPI getCommonHoliday(CommonHolidayRequestAPI requestAPI) throws ExecutionException, InterruptedException {
		try {
			return searchCommonHolidaysInYear(requestAPI.getFromDate().getYear(), requestAPI);
		} catch (NoCommonHolidaysFoundException e) {
			log.warn(String.format("Didn't found common holidays in the year %d, searching in the next one...",
					requestAPI.getFromDate().getYear()));
			return checkHolidaysFromNextYear(requestAPI);
		}
	}

	private CommonHolidayResponseAPI checkHolidaysFromNextYear(CommonHolidayRequestAPI requestAPI) throws ExecutionException, InterruptedException {
		try {
			return searchCommonHolidaysInYear(requestAPI.getFromDate().getYear() + 1, requestAPI);
		} catch (NoCommonHolidaysFoundException exc) {
			log.error(String.format("No common holidays found for countries %s and %s",
					requestAPI.getFirstCountryCode(),
					requestAPI.getSecondCountryCode()));
			throw exc;
		}
	}

	private CommonHolidayResponseAPI searchCommonHolidaysInYear(Integer year, CommonHolidayRequestAPI requestAPI) throws ExecutionException, InterruptedException {
		CompletableFuture<HolidayCalendar> firstCountryHolidays = CompletableFuture.supplyAsync(
				() -> new HolidayCalendar(openAPIService.getHolidays(year, requestAPI.getFirstCountryCode())));

		CompletableFuture<HolidayCalendar> secondCountryHolidays = CompletableFuture.supplyAsync(
				() -> new HolidayCalendar(openAPIService.getHolidays(year, requestAPI.getSecondCountryCode())));
		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(firstCountryHolidays, secondCountryHolidays);
		combinedFuture.get();

		if (firstCountryHolidays.isDone() && secondCountryHolidays.isDone()) {
			HolidayCalendar combinedCalendar = firstCountryHolidays.get()
					.merge(secondCountryHolidays.get())
					.getAllCommonHolidaysAfterDate(requestAPI.getFromDate());
			HolidayAPI[] holidays = combinedCalendar.getClosestDateAndHolidays();
			return CommonHolidayResponseAPI.of(holidays[0].getDate().toString(), holidays[0].getLocalName(), holidays[1].getLocalName());
		}
		return null;
	}
}
