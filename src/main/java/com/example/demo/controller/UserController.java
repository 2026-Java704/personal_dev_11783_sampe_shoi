package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	@GetMapping("/users/new")
	public String createForm() {
		return "users/new";
	}

	@PostMapping("/users/add")
	public String register(
			@RequestParam String name,
			@RequestParam String password,
			@RequestParam("password_confirm") String passwordConfirm,
			RedirectAttributes redirectAttributes) {

		return "redirect:/login";
	}

	@GetMapping({ "/", "/login" })
	public String loginForm(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "ユーザー名またはパスワードが違います。");
		}
		return "login";
	}

	@PostMapping("/login")
	public String login(
			@RequestParam String name,
			@RequestParam String password) {

		// TODO: 認証ロジックの実装

		return "redirect:/tasks";
	}
}
