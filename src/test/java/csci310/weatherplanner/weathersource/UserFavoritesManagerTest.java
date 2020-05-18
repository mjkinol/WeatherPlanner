package csci310.weatherplanner.weathersource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.db.Database;
import csci310.weatherplanner.weathersource.mock.MockSession;

public class UserFavoritesManagerTest {
	private UserManager userManager;
	private UserFavoritesManager favoritesManager;
	
	private HttpSession session1;
	private HttpSession session2;

	@Before
	public void setUp() throws Exception {
		Database.resetDB();
		userManager = new UserManager();
		
		userManager.CreateUser("user1", "password");
		userManager.CreateUser("user2", "password");
		favoritesManager = new UserFavoritesManager(userManager);
		
		session1 = new MockSession("1");
		session2 = new MockSession("2");
	}

	@Test
	public void testGetFavoritesAcrossSessions() {
		userManager.LogIn("user1", "password", session1);
		
		List<String> favoriteLocations = new ArrayList<String>();
		favoriteLocations.add("Atlanta,US");
		favoriteLocations.add("New York City,US");
		
		for(String loc: favoriteLocations)
			favoritesManager.addFavorite(session1, loc);
		
		
		userManager.LogIn("user1", "password", session2);
		List<String> result = favoritesManager.getFavorites(session2);
		
		assertNotNull(result);
		assertEquals(favoriteLocations, result);
	}

