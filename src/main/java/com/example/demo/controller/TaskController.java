package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService;

	@GetMapping
	public String index(
			@RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false, defaultValue = "closingDate") String sort,
			Model model,
			HttpSession session) {
		String loginUser = (String) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		List<Task> tasks = taskService.getAllTasks(loginUser, categoryId, sort);
		int totalRemainingTime = taskService.calculateTotalRemainingTime(tasks);

		model.addAttribute("tasks", tasks);
		model.addAttribute("totalTime", totalRemainingTime);
		model.addAttribute("taskService", taskService);
		model.addAttribute("username", loginUser);
		model.addAttribute("currentCategoryId", categoryId);
		model.addAttribute("currentSort", sort);

		return "tasklist";
	}

	@GetMapping("/create")
	public String createForm() {
		return "taskadd";
	}

	@PostMapping("/create")
	public String registerTask(Task task, HttpSession session, RedirectAttributes redirectAttributes) {
		String loginUser = (String) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}
		if (task.getTitle() != null && task.getTitle().length() > 30) {
			redirectAttributes.addFlashAttribute("error", "タイトルは30文字以内で入力してください。");
			return "redirect:/tasks/create";
		}

		if (task.getDate() != null && task.getDate().isBefore(LocalDate.now())) {
			redirectAttributes.addFlashAttribute("error", "タスク開始日には、今日以降の日付を指定してください。");
			return "redirect:/tasks/create";
		}

		if (task.getDate() != null && task.getClosingDate() != null) {
			if (task.getClosingDate().isBefore(task.getDate())) {
				redirectAttributes.addFlashAttribute("error", "期限には、タスク開始日以降の日付を指定してください。");
				return "redirect:/tasks/create";
			}
		}
		task.setUsername(loginUser);
		task.setProgress("未着手");
		task.setCreatedAt(java.time.LocalDateTime.now());
		taskRepository.save(task);
		return "redirect:/tasks";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model, HttpSession session) {
		String loginUser = (String) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		Task task = taskRepository.findById(id).orElseThrow();

		if (!loginUser.equals(task.getUsername())) {
			return "redirect:/tasks";
		}

		model.addAttribute("task", task);
		return "taskedit";
	}

	@PostMapping("/{id}/edit")
	public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask,
			RedirectAttributes redirectAttributes) {
		Task task = taskRepository.findById(id).orElseThrow();
		if (updatedTask.getTitle() != null && updatedTask.getTitle().length() > 30) {
			redirectAttributes.addFlashAttribute("error", "タイトルは30文字以内で入力してください。");
			return "redirect:/tasks/" + id + "/edit";
		}

		if (updatedTask.getDate() != null && updatedTask.getDate().isBefore(LocalDate.now())) {
			redirectAttributes.addFlashAttribute("error", "タスク開始日には、今日以降の日付を指定してください。");
			return "redirect:/tasks/" + id + "/edit";
		}

		if ("未着手".equals(task.getProgress()) && "進行中".equals(updatedTask.getProgress())) {
			task.setStartedAt(java.time.LocalDateTime.now());
		}
		if ("進行中".equals(task.getProgress()) && "未着手".equals(updatedTask.getProgress())) {
			task.setStartedAt(null);
		}

		task.setCategoryId(updatedTask.getCategoryId());
		task.setTitle(updatedTask.getTitle());
		task.setPriority(updatedTask.getPriority());
		task.setProgress(updatedTask.getProgress());
		task.setDate(updatedTask.getDate());
		task.setClosingDate(updatedTask.getClosingDate());
		task.setTime(updatedTask.getTime());
		task.setMemo(updatedTask.getMemo());
		taskRepository.save(task);
		return "redirect:/tasks";
	}

	@PostMapping("/delete")
	public String deleteTasks(@RequestParam(value = "task_ids", required = false) List<Long> taskIds) {
		if (taskIds != null) {
			taskRepository.deleteAllById(taskIds);
		}
		return "redirect:/tasks";
	}

	@PostMapping("/update-progress")
	public String updateProgress(@RequestParam(value = "task_ids", required = false) List<Long> taskIds) {
		if (taskIds != null) {
			List<Task> tasks = taskRepository.findAllById(taskIds);
			for (Task task : tasks) {
				if ("未着手".equals(task.getProgress())) {
					task.setProgress("進行中");
					task.setStartedAt(java.time.LocalDateTime.now());
				} else if ("進行中".equals(task.getProgress())) {
					task.setProgress("完了");
				}
			}
			taskRepository.saveAll(tasks);
		}
		return "redirect:/tasks";

	}
}
