package commmonholidays.services.nager.api;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class HolidaysResponseAPI implements Serializable {
	private List<HolidayAPI> holidays;
}
