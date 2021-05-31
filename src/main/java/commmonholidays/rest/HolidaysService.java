package commmonholidays.rest;

import java.util.concurrent.ExecutionException;

public interface HolidaysService {

	CommonHolidayResponseAPI getCommonHoliday(CommonHolidayRequestAPI requestAPI) throws ExecutionException, InterruptedException;
}
