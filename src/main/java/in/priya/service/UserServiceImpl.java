package in.priya.service;


import java.time.LocalDateTime;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.priya.dto.UpdatableFields;
import in.priya.entity.User;
import in.priya.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	
	
	@Override
	public User registerUser(User user) {
		
		  User findByEmail = userRepo.findByEmail(user.getEmail());
		
		 if(findByEmail!=null)
		 {
			 throw new RuntimeErrorException(null,"Entered Email is already in use");
		 }
		    User findByUsername = userRepo.findByUsername(user.getUsername());
		    if(findByUsername!=null)
		    {
				 throw new RuntimeErrorException(null, "Entered Username is already present");

		    }
		return userRepo.save(user);
	}
	
	public User login(String username, String password) {
		
		 User user = userRepo.findByUsername(username);
		 if(user!=null && username.equals(user.getUsername()) && password.equals(user.getPassword()))
		 {
			 return user;
		 }
		 
		return null;
	}
	

	public User updateUser(Integer id, UpdatableFields updatable) {
	       
		      User user = userRepo.findById(id).get();
		      
		      user.setFirstName(updatable.getFirstName());
		      user.setLastName(updatable.getLastName());
		      user.setMobileNumber(updatable.getMobileNumber());
		      user.setPassword(updatable.getPassword());
		      
		      userRepo.save(user);
		     
		return user;
	}
	
	
	
}
