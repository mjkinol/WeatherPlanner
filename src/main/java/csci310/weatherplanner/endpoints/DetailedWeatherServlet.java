package csci310.weatherplanner.endpoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.base.Enums;
import com.google.gson.Gson;

import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.weathersource.APIRequester;
import csci310.weatherplanner.weathersource.AerisAdvancedWeatherSource;
import csci310.weatherplanner.weathersource.AerisSimpleForecastSource;
import csci310.weatherplanner.weathersource.DailyForecast;
import csci310.weatherplanner.weathersource.Date;
import csci310.weatherplanner.weathersource.DetailedForecast;
import csci310.weatherplanner.weathersource.FavoritesManager;
import csci310.weatherplanner.weathersource.GooglePlacesAPI;
import csci310.weatherplanner.weathersource.IAdvancedWeatherSource;
import csci310.weatherplanner.weathersource.IFavoritesManager;
import csci310.weatherplanner.weathersource.IForecastSource;
import csci310.weatherplanner.weathersource.IPlaceImageSource;
import csci310.weatherplanner.weathersource.Month;
import csci310.weatherplanner.weathersource.MonthTemp;
import csci310.weatherplanner.weathersource.TempUnit;
import csci310.weatherplanner.weathersource.UserFavoritesManager;
import csci310.weatherplanner.weathersource.WeatherIcon;
import csci310.weatherplanner.weathersource.WeatherLocation;
import csci310.weatherplanner.weathersource.WeatherSource;
import csci310.weatherplanner.weathersource.mock.AdvancedWeatherMock;
import csci310.weatherplanner.weathersource.mock.FavoritesMock;
import csci310.weatherplanner.weathersource.mock.ForecastMock;
import csci310.weatherplanner.weathersource.mock.MockSession;
import csci310.weatherplanner.weathersource.mock.PlaceImageMock;

