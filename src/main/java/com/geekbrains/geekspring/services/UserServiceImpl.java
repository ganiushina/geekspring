package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.Product;

import com.geekbrains.geekspring.exceptions.UserNotFoundException;
import com.geekbrains.geekspring.repositories.RoleRepository;
import com.geekbrains.geekspring.repositories.UserRepository;
import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.Role;
import com.geekbrains.geekspring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Autowired
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public User findByUserName(String userName) {
		return userRepository.findOneByUserName(userName);
	}

	@Override
	@Transactional
	public void save(SystemUser systemUser) {
		User user = new User();
		user.setUserName(systemUser.getUserName());
		user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
		user.setFirstName(systemUser.getFirstName());
		user.setLastName(systemUser.getLastName());
		user.setEmail(systemUser.getEmail());

		com.baeldung.grpc.Role role = com.baeldung.grpc.Role.newBuilder()
				.setName("ROLE_EMPLOYEE")
				.build();

		user.setRoles(Arrays.asList(roleRepository.findOneByName(role.getName())));

		userRepository.save(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findOneByUserName(userName);

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));


	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	public User saveOrUpdate(User user) {
		return userRepository.save(user);
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Can't found user with id = " + id));
	}
}
