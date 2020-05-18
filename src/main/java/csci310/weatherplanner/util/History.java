package csci310.weatherplanner.util;

public class History {
    public String requestBody;
    public String responseBody;
    public int user_id;
    History(String request_body, String response_body)
    {
        requestBody = request_body;
        responseBody = response_body;
    }
}
