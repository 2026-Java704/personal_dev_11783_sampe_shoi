package com.example.demo.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> getAllTasks(String username, Integer categoryId, String sortParam) {
		List<Task> tasks;
		Sort defaultSort = Sort.by(Sort.Direction.ASC, "closingDate");

		if (categoryId == null) {
			tasks = taskRepository.findByUsername(username, defaultSort);
		} else {
			tasks = taskRepository.findByUsernameAndCategoryId(username, categoryId, defaultSort);
		}

		if ("priority".equals(sortParam)) {
			tasks.sort((t1, t2) -> {
				int p1 = getPriorityValue(t1.getPriority());
				int p2 = getPriorityValue(t2.getPriority());
				return Integer.compare(p1, p2);
			});
		} else if ("progress".equals(sortParam)) {
			tasks.sort((t1, t2) -> {
				int pr1 = getProgressValue(t1.getProgress());
				int pr2 = getProgressValue(t2.getProgress());
				return Integer.compare(pr1, pr2);
			});
		} else if ("createdAt".equals(sortParam)) {
			tasks.sort(Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
		} else if ("startedAt".equals(sortParam)) {
			tasks.sort(Comparator.comparing(Task::getStartedAt, Comparator.nullsLast(Comparator.reverseOrder())));
		} else if ("closingDate".equals(sortParam)) {
			tasks.sort(Comparator.comparing(Task::getClosingDate, Comparator.nullsLast(Comparator.naturalOrder())));
		}

		return tasks;
	}

	private int getPriorityValue(String priority) {
		if ("高".equals(priority))
			return 1;
		if ("中".equals(priority))
			return 2;
		if ("低".equals(priority))
			return 3;
		return 4;
	}

	private int getProgressValue(String progress) {
		if ("未着手".equals(progress))
			return 1;
		if ("進行中".equals(progress))
			return 2;
		if ("完了".equals(progress))
			return 3;
		return 4;
	}

	public int calculateTotalRemainingTime(List<Task> tasks) {
		if (tasks == null) {
			return 0;
		}
		return tasks.stream()
				.mapToInt(Task::getRemainingTime)
				.sum();
	}
}