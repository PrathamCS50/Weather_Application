package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
		//API
		String apiKey = "a3ea723efe51005c40460c28a8ad9434";
		
		String city = request.getParameter("city");
		
		String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

		//API Integration
		URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
		
        //Reading data from network
        InputStream inputstream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputstream);
        
        StringBuilder responseContent = new StringBuilder();
        
        //Want to store in string
        //Input lene le liye from the reader, will create scanner object
        Scanner sc = new Scanner(reader);
        
        while(sc.hasNext()) {
        	responseContent.append(sc.nextLine());
        }
        
        sc.close();
//        System.out.println(responseContent);
        //All the content is displayed in string Format
        //Conversion from String to JSON Format 
        
        //TypeCasting
        //Added Libraries
        Gson gson = new Gson();
        JsonObject jsonobject = gson.fromJson(responseContent.toString(), JsonObject.class);
        
        
        
//        	{"coord":{"lon":77.2311,"lat":28.6128},"weather":[{"id":721,"main":"Haze",
//        	"description":"haze","icon":"50d"}],"base":"stations",
//			"main":{"temp":312.24,"feels_like":309.56,"temp_min":312.24,"temp_max":312.24,"pressure":1010,
//        	"humidity":12},"visibility":4000,"wind":{"speed":2.57,"deg":270},
//			"clouds":{"all":44},"dt":1715673945,
//			"sys":{"type":1,"id":9165,"country":"IN","sunrise":1715644855,"sunset":1715693645},
//			"timezone":19800,"id":1261481,"name":"New Delhi","cod":200}
        
        
        //Date & Time
        long dateTimestamp = jsonobject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
        //Temperature
        double temperatureKelvin = jsonobject.getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
       
        //Humidity
        int humidity = jsonobject.getAsJsonObject("main").get("humidity").getAsInt();
        
        //Wind Speed
        double windSpeed = jsonobject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        //Weather Condition
        String weatherCondition = jsonobject.getAsJsonArray("weather").get(0).getAsJsonObject()
        		.get("main").getAsString();
                
        
     // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responseContent.toString());
        
        connection.disconnect();
	}catch(IOException e) {
		e.printStackTrace();
	}
		// Forward the request to the weather.jsp page for rendering
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }    
}


