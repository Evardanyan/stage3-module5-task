package com.mjc.school.repository.impl;

import com.mjc.school.repository.model.impl.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Long> {

}
