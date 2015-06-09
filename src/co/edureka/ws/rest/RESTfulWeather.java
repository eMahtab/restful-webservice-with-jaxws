package co.edureka.ws.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;



@WebServiceProvider
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
@BindingType(value = HTTPBinding.HTTP_BINDING)

public class RESTfulWeather implements Provider<Source> {

	@Resource
	protected WebServiceContext wsContext;

	@Override
	public Source invoke(Source request) {		
		MessageContext msg_cxt = wsContext.getMessageContext();
		String httpMethod = (String) msg_cxt
				.get(MessageContext.HTTP_REQUEST_METHOD);
		//System.out.println("Http Method : " + httpMethod);
		if (httpMethod.equalsIgnoreCase("GET")) {
			 return doGet(msg_cxt);
		}
		return null;
	}

	private Source doGet(MessageContext msg_cxt) {
		String query_string = (String) msg_cxt.get(MessageContext.QUERY_STRING);
		StringBuffer text=new StringBuffer("");		
		String cityName=query_string.split("=")[1];
		try {
			URL url = new URL(
					"http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&mode=xml");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");		
			urlConnection.connect();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String line = null;			
			
			while ((line = bReader.readLine()) != null) {
				System.out.println(line);			
				text=text.append(line);			
			}		
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return new StreamSource( new StringReader(text.toString()) );
		
	}	
	
}