@WebServlet("/detailedweatherservlet")
public class DetailedWeatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Gson gson;
	private final WeatherSource weatherSource;
	private IForecastSource forecastSource;
	private IAdvancedWeatherSource advForecastSource;
	private IFavoritesManager favoritesManager;
	private IPlaceImageSource imageSource;
	
	public DetailedWeatherServlet() {
		super();
		if(System.getenv().containsKey("mock") && System.getenv("mock").equals("true")) {
			initMockedSources();
		}
		else {
			initSources();
		}
        gson = new Gson();
		weatherSource = new WeatherSource(forecastSource, advForecastSource, favoritesManager, imageSource);
    }
	
    private void initMockedSources() {
    	WeatherLocation forecastSourceCurrentWeather;
    	List<WeatherLocation> forecastSourceCurrentWeathers;
    	List<DailyForecast> forecastSourceForecast;
    	
    	List<WeatherLocation> advForecastSourceLocations;
    	List<MonthTemp> advForecastSourceHistorical;
    	
    	List<String> imageSourceImages;
    	forecastSourceCurrentWeather = new WeatherLocation("Atlanta", "US", "Light rain", 
				"/rainy-1.svg", "/light-rain-animate.gif", 42, 63, 56, TempUnit.Fahrenheit, 512, false, 0);
		
		forecastSourceCurrentWeathers = new ArrayList<WeatherLocation>();
		forecastSourceCurrentWeathers.add(new WeatherLocation("Atlanta", "US", "Light rain", "/rainy-1.svg", "/rainy-1.gif",
				42, 63, 56, TempUnit.Fahrenheit, 512, false, 0));
		forecastSourceCurrentWeathers.add(new WeatherLocation("Los Angeles", "US", "Sunny", "/sunny.png", "/sunny-animate.gif",
				62, 83, 76, TempUnit.Fahrenheit, 3, false, 0));
		forecastSourceCurrentWeathers.add(new WeatherLocation("New York", "US", "Snowing", "/snow.png", "/snow-animate.gif",
				25, 41, 30, TempUnit.Fahrenheit, 800, false, 0));
		forecastSourceCurrentWeathers.add(new WeatherLocation("Boston", "US", "Snowing", "/snow.png", "/snow-animate.gif",
				24, 39, 31, TempUnit.Fahrenheit, 800, false, 0));
		forecastSourceCurrentWeathers.add(new WeatherLocation("Baltimore", "US", "Snowing", "/snow.png", "/snow-animate.gif",
				23, 40, 32, TempUnit.Fahrenheit, 800, false, 0));
		forecastSourceCurrentWeathers.add(new WeatherLocation("Philadelphia", "US", "Snowing", "/snow.png", "/snow-animate.gif",
				23, 40, 32, TempUnit.Fahrenheit, 800, false, 0));
		
		forecastSourceForecast = new ArrayList<DailyForecast>();
		forecastSourceForecast.add(new DailyForecast(new Date(Month.Apr, 4), "/icons/static/rainy-1.svg", 42, 67, TempUnit.Fahrenheit));
		forecastSourceForecast.add(new DailyForecast(new Date(Month.Apr, 5), "/icons/static/rainy-1.svg", 54, 72, TempUnit.Fahrenheit));
		forecastSourceForecast.add(new DailyForecast(new Date(Month.Apr, 6), "/icons/static/rainy-1.svg", 51, 63, TempUnit.Fahrenheit));
		forecastSourceForecast.add(new DailyForecast(new Date(Month.Apr, 7), "/icons/static/rainy-1.svg", 60, 77, TempUnit.Fahrenheit));
		forecastSourceForecast.add(new DailyForecast(new Date(Month.Apr, 8), "/icons/static/rainy-1.svg", 41, 62, TempUnit.Fahrenheit));
		
		// AdvForecastSource mock data
		advForecastSourceLocations = new ArrayList<WeatherLocation>();
		advForecastSourceLocations.add(new WeatherLocation("Atlanta", "US", "Light rain", "/icons/static/rainy-1.svg", "/light-rain-animate.gif",
				42, 63, 56, TempUnit.Fahrenheit, 512, false, 2));
		advForecastSourceLocations.add(new WeatherLocation("Los Angeles", "US", "Sunny", "/icons/static/sunny.png", "/sunny-animate.gif",
				62, 83, 76, TempUnit.Fahrenheit, 3, false, 2));
		advForecastSourceLocations.add(new WeatherLocation("New York", "US", "Snowing", "/icons/static/snow.png", "/snow-animate.gif",
				25, 41, 30, TempUnit.Fahrenheit, 800, false, 2));
		advForecastSourceLocations.add(new WeatherLocation("Boston", "US", "Snowing", "/icons/static/snow.png", "/snow-animate.gif",
				25, 41, 30, TempUnit.Fahrenheit, 800, false, 2));
		advForecastSourceLocations.add(new WeatherLocation("Baltimore", "US", "Snowing", "/icons/static/snow.png", "/snow-animate.gif",
				25, 41, 30, TempUnit.Fahrenheit, 800, false, 2));
		advForecastSourceLocations.add(new WeatherLocation("Philadelphia", "US", "Snowing", "/icons/static/snow.png", "/snow-animate.gif",
				25, 41, 30, TempUnit.Fahrenheit, 800, false, 2));
		
		advForecastSourceHistorical = new ArrayList<MonthTemp>();
		advForecastSourceHistorical.add(new MonthTemp(Month.Jan, 34, 61));
		advForecastSourceHistorical.add(new MonthTemp(Month.Feb, 38, 65));
		advForecastSourceHistorical.add(new MonthTemp(Month.Mar, 43, 70));
		advForecastSourceHistorical.add(new MonthTemp(Month.Apr, 63, 81));
		advForecastSourceHistorical.add(new MonthTemp(Month.May, 75, 85));
		advForecastSourceHistorical.add(new MonthTemp(Month.Jun, 76, 89));
		advForecastSourceHistorical.add(new MonthTemp(Month.Jul, 78, 93));
		advForecastSourceHistorical.add(new MonthTemp(Month.Aug, 73, 84));
		advForecastSourceHistorical.add(new MonthTemp(Month.Sep, 64, 79));
		advForecastSourceHistorical.add(new MonthTemp(Month.Nov, 56, 65));
		advForecastSourceHistorical.add(new MonthTemp(Month.Dec, 31, 57));
		
		imageSourceImages = new ArrayList<String>();
		imageSourceImages.add("https://upload.wikimedia.org/wikipedia/commons/d/dc/Georgia_State_Capitol%2C_Atlanta%2C_West_view_20160716_1.jpg");
		imageSourceImages.add("https://upload.wikimedia.org/wikipedia/commons/a/a6/Beath_Dickey_House_Exterior_2018.jpg");
		imageSourceImages.add("https://upload.wikimedia.org/wikipedia/commons/8/8f/Bright_Atlanta.jpg");
		
    	forecastSource = new ForecastMock(forecastSourceCurrentWeather, forecastSourceCurrentWeathers, forecastSourceForecast);;
		advForecastSource = new AdvancedWeatherMock(advForecastSourceLocations, advForecastSourceHistorical);
		favoritesManager = new UserFavoritesManager(UserManager.getGlobalUserManager());
		imageSource = new PlaceImageMock(imageSourceImages);
    }
    
    private void initSources() {
		forecastSource = new AerisSimpleForecastSource(new APIRequester("https://api.aerisapi.com"));
		advForecastSource = new AerisAdvancedWeatherSource(new APIRequester("https://api.aerisapi.com"));
		favoritesManager = new UserFavoritesManager(UserManager.getGlobalUserManager());
		imageSource = new GooglePlacesAPI(new APIRequester("https://maps.googleapis.com/maps/api/place"));
    }
  
    
    public DetailedWeatherServlet(WeatherSource weatherSource) {
    	super();
    	gson = new Gson();
    	this.weatherSource = weatherSource;
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		if(!request.getParameterMap().containsKey("loc")) {
			response.setStatus(400);
			return;
		}

		String loc = request.getParameter("loc");

		TempUnit unit = Enums.getIfPresent(TempUnit.class, request.getParameter("unit")).orNull();
		if(unit == null)
		{
			response.setStatus(400);
			return;
		}
		
		DetailedForecast detailedForecast = weatherSource.getDetailedForecast(request.getSession(), loc, unit);
	    out.print(gson.toJson(detailedForecast));
	    out.flush();
	}
}