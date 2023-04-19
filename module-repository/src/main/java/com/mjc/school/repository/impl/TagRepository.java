package com.mjc.school.repository.impl;

import com.mjc.school.repository.model.impl.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagModel, Long> {
}
