package com.aryanshu.code.repository;

import com.aryanshu.code.dto.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<UserSession, String> {
}
