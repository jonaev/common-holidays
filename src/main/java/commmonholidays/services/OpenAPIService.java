package commmonholidays.services;

public interface OpenAPIService<T>  {
	T[] getHolidays(Integer year, String countryCode);

}
