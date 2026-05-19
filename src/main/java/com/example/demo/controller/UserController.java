package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	@GetMapping({ "/", "/login" })
	public String loginForm(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "ユーザー名またはパスワードが違います。");
		}
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String name, @RequestParam String password) {
		return "redirect:/tasks";
	}

	@GetMapping("/users/new")
	public String createForm() {
		return "useradd";
	}

	@PostMapping("/users/add")
	public String register(@RequestParam String name, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("success", "新規登録が完了しました。ログインしてください。");
		return "redirect:/login";
	}
}
