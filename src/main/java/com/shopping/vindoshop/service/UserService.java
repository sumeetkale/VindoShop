package com.shopping.vindoshop.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.shopping.vindoshop.dao.UserDao;

@Component
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	private List<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (String userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(
				setAuths);

		return Result;
	}

	// Converts com.mkyong.users.model.User user to
	// org.springframework.security.core.userDetail.User
	private User buildUserForAuthentication(
			com.shopping.vindoshop.model.User user,
			List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(),
				user.isActive(), true, true, true, authorities);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) {

		com.shopping.vindoshop.model.User user = null;
		try {
			user = userDao.findByUserName(username, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<String> userRoleSet = new HashSet<String>();
		if (user.isAdmin())
			userRoleSet.add("ROLE_ADMIN");
		else
			userRoleSet.add("ROLE_USER");
		List<GrantedAuthority> authorities = buildUserAuthority(userRoleSet);

		return buildUserForAuthentication(user, authorities);

	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}