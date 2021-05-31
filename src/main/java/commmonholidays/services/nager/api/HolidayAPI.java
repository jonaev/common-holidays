package commmonholidays.services.nager.api;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * {
 *         "date": "2017-12-25",
 *         "localName": "Weihnachten",
 *         "name": "Christmas Day",
 *         "countryCode": "AT",
 *         "fixed": true,
 *         "global": true,
 *         "counties": null,
 *         "launchYear": null,
 *         "types": [
 *             "Public"
 *         ]
 *     }
 */
@AllArgsConstructor(staticName = "of")
@Data
public class HolidayAPI implements Serializable {
	private LocalDate date;
	private String localName;
	private String name;
	private String countryCode;
//	private Boolean fixed;
//	private Integer launchYear;
//	private String[] types;
}