	@Test
	public void testGetFavoritesNullSession() {
		List<String> favoriteLocations = new ArrayList<String>();
		favoriteLocations.add("Atlanta,US");
		favoriteLocations.add("New York City,US");
		
		for(String loc: favoriteLocations)
			favoritesManager.addFavorite(session1, loc);
		
		List<String> result = favoritesManager.getFavorites(null);
		
		// Should return an empty list
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testLabelFavoriteAcrossSessions() {
		List<WeatherLocation> locations = new ArrayList<WeatherLocation>();
		locations.add(new WeatherLocation("Atlanta", "US", "Light rain", "/light-rain.png", "/light-rain-animate.gif",
				42, 63, 56, TempUnit.Fahrenheit, 512, false, 0));
		locations.add(new WeatherLocation("Los Angeles", "US", "Sunny", "/sunny.png", "/sunny-animate.gif",
				62, 83, 76, TempUnit.Fahrenheit, 3, false, 0));
		locations.add(new WeatherLocation("New York", "US", "Snowing", "/snow.png", "/snow-animate.gif",
				25, 41, 30, TempUnit.Fahrenheit, 800, false, 0));
		
		userManager.LogIn("user1", "password", session1);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		favoritesManager.addFavorite(session1, "New York,US");
		
		userManager.LogIn("user1", "password", session2);
		favoritesManager.labelFavorites(session2, locations);
		
		// Atlanta is a favorite
		assertTrue(locations.get(0).isFavorite());
		
		// Los Angeles is not a favorite
		assertFalse(locations.get(1).isFavorite());
		
		// New York is a favorite
		assertTrue(locations.get(2).isFavorite());
	}

	@Test
	public void testIsFavoriteTrue() {
		userManager.LogIn("user1", "password", session1);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		
		assertTrue(favoritesManager.isFavorite(session1, "Atlanta,US"));
	}
	
	@Test
	public void testIsFavoriteFalse() {
		assertFalse(favoritesManager.isFavorite(session1, "Atlanta,US"));
	}
	
	@Test
	public void testIsFavoriteTrueAcrossSessions() {
		userManager.LogIn("user1", "password", session1);
		userManager.LogIn("user1", "password", session2);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		
		assertTrue(favoritesManager.isFavorite(session2, "Atlanta,US"));
	}
	
	public void testIsFavoriteNullSession() {
		favoritesManager.addFavorite(session1, "Atlanta,US");
		
		assertFalse(favoritesManager.isFavorite(null, "Atlanta,US"));
	}

	@Test
	public void testAddFavorite() {
		userManager.LogIn("user1", "password", session1);
		userManager.LogIn("user1", "password", session2);
		
		assertFalse(favoritesManager.isFavorite(session1, "Atlanta,US"));
		assertFalse(favoritesManager.isFavorite(session1, "New York,US"));
		
		assertFalse(favoritesManager.isFavorite(session2, "Atlanta,US"));
		assertFalse(favoritesManager.isFavorite(session2, "Los Angeles,US"));
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		favoritesManager.addFavorite(session1, "New York,US");
		
		favoritesManager.addFavorite(session2, "Atlanta,US");
		favoritesManager.addFavorite(session2, "Los Angeles,US");
		
		assertTrue(favoritesManager.isFavorite(session1, "Atlanta,US"));
		assertTrue(favoritesManager.isFavorite(session1, "New York,US"));
		
		assertTrue(favoritesManager.isFavorite(session2, "Atlanta,US"));
		assertTrue(favoritesManager.isFavorite(session2, "Los Angeles,US"));
	}
	
	@Test
	public void testAddFavoriteAcrossSession() {
		assertFalse(favoritesManager.isFavorite(session1, "Atlanta,US"));
		assertFalse(favoritesManager.isFavorite(session1, "New York,US"));
		
		assertFalse(favoritesManager.isFavorite(session2, "Atlanta,US"));
		assertFalse(favoritesManager.isFavorite(session2, "Los Angeles,US"));
		
		userManager.LogIn("user1", "password", session1);
		userManager.LogIn("user1", "password", session2);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		favoritesManager.addFavorite(session1, "New York,US");
		
		favoritesManager.addFavorite(session2, "Atlanta,US");
		favoritesManager.addFavorite(session2, "Los Angeles,US");
		
		assertTrue(favoritesManager.isFavorite(session1, "Atlanta,US"));
		assertTrue(favoritesManager.isFavorite(session1, "New York,US"));
		assertTrue(favoritesManager.isFavorite(session1, "Los Angeles,US"));
	}
	
	@Test
	public void testAddFavoriteTrue() {
		userManager.LogIn("user1", "password", session1);
		
		// Adding a favorite is idempotent
		assertTrue(favoritesManager.addFavorite(session1, "Atlanta,US"));
		assertTrue(favoritesManager.addFavorite(session1, "Atlanta,US"));
	}
	
	@Test
	public void testAddFavoriteNullSession() {		
		assertFalse(favoritesManager.addFavorite(null, "Atlanta,US"));
	}
	
	@Test
	public void testAddFavoriteNullLocation() {		
		assertFalse(favoritesManager.addFavorite(session1, null));
	}

	@Test
	public void testRemoveFavorite() {
		userManager.LogIn("user1", "password", session1);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		favoritesManager.addFavorite(session1, "New York,US");
		
		favoritesManager.removeFavorite(session1, "Atlanta,US");
		favoritesManager.removeFavorite(session2, "New York,US");
		
		assertFalse(favoritesManager.isFavorite(session1, "Atlanta,US"));
		assertTrue(favoritesManager.isFavorite(session1, "New York,US"));
	}
	
	@Test
	public void testRemoveFavoriteAcrossSession() {
		userManager.LogIn("user1", "password", session1);
		userManager.LogIn("user1", "password", session2);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		favoritesManager.addFavorite(session1, "New York,US");
		
		favoritesManager.removeFavorite(session2, "New York,US");
		
		assertTrue(favoritesManager.isFavorite(session1, "Atlanta,US"));
		assertFalse(favoritesManager.isFavorite(session1, "New York,US"));
	}
	
	@Test
	public void testRemoveFavoriteTrue() {
		userManager.LogIn("user1", "password", session1);
		
		favoritesManager.addFavorite(session1, "Atlanta,US");
		
		// Removing a favorite is idempotent
		assertTrue(favoritesManager.removeFavorite(session1, "Atlanta,US"));
		assertTrue(favoritesManager.removeFavorite(session1, "Atlanta,US"));
	}
	
	@Test
	public void testRemoveFavoriteNullSession() {
		favoritesManager.addFavorite(session1, "Atlanta,US");

		assertFalse(favoritesManager.removeFavorite(null, "Atlanta,US"));
	}
	
	@Test
	public void testRemoveFavoriteNullLocation() {
		favoritesManager.addFavorite(session1, "Atlanta,US");

		assertFalse(favoritesManager.removeFavorite(session1, null));
	}

}
