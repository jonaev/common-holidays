package commmonholidays.services.nager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import commmonholidays.services.OpenAPIService;
import commmonholidays.services.nager.api.HolidayAPI;
import lombok.extern.log4j.Log4j2;

/**
 * Nager.at api client
 */
@Service
@Log4j2
public class NagerService implements OpenAPIService<HolidayAPI> {

	private final RestTemplate restTemplate;

	@Value("${api.nager.endpoint}")
	private String apiEnpoint;

	public NagerService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public HolidayAPI[] getHolidays(Integer year, String countryCode) {
			String url = String.format("%s/%d/%s", apiEnpoint, year, countryCode);
			log.info(url);
			return this.restTemplate.getForObject(url, HolidayAPI[].class);
	}
}
