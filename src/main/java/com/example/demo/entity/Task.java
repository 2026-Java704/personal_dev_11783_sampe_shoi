package com.example.demo.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_id")
	private Integer categoryId;

	private String title;

	private String progress = "未着手";

	private LocalDate date;

	private LocalDate closingDate;

	private Integer time;

	private String memo;

	@Column(name = "username")
	private String username;

	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	private String priority = "中";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public String getFormattedCreatedAt() {
		if (this.createdAt == null) {
			return "";
		}
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return this.createdAt.format(formatter);
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getRemainingTime() {
		if ("完了".equals(this.progress)) {
			return 0;
		}

		if (this.progress == null || "未着手".equals(this.progress)) {
			return this.time != null ? this.time : 0;
		}

		if ("進行中".equals(this.progress)) {
			if (this.startedAt == null || this.time == null) {
				return this.time != null ? this.time : 0;
			}

			long elapsedMinutes = Duration.between(this.startedAt, LocalDateTime.now()).toMinutes();

			int remaining = this.time - (int) elapsedMinutes;

			return Math.max(remaining, 0);
		}

		return this.time != null ? this.time : 0;
	}
}