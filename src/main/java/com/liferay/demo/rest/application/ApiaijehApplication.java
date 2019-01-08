package com.liferay.demo.rest.application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

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

}