package in.priya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.priya.dto.UpdatableFields;
import in.priya.entity.User;
import in.priya.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestHeader(name = "HeaderKey", required = true) String headerValue, @RequestBody User user)
	{
	
		
        if (!"ExpectedHeaderValue".equals(headerValue)) {
            return new ResponseEntity<>("Invalid header value", HttpStatus.FORBIDDEN);
        }
        try {
            userService.registerUser(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
		
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> loginCheck( @RequestHeader(name = "X-CMC_PRO_API_KEY", required = true) String headerValue, 
			                                  @RequestParam("username") String username,
			                                  @RequestParam("password") String password,
			                                  HttpServletRequest resq)
	{
		
		 if (!"27ab17d1-215f-49e5-9ca4-afd48810c149".equals(headerValue)) {
	            return new ResponseEntity<>("Invalid header value", HttpStatus.FORBIDDEN);
	        }
		 
		  User user = userService.login(username, password);
		
		  if(user!=null)
		  {
			  HttpSession session=resq.getSession(true);
			  session.setAttribute("ID",user.getId());
			  return new ResponseEntity<>("Login successful..",HttpStatus.OK);
		  }
		  
		  else
		  {
			  return new ResponseEntity<>("Login failed ,,",HttpStatus.INTERNAL_SERVER_ERROR);

		  }
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateUser (HttpServletRequest req, @RequestBody UpdatableFields updatable)
	{
          HttpSession session=req.getSession(false);
		
		Object obj=session.getAttribute("ID");
		
		Integer id=(Integer)obj;
		
	        User updateUser = userService.updateUser(id,updatable);
	        if(updateUser!=null)
	        {
	        	return new ResponseEntity<String>("Record updated successfully of id = "+ id,HttpStatus.OK);
	        }
	        else
	        {
	        	return new ResponseEntity<String>("Record failed to update of id = "+ id,HttpStatus.OK);

	        }
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logoutFunction(HttpServletRequest req)
	{
		HttpSession session=req.getSession(false);
		session.invalidate();
		
		return new ResponseEntity<String>("Logout Successfully..",HttpStatus.OK);
	}
	
	
	    

}
