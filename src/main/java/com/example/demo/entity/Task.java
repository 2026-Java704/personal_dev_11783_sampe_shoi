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

	private LocalDateTime createdAt = LocalDateTime.now();

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getRemainingTime() {
		if ("完了".equals(this.progress)) {
			return 0;
		}

		if (this.progress == null || "未着手".equals(this.progress)) {
			return this.time;
		}

		if (this.date != null) {
			LocalDateTime startDateTime = this.date.atStartOfDay();
			if (LocalDateTime.now().isBefore(startDateTime)) {
				return this.time;
			}
		}

		if (this.createdAt == null) {
			return this.time;
		}

		long elapsedMinutes = Duration.between(this.createdAt, LocalDateTime.now()).toMinutes();
		int remaining = this.time - (int) elapsedMinutes;
		return Math.max(remaining, 0);
	}
}
