
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiTest {

    private static final String API_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";

    public static void main(String[] args) throws Exception {
        // Test 1: Valid User Creation
        System.out.println("Test 1: Valid User Creation");
        sendPostRequest(API_URL, "1", "Test", "Test", "9999999999", "test.test@test.com");

        // Test 2: Duplicate Phone Number
        System.out.println("Test 2: Duplicate Phone Number");
        sendPostRequest(API_URL, "1", "Test", "Test", "9999999999", "another.email@test.com");

        // Test 3: Duplicate Email Address
        System.out.println("Test 3: Duplicate Email Address");
        sendPostRequest(API_URL, "1", "Test", "Test", "8888888888", "test.test@test.com");

        // Test 4: Missing roll-number Header
        System.out.println("Test 4: Missing roll-number Header");
        sendPostRequest(API_URL, null, "Test", "Test", "7777777777", "missing.rollnumber@test.com");

        // Test 5: Empty firstName Field
        System.out.println("Test 5: Empty firstName Field");
        sendPostRequest(API_URL, "1", "", "Test", "6666666666", "empty.firstname@test.com");

        // Test 6: Invalid phoneNumber Format
        System.out.println("Test 6: Invalid phoneNumber Format");
        sendPostRequest(API_URL, "1", "Test", "Test", "invalidPhone", "invalid.phone@test.com");

       // Test 7: Empty lastName Field
        System.out.println("Test 7: Empty lastName Field");
        sendPostRequest(API_URL, "1", "Test", "", "invalidPhone", "valid.phone@test.com");
        
       // Test 8: Empty both firstName and lastName Field
        System.out.println("Test 8: Empty both firstName and lastName Field");
        sendPostRequest(API_URL, "1", "", "", "invalidPhone", "test8@test.com");
        
        // Test 9: Invalid phoneNumber Format and duplicate email address
        System.out.println("Test 9: Invalid phoneNumber Format  and duplicate email address");
        sendPostRequest(API_URL, "1", "Test", "Test", "phonenotest", "invalid.phone@test.com");
        
     // Test 10: Invalid emailId Format
        System.out.println("Test 10: Invalid emailId Format");
        sendPostRequest(API_URL, "1", "Test", "Test", "4444444444", "invalidemail");
        
     // Test 11: Large Input Values
        System.out.println("Test 11: Large Input Values");
        sendPostRequest(API_URL, "1", "VeryLongFirstNameThatExceedsNormalLength", "VeryLongLastNameThatExceedsNormalLength",
                "2222222222", "large.input@test.com");
     
     // Test 12: Malformed Phone Number
        System.out.println("Test 12: Malformed Phone Number");
        sendPostRequest(API_URL, "1", "Test", "Test", "99999-99999", "malformed.phone@test.com");
     
        // Test 13: Numeric firstName
        System.out.println("Test 13: Numeric firstName");
        sendPostRequest(API_URL, "1", "12345", "Test", "8888888888", "numeric.firstname@test.com");
        
     // Test 14: Numeric lastName
        System.out.println("Test 14: Numeric lastName");
        sendPostRequest(API_URL, "1", "Test", "12345", "8888888888", "numeric.firstname@test.com");
      
     // Test 15: SQL Injection Attempt in emailId
        System.out.println("Test 15: SQL Injection Attempt in emailId");
        sendPostRequest(API_URL, "1", "Test", "Test", "6666666666", "test@test.com' OR '1'='1");
        
     // Test 16: HTML Injection in lastName
        System.out.println("Test 16: HTML Injection in lastName");
        sendPostRequest(API_URL, "1", "Test", "<b>Test</b>", "5555555555", "html.injection@test.com");
      
     // Test 17: Check for Rate Limiting
        System.out.println("Test 17: Check for Rate Limiting");
        for (int i = 0; i < 50; i++) {
            sendPostRequest(API_URL, "1", "Test", "Test", "4444444444", "rate.limiting@test.com");
        }
        
        // Test 18: Invalid HTTP Method
        System.out.println("Test 18: Invalid HTTP Method");
        sendInvalidHttpMethod(API_URL);
        
        // Test 19: Missing Content-Type Header
        System.out.println("Test 19: Missing Content-Type Header");
        sendPostRequestWithoutContentType(API_URL, "1", "Test", "Test", "1234567890", "missing.contenttype@test.com");
        
     // Test 20: Numeric both firstName and lastName
        System.out.println("Test 20: Numeric both firstName and lastName");
        sendPostRequest(API_URL, "1", "12345", "12356", "8888888888", "numeric.firstname@test.com");
        
        
    }

    private static void sendPostRequest(String apiUrl, String rollNumber, String firstName, String lastName, String phoneNumber, String emailId) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        if (rollNumber != null) {
            conn.setRequestProperty("roll-number", rollNumber);
        }

        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"phoneNumber\": \"%s\", \"emailId\": \"%s\"}",
                firstName, lastName, phoneNumber, emailId);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Status Code: " + responseCode);

        try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
            String responseBody = scanner.useDelimiter("\\A").next();
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("---------------------------------------------------");
    }
}

