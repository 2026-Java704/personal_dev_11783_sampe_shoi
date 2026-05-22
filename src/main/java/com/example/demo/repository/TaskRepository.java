package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByCategoryId(Integer categoryId);

	List<Task> findByUsernameOrderByClosingDateAsc(String username);

	List<Task> findByUsernameAndCategoryIdOrderByClosingDateAsc(String username, Integer categoryId);

}