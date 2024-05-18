package in.priya.service;

import in.priya.dto.UpdatableFields;
import in.priya.entity.User;

public interface UserService {

	public User registerUser(User user);
	public User login(String username, String password);
	
	public User updateUser(Integer id,UpdatableFields updatable);
	
	
}
