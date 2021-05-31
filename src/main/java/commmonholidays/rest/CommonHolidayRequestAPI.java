package commmonholidays.rest;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class CommonHolidayRequestAPI {
	private final String firstCountryCode;
	private final String secondCountryCode;
	private final LocalDate fromDate;
}
