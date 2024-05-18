package in.priya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.priya.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	
	public User findByEmail(String email);
	public User findByUsername(String username);

}
