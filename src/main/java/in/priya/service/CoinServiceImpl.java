package in.priya.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.priya.entity.ApiRequestResponse;
import in.priya.entity.User;
import in.priya.repository.UserRepository;
import in.priya.repositry.ApiRequestResponseRepository;

@Service
public class CoinServiceImpl implements CoinService {
	
	
    @Value("${api_key}")
    private String apiKey;
    
   @Value("${api_url}")
    private String apiUrl;

    @Autowired
    private ApiRequestResponseRepository repository;
    
    @Autowired
    private UserRepository userRepo;
       
    private RestTemplate restTemplate;

    public CoinServiceImpl(ApiRequestResponseRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public String getCoinDetails(Integer userId, String symbols) {
    	
    	    Optional<User> findById = userRepo.findById(userId);
    	    if(findById.isPresent())
    	    {
    	    
    	String sanitizedSymbols = sanitizeSymbols(symbols);
    	
        if (sanitizedSymbols.isEmpty()) {
            throw new IllegalArgumentException("Symbols must contain valid, comma-separated, alphanumeric cryptocurrency symbols.");
        }
    	
    	
        String requestUrl = apiUrl + "?symbol=" + symbols;
        
        // Set up the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", apiKey);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
        
        // Save the request and response to the database
        ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
        apiRequestResponse.setUserId(userId);
        apiRequestResponse.setRequestUrl(requestUrl);
        apiRequestResponse.setResponseBody(response.getBody());
        apiRequestResponse.setTimestamp(LocalDateTime.now());
        
        repository.save(apiRequestResponse);
        
        return response.getBody();
    	    }
    	    else
    	    {
    	    	return "Entered user id not present..";
    	    }
    }
    
    private String sanitizeSymbols(String symbols) {
        // Split by commas, trim whitespace, and filter invalid symbols
        return Arrays.stream(symbols.split(","))
                .map(String::trim)
                .filter(symbol -> symbol.matches("^[a-zA-Z0-9]+$"))
                .collect(Collectors.joining(","));
    }
}
