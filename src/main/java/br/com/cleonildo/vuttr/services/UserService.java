package br.com.cleonildo.vuttr.services;

import br.com.cleonildo.vuttr.dto.UserRequest;
import br.com.cleonildo.vuttr.dto.UserResponse;
import br.com.cleonildo.vuttr.entities.User;
import br.com.cleonildo.vuttr.handler.excpetion.NotFoundException;
import br.com.cleonildo.vuttr.handler.excpetion.PasswordDontMatchException;
import br.com.cleonildo.vuttr.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static br.com.cleonildo.vuttr.handler.excpetion.constants.ExcpetionMessageConstants.USER_NOT_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_DELETED;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_ID_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_ID_NOT_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_LIST;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_LIST_EMPTY;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_PASSWORD_DONT_MATCH;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_SAVED;
import static br.com.cleonildo.vuttr.log.LogConstants.USER_UPDATE;

@Service
@Transactional
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getListUsers() {
        var response = this.userRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(UserResponse::new)
                .toList();

        if (response.isEmpty()) {
            LOG.info(USER_LIST_EMPTY);
            return response;
        }

        LOG.info(USER_LIST);
        return response;
    }

    @Transactional(readOnly = true)
    public UserResponse geUsersById(Integer id) {
        var response = this.userRepository.findById(id).orElseThrow(() -> {
            LOG.warn(USER_ID_NOT_FOUND, id);
            return new RuntimeException(USER_NOT_FOUND);
        });


        LOG.info(USER_ID_FOUND, id);
        return new UserResponse(response);
    }

    public UserResponse saveUser(UserRequest request) {
        var response = this.userRepository.save(this.userFromRequest(request));

        LOG.info(USER_SAVED);
        return new UserResponse(response);
    }

    private User userFromRequest(UserRequest request) {
        this.isPasswordsEquals(request);

        var encodedPasswor = this.passwordEncoder.encode(request.password());

        return new User(request.email(), request.username(), request.cpf(),encodedPasswor, request.role());
    }

    public UserResponse updateUser(Integer id, UserRequest request) {
        var response = this.userRepository.findById(id).orElseThrow(() -> {
            LOG.warn(USER_ID_NOT_FOUND, id);
            return new NotFoundException(USER_NOT_FOUND);
        });

        this.isPasswordsEquals(request);

        response.setEmail(request.email());
        response.setUsername(request.username());
        response.setCpf(request.cpf());
        response.setPassword(this.passwordEncoder.encode(request.password()));
        response.setRole(request.role());

        this.userRepository.save(response);

        LOG.info(USER_UPDATE);
        return new UserResponse(response);
    }

    private void isPasswordsEquals(UserRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            LOG.warn(USER_PASSWORD_DONT_MATCH);
            throw new PasswordDontMatchException(USER_PASSWORD_DONT_MATCH);
        }
    }

    public void deleteUserById(Integer id) {
        var user = this.userRepository.findById(id).orElseThrow(() -> {
            LOG.warn(USER_ID_NOT_FOUND, id);
            return new NotFoundException(USER_NOT_FOUND);
        });

        LOG.info(USER_DELETED, user.getUsername());
        this.userRepository.delete(user);
    }

}