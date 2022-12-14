package com.example.FactorITtest.DTO.Response;

import com.example.FactorITtest.Entities.UserEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Xavier Pocchettino
 */
@Data
@Builder
public class UsersResponse {
    List<UserEntity> listUsers;
    Long totalUsers;
}
