package gl.service.impl;

import gl.model.entity.RoleEntity;
import gl.model.entity.UserEntity;
import gl.repository.UserRepository;
import gl.service.RoleService;
import gl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
		});
		return authorities;
	}


	@Override
	public void delete(long id) {
		userRepository.deleteById(id);
	}

	@Override
	public UserEntity findOne(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserEntity findById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public UserEntity save(UserEntity user) {

		UserEntity foundUser = findOne(user.getUsername());

		if (foundUser == null) {

			UserEntity newUser = new UserEntity();
			newUser.setUsername(user.getUsername());
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			Set<RoleEntity> roles = new HashSet<>();

			RoleEntity role = roleService.findByRole("USER");
			roles.add(role);
			newUser.setRoles(roles);
			return userRepository.save(newUser);

		} else {
			return null;
		}
	}
}
