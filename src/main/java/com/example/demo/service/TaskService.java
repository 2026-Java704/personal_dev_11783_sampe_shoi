package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;

	public List<Task> getAllTasks(String username, Integer categoryId) {
		if (categoryId != null) {
			return taskRepository.findByUsernameAndCategoryIdOrderByClosingDateAsc(username, categoryId);
		}
		return taskRepository.findByUsernameOrderByClosingDateAsc(username);
	}

	public int calculateRemainingTime(Task task) {
		return task.getRemainingTime();
	}

	public int calculateTotalRemainingTime(List<Task> tasks) {
		return tasks.stream().mapToInt(Task::getRemainingTime).sum();
	}
}