package online.mayak.energy.config;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class OcppConfig {
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
	public static final int PING_INTERVAL = 60;
	public static final TimeUnit PING_INTERVAL_TIME_UNIT = TimeUnit.SECONDS;
}
