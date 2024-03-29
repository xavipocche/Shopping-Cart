package com.example.FactorITtest.Service.Impl;

import com.example.FactorITtest.Constants.IConstants;
import com.example.FactorITtest.DTO.Request.UserRequest;
import com.example.FactorITtest.DTO.Response.AddBalanceResponse;
import com.example.FactorITtest.DTO.Response.UsersResponse;
import com.example.FactorITtest.Entities.UserEntity;
import com.example.FactorITtest.Exceptions.UserException;
import com.example.FactorITtest.Repository.UserRepository;
import com.example.FactorITtest.Service.Interface.UserService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Xavier Pocchettino
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UsersResponse getAllUsers() {
        List<UserEntity> listUsers = (List) userRepository.findAll();
        
        UsersResponse usersResponse = 
                UsersResponse.builder()
                    .listUsers(listUsers)
                    .totalUsers(userRepository.countUsers())
                    .build();
        
        return usersResponse;
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) throws UserException {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        
        if(userOptional.isPresent()) {
            return userOptional;
        } else {
            throw new UserException("No se encontró el usuario solicitado");
        }
    }

    @Override
    public UserEntity saveUser(UserRequest userRequest) throws UserException {
        validateData(userRequest);
        
        if(userRequest.getBalance() == null) {
            userRequest.setBalance(BigDecimal.ZERO);
        }
        
        UserEntity userEntity = 
                UserEntity.builder()
                .name(userRequest.getName())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .balance(userRequest.getBalance().abs())
                .build();
                
        return userRepository.save(userEntity);
    }

    @Override
    public Boolean deleteUserById(Long id) throws UserException {
        Optional<UserEntity> userOptional = getUserById(id);
            
        if(userOptional.isPresent()) {
            try {
                userRepository.delete(userOptional.get());
            } catch (Exception e) {
                throw new UserException(e.getMessage());
            }
            return true;
        } else {
            throw new UserException("No se encontró el usuario solicitado para su eliminación");
        }
    }
    
    @Override
    public AddBalanceResponse addBalance(Long idUser, BigDecimal balance) throws UserException {
        Optional<UserEntity> userOptional = getUserById(idUser);
        
        if(userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.setBalance(userEntity.getBalance().add(balance));
            userRepository.save(userEntity);
            
            AddBalanceResponse addBalanceResponse = 
                    AddBalanceResponse.builder()
                    .user(userEntity)
                    .message(IConstants.SUCCESS_MESSAGE_ADD_BALANCE)
                    .build();
            
            return addBalanceResponse;
        } else {
            throw new UserException("No se encontró el usuario solicitado para agregarle saldo");
        }
    }
    
    private void validateData(UserRequest userRequest) throws UserException {
        if(userRequest.getName().trim().isEmpty()) {
            throw new UserException("El nombre no puede ser nulo");
        }
        if(userRequest.getLastname().trim().isEmpty()) {
            throw new UserException("El apellido no puede ser nulo");
        }
    }

    @Override
    public BigDecimal getTotalSpendInActualMonth(Long id) {
        BigDecimal totalAmountSpent = userRepository.getTotalSpendInActualMonth(id);
        
        if(totalAmountSpent != null){
            return totalAmountSpent;
        } else {
            return new BigDecimal("0");
        }
    }
    
    @Override
    public List<UserEntity> getVipUsers() {
        return userRepository.getVipUsers();
    }
}
