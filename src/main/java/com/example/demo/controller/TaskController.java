package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

@Controller
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	/*private TaskService taskService;
	
	// タスク一覧表示
	@GetMapping
	public String index(@RequestParam(required = false) Integer categoryId, Model model) {
	    List<Task> tasks = taskService.getAllTasks(categoryId);
	    
	    // 経過時間を反映した合計時間を計算して渡す
	    int totalRemainingTime = taskService.calculateTotalRemainingTime(tasks);
	    
	    model.addAttribute("tasks", tasks);
	    model.addAttribute("totalTime", totalRemainingTime);
	    model.addAttribute("taskService", taskService); // HTML側で個別残り時間を出す用
	    return "tasklist";
	}*/

	@GetMapping("/create")
	public String createForm() {
		return "taskadd";
	}

	@PostMapping("/create")
	public String registerTask(Task task) {
		taskRepository.save(task);
		return "redirect:/tasks";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		Task task = taskRepository.findById(id).orElseThrow();
		model.addAttribute("task", task);
		return "taskedit";
	}

	@PostMapping("/{id}/edit")
	public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
		Task task = taskRepository.findById(id).orElseThrow();
		task.setCategoryId(updatedTask.getCategoryId());
		task.setTitle(updatedTask.getTitle());
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
				} else if ("進行中".equals(task.getProgress())) {
					task.setProgress("完了");
				}
			}
			taskRepository.saveAll(tasks);
		}
		return "redirect:/tasks";
	}
}
