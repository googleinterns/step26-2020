/** 
 * TODO: Include License
 * Helpful links:
 *  https://github.com/google/google-api-java-client-samples
 */

package com.google.growpod.servlets;

import com.google.gson.Gson;
import com.google.api.services.calendar.Calendar;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.api.client.auth.oauth2.Credential;

import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.Event;
import java.util.List;
import com.google.api.client.util.DateTime;

@WebServlet("/calendartest")
public class CalendarTestServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Generate credentials
    /**
     * Current issues: 
     * How to generate the client secrets json file?
     */
    Credential authCredential = authorize();
    AppIdentityCredential credential = new AppIdentityCredential(Arrays.asList(UrlshortenerScopes.URLSHORTENER));
    Urlshortener shortener = new Urlshortener.Builder(new UrlFetchTransport(), new JacksonFactory(), credential).build();
    UrlHistory history = shortener.URL().list().execute();

    // Create a client to access the calendar
    // Reference link: https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/com/google/api/services/calendar/Calendar.html
    // Issue: is the variable credential the proper parameter for the constructor?
    Calendar client = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credential).build();
    
    // get events from user's calendar.
    CalendarList allCalendars = client.calendarList().list().execute();
 
    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson(allCalendars));
  }

   /** Authorizes the installed application to access user's protected data. */
   private static Credential authorize() throws Exception {
   // load client secrets
   GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
       new InputStreamReader(CalendarSample.class.getResourceAsStream("/client_secrets.json")));
   // set up authorization code flow
   GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
       httpTransport, JSON_FACTORY, clientSecrets,
       Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
      .build();
   // authorize
   return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  /* Obtain a calendar client; currently unused yet this should serve as an alternative to the 
  current implmentation on the doGet */
  public static Calendar getCalendarClient() throws IOException {
    String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
    Credential credential = newFlow().loadCredential(userId);
    return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
  }
}
