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

	public List<Task> getAllTasks(String username, Integer categoryId, String sort) {
		List<Task> tasks;
		if (categoryId != null) {
			tasks = taskRepository.findByUsernameAndCategoryIdOrderByClosingDateAsc(username, categoryId);
		} else {
			tasks = taskRepository.findByUsernameOrderByClosingDateAsc(username);
		}

		if (sort == null || sort.isEmpty()) {
			return tasks;
		}

		List<String> priorityOrder = List.of("高", "中", "低");
		List<String> progressOrder = List.of("未着手", "進行中", "完了");

		tasks.sort((t1, t2) -> {
			switch (sort) {
			case "priority":
				int p1 = priorityOrder.indexOf(t1.getPriority() != null ? t1.getPriority() : "中");
				int p2 = priorityOrder.indexOf(t2.getPriority() != null ? t2.getPriority() : "中");
				return Integer.compare(p1 == -1 ? 99 : p1, p2 == -1 ? 99 : p2);

			case "progress":
				int pr1 = progressOrder.indexOf(t1.getProgress() != null ? t1.getProgress() : "未着手");
				int pr2 = progressOrder.indexOf(t2.getProgress() != null ? t2.getProgress() : "未着手");
				return Integer.compare(pr1 == -1 ? 99 : pr1, pr2 == -1 ? 99 : pr2);

			case "createdAt":
				if (t1.getCreatedAt() == null)
					return 1;
				if (t2.getCreatedAt() == null)
					return -1;
				return t1.getCreatedAt().compareTo(t2.getCreatedAt());

			case "startedAt":
				if (t1.getStartedAt() == null && t2.getStartedAt() == null) {
					if (t1.getCreatedAt() == null)
						return 1;
					if (t2.getCreatedAt() == null)
						return -1;
					return t1.getCreatedAt().compareTo(t2.getCreatedAt());
				}
				if (t1.getStartedAt() == null)
					return 1;
				if (t2.getStartedAt() == null)
					return -1;

				int dateCompare = t1.getStartedAt().compareTo(t2.getStartedAt());
				if (dateCompare == 0) {
					if (t1.getCreatedAt() == null)
						return 1;
					if (t2.getCreatedAt() == null)
						return -1;
					return t1.getCreatedAt().compareTo(t2.getCreatedAt());
				}
				return dateCompare;

			case "closingDate":
			default:
				if (t1.getClosingDate() == null)
					return 1;
				if (t2.getClosingDate() == null)
					return -1;
				return t1.getClosingDate().compareTo(t2.getClosingDate());
			}
		});

		return tasks;
	}

	public int calculateRemainingTime(Task task) {
		return task.getRemainingTime();
	}

	public int calculateTotalRemainingTime(List<Task> tasks) {
		return tasks.stream().mapToInt(Task::getRemainingTime).sum();
	}
}