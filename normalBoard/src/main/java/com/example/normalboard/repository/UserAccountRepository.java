package com.example.normalboard.repository;

import com.example.normalboard.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount,String> {
}
