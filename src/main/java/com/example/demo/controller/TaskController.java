package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {

	@GetMapping("/tasks")
	public String index(
			@RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) String sort,
			Model model) {

		return "tasks/index";
	}

	@GetMapping("/tasks/create")
	public String createForm() {
		return "tasks/create";
	}

	@PostMapping("/tasks/create")
	public String registerTask(
			@RequestParam Integer categoryId,
			@RequestParam String title,
			@RequestParam String closing_date,
			@RequestParam String progress,
			@RequestParam String memo,
			@RequestParam String time,
			@RequestParam String date) {

		return "redirect:/tasks";
	}

	@GetMapping("/tasks/{id}/edit")
	public String editForm(@PathVariable("id") Long id, Model model) {

		return "tasks/edit";
	}

	@GetMapping("/tasks/{id}/delete")
	public String deletePopup(@PathVariable("id") Long id, Model model) {
		model.addAttribute("taskId", id);
		return "tasks/delete_popup";
	}

	@PostMapping("/tasks/{id}/delete")
	public String deleteAction(
			@PathVariable("id") Long id,
			@RequestParam(value = "action", required = false) String action) {

		if ("cancel".equals(action)) {
			return "redirect:/tasks";
		}

		return "redirect:/tasks";
	}

	@GetMapping("/logout")
	public String logoutPopup() {
		return "logout_popup";
	}

	@PostMapping("/logout")
	public String logoutAction(@RequestParam(value = "action", required = false) String action) {

		if ("cancel".equals(action)) {
			return "redirect:/tasks";
		}

		return "redirect:/login";
	}
}
