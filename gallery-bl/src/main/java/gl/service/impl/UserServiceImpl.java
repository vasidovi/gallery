package gl.service.impl;

import gl.model.entity.RoleEntity;
import gl.model.entity.UserEntity;
import gl.repository.UserRepository;
import gl.service.RoleService;
import gl.service.UserService;
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

	private UserRepository userRepository;
	private RoleService roleService;
	private BCryptPasswordEncoder bcryptEncoder;


	public UserServiceImpl(UserRepository userRepository,
						   RoleService roleService,
						   BCryptPasswordEncoder bcryptEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.bcryptEncoder = bcryptEncoder;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole())));
		return authorities;
	}

	@Override
	public UserEntity findOne(String username) {
		return userRepository.findByUsername(username);
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

			if (userRepository.findAll().size() == 0 ) {
				RoleEntity roleAdmin = roleService.findByRole("ADMIN");
				roles.add(roleAdmin);
			}

			newUser.setRoles(roles);
			return userRepository.save(newUser);

		} else {
			return null;
		}
	}
}
