package com.shopping.vindoshop.util;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig,
			Environment env) {
		/*
		 * cfConfig.addConnectionFactory(new TwitterConnectionFactory(env
		 * .getProperty("m4m3uT2NT3phiZsDfhCz7w6iO"), env
		 * .getProperty("nvaky87895eOhRB6QG50pgmRooaB6sVupAM6KBI7Hye6sLdSjp")));
		 */
		cfConfig.addConnectionFactory(new FacebookConnectionFactory(
				"1687180814851994", "b903f56348bfacfe8a4fb2da7be096eb"));
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(
			ConnectionFactoryLocator connectionFactoryLocator) {
		return new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
