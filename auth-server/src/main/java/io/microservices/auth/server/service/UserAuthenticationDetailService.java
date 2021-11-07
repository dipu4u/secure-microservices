package io.microservices.auth.server.service;

import io.microservices.auth.server.model.SystemUser;
import io.microservices.auth.server.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationDetailService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationDetailService.class);

    private final ResourceLoader resourceLoader;
    private final List<SystemUser> users;

    public UserAuthenticationDetailService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        users = loadUsers();
        LOG.info("Users loaded from disk {}", users.size());
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> userName.equals(user.getUserName())).findFirst()
                .map(user -> new User(userName, user.getPassword(), true, true, true, true,
                        Arrays.stream(user.getPermissions())
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()))
                ).orElseThrow(() -> {
                    LOG.info("User not found with name {}", userName);
                    return new UsernameNotFoundException("Invalid User " + userName);
                });
    }

    private List<SystemUser> loadUsers() {
        Resource userResource = resourceLoader.getResource("classpath:users.json");
        try (InputStream inputStream = userResource.getInputStream()) {
            return JsonUtils.readList(inputStream, SystemUser.class);
        } catch(Exception e) {
            LOG.error("Error while reading Users. {}", e.getMessage(), e);
        }
        return Optional.ofNullable(users).orElse(List.of());
    }
}
