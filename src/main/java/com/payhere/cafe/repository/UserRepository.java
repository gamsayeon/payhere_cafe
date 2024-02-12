package com.payhere.cafe.repository;

import com.payhere.cafe.model.User;
import com.payhere.cafe.projection.UserIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumberAndPassword(String phoneNumber, String password);

    UserIdProjection findUserIdProjectionByPhoneNumber(String phoneNumber);
}
