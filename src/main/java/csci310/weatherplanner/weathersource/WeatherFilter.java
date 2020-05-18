package csci310.weatherplanner.weathersource;

public class WeatherFilter {
	private final String loc;
	private final int tempMax;
	private final int tempMin;
	private PrecipType precipType;
	private final int maxResults;
	private final int radius;
	
	public WeatherFilter(String loc, int tempMax, int tempMin, PrecipType precipType, int maxResults, int radius) {
		this.loc = loc;
		this.tempMax = tempMax; 
		this.tempMin = tempMin;
		this.precipType = precipType;
		this.maxResults = maxResults;
		this.radius = radius;
	}
	
	public PrecipType getPrecipType() {
		return precipType;
	}
	
	public void setPrecipType(PrecipType precipType) {
		this.precipType = precipType;
	}
	
	public String getLocation() {
		return loc;
	}
	
	public int getTempMax() {
		return tempMax;
	}
	
	public int getTempMin() {
		return tempMin;
	}
	
	public int getMaxResults() {
		return maxResults;
	}
	
	public int getRadius() {
		return radius;
	}
}
