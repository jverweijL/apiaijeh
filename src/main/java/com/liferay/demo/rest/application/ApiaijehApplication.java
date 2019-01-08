package com.liferay.demo.rest.application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;

/**
 * @author jverweij
 * http://localhost:8080/o/apiaijeh/apis
 */
@ApplicationPath("/apis")
@Component(immediate = true, service = Application.class)
public class ApiaijehApplication extends Application {

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@GET
	@Produces("text/plain")
	public String working() {
		return "It works!";
	}

	@GET
	@Path("/postcode/test")
	@Produces("text/plain")
	public String zipcodetest() {

		HttpClient client = HttpClientBuilder.create()
				//.setDefaultCredentialsProvider(provider)
				.build();

		// build http request and assign multipart upload data
		HttpUriRequest request = RequestBuilder
				.get("https://api.postcodeapi.nu/v2/addresses/?postcode=3438DC&number=9")
				.addHeader("X-Api-Key","chiImWTzBc1u3GK4xbnsJ7eVVXGT7YQz37cvLA73")
				.build();

		HttpResponse response = null;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response.getStatusLine());
		return "";
	}

	@GET
	@Path("/postcode/address")
	@Produces("text/plain")
	public String morning(
		@QueryParam("postcode") String postcode,
		@QueryParam("number") String number) {

		System.out.println("Trying to fetch the address info...");

		HttpClient client = HttpClientBuilder.create().build();

	// build http request and assign multipart upload data
		HttpUriRequest request = RequestBuilder
			.get("https://api.postcodeapi.nu/v2/addresses/?postcode=" + postcode.replaceAll("\\s","") + "&number=" + number)
			.addHeader("X-Api-Key","chiImWTzBc1u3GK4xbnsJ7eVVXGT7YQz37cvLA73")
			.build();

		HttpResponse response = null;
			try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
			//TODO create new jsonobject and return interesting info
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	@GET
	@Path("/compare/dates")
	@Produces("text/plain")
	public String comparedates(
			@QueryParam("date1") String datestring1,
			@QueryParam("date2") String datestring2) throws ParseException, JsonProcessingException {

		System.out.println("comparing dates...");

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date date1 = sdf.parse(datestring1);
		Date date2 = sdf.parse(datestring2);

		System.out.println("date1 : " + sdf.format(date1));
		System.out.println("date2 : " + sdf.format(date2));


		long diffInMillies = date2.getTime() - date1.getTime();
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("diffdays",diff);
		/*{
			"diffdays": -1,
				"message": "date1 smaller"
		}*/

		if (date1.after(date2)) {
			System.out.println("Date1 is after Date2");
			rootNode.put("message", "Date1 is after Date2");
		}

		if (date1.before(date2)) {
			System.out.println("Date1 is before Date2");
			rootNode.put("message", "Date1 is before Date2");
		}

		if (date1.equals(date2)) {
			System.out.println("Date1 is equal Date2");
			rootNode.put("message", "Date1 is equal Date2");
		}

		return mapper.writer().writeValueAsString(rootNode);
	}

}