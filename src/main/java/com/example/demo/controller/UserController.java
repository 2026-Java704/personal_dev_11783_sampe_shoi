package com.example.demo.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping({ "/", "/login" })
	public String loginForm(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "ユーザー名またはパスワードが違います。");
		}
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String name, @RequestParam String password, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Optional<User> user = userRepository.findByNameAndPassword(name, password);

		if (user.isPresent()) {
			session.setAttribute("loginUser", name);
			return "redirect:/tasks";
		} else {
			return "redirect:/login?error=true";
		}
	}

	@GetMapping("/users/new")
	public String createForm() {
		return "useradd";
	}

	@PostMapping("/users/add")
	public String register(@RequestParam String name, @RequestParam String password,
			Model model, RedirectAttributes redirectAttributes) {

		if (userRepository.findByName(name).isPresent()) {
			model.addAttribute("error", "そのユーザー名は既に存在します。");
			return "useradd";
		}

		User user = new User();
		user.setName(name);
		user.setPassword(password);
		userRepository.save(user);

		redirectAttributes.addFlashAttribute("success", "新規登録が完了しました。ログインしてください。");
		return "redirect:/login";
	}
}