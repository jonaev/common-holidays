package commmonholidays.services.nager;

import org.apache.commons.lang3.ArrayUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import commmonholidays.services.nager.api.HolidayAPI;
import commmonholidays.services.nager.api.NoCommonHolidaysFoundException;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;

@Data
public class HolidayCalendar {

	@NonNull
	@Delegate
	private final Map<LocalDate, HolidayAPI[]> calendar;

	public HolidayCalendar(Map<LocalDate, HolidayAPI[]> calendar) {
		this.calendar = calendar;
	}

	public HolidayCalendar(HolidayAPI[] holidays) {
		calendar = new HashMap<>();
		Arrays.stream(holidays).forEach(holiday -> calendar.put(holiday.getDate(), new HolidayAPI[] {holiday})
		);
	}

	public HolidayCalendar merge(HolidayCalendar secondCalendar) {
		secondCalendar.forEach((k, v) -> this.calendar.merge(k, v, ArrayUtils::addAll));
		return this;
	}

	public HolidayCalendar getAllCommonHolidaysAfterDate(LocalDate fromDate) {
		return new HolidayCalendar(entrySet().stream()
				.filter(map -> containsExactlyTwoHolidaysFromEachCountry(map.getValue()))
				.filter(map -> map.getKey().isAfter(fromDate.minusDays(1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	private boolean containsExactlyTwoHolidaysFromEachCountry(HolidayAPI[] holidays) {
		return holidays.length == 2 && (!holidays[0].getCountryCode().equals(holidays[1].getCountryCode()));
	}

	private LocalDate getClosestDate() {
		return this.calendar.keySet()
				.stream().min(LocalDate::compareTo).orElseThrow(NoCommonHolidaysFoundException::new);
	}

	public HolidayAPI[] getClosestDateAndHolidays() {
		return this.calendar.get(getClosestDate());
	}
}
