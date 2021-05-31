package commmonholidays.rest;

import lombok.Data;

@Data(staticConstructor = "of")
public class CommonHolidayResponseAPI {
	private final String date;
	private final String name1;
	private final String name2;
}
