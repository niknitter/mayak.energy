package online.mayak.energy.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SystemUtils {

	private static ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

	/**
	 * Текущее системное время
	 * @return LocalDateTime
	 */
	public static LocalDateTime currentLocalDateTime() {
		return LocalDateTime.now();
	}

	/**
	 * Текущее время UTC
	 * @return Instant
	 */
	public static Instant currentInstant() {
		return Instant.now();
	}

	/**
	 * Текущее время UTC с данными о часовом поясе
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime currentZonedDateTimeUTC() {
		return ZonedDateTime.ofInstant(currentInstant(), ZONE_ID_UTC);
	}

	public static ZonedDateTime localToZonedUtcDateTime(LocalDateTime localDateTime) {
		return ZonedDateTime.of(localDateTime, ZONE_ID_UTC);
	}

}
