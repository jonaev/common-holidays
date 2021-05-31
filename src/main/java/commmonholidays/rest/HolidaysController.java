package commmonholidays.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import commmonholidays.services.nager.api.NoCommonHolidaysFoundException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/holiday")
public class HolidaysController {

	private final HolidaysService holidaysService;

	public HolidaysController(HolidaysService holidaysService) {
		this.holidaysService = holidaysService;
	}


	@GetMapping
	public CompletableFuture<ResponseEntity<CommonHolidayResponseAPI>> getCommonHolidays(
			@RequestHeader("1cc") String firstCountryCode, @RequestHeader("2cc") String secondCountryCode,
			@RequestHeader("from-date") String fromDate) {
		CommonHolidayRequestAPI requestAPI = CommonHolidayRequestAPI.of(
				firstCountryCode,
				secondCountryCode,
				LocalDate.parse(fromDate, DateTimeFormatter.ISO_LOCAL_DATE));
		return CompletableFuture.supplyAsync(
				() -> {
					try {
						return new ResponseEntity<>(holidaysService.getCommonHoliday(requestAPI), HttpStatus.OK);
					} catch (ExecutionException | InterruptedException e) {
						log.error(e.getMessage(), e);
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					} catch (NoCommonHolidaysFoundException exc) {
						log.error(exc.getMessage(), exc);
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
				});
	}

}
