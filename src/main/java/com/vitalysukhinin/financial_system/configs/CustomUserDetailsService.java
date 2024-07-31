package com.vitalysukhinin.financial_system.configs;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;
import java.util.logging.Logger;

public class CustomUserDetailsService extends JdbcDaoImpl implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    public CustomUserDetailsService(DataSource dataSource) {
        setDataSource(dataSource);
        setUsersByUsernameQuery("SELECT email, password, active FROM user WHERE email = ?\n");
        setAuthoritiesByUsernameQuery("select email, 'ROLE_USER' FROM user WHERE email=?");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: " + username);
        try {
            UserDetails user = super.loadUserByUsername(username);
            logger.info("User loaded successfully: " + user);
            return user;
        } catch (UsernameNotFoundException e) {
            logger.severe("User not found: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Error loading user: " + e.getMessage());
            throw new UsernameNotFoundException("Error loading user", e);
        }

    }
}
