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
@Table(name = "tasks") // データベース上のテーブル名を「tasks」に指定
public class Task {

	@Id // このフィールドがテーブルの主キー（Primary Key：データを一意に識別するID）であることを指定
	@GeneratedValue(strategy = GenerationType.IDENTITY) // IDをデータベース側で自動連番（1, 2, 3...）に設定
	private Long id; // タスクの一意なIDを保存する変数

	@Column(name = "category_id") // データベースのカラム名を「category_id」にマッピング
	private Integer categoryId; // カテゴリID（1:日常、2:仕事）を保存する変数

	private String title; // タスクのタイトルを保存する変数

	private String progress = "未着手"; // 進捗状況（初期値は「未着手」、他は「進行中」「完了」）を保存する変数

	private LocalDate date; // タスクの開始日（年・月・日）を保存する変数

	private LocalDate closingDate; // タスクの期限（年・月・日）を保存する変数

	private Integer time; // 予想所要時間（分単位）を保存する変数

	private String memo; // タスクのメモ（詳細説明）を保存する変数

	@Column(name = "username") // データベースのカラム名を「username」にマッピング
	private String username; // ★追加：このタスクが「誰のものか」を判別するためのユーザー名を保存する変数

	private LocalDateTime createdAt = LocalDateTime.now(); // タスクが作られた日時（初期値は現在時刻）を保存する変数

	// --- 以下、各変数を安全に読み書きするための ゲッター（get）と セッター（set） ---

	public Long getId() {
		return id; // IDを取得する
	}

	public void setId(Long id) {
		this.id = id; // IDを設定する
	}

	public Integer getCategoryId() {
		return categoryId; // カテゴリIDを取得する
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId; // カテゴリIDを設定する
	}

	public String getTitle() {
		return title; // タイトルを取得する
	}

	public void setTitle(String title) {
		this.title = title; // タイトルを設定する
	}

	public String getProgress() {
		return progress; // 進捗状況を取得する
	}

	public void setProgress(String progress) {
		this.progress = progress; // 進捗状況を設定する
	}

	public LocalDate getDate() {
		return date; // 開始日を取得する
	}

	public void setDate(LocalDate date) {
		this.date = date; // 開始日を設定する
	}

	public LocalDate getClosingDate() {
		return closingDate; // 期限を取得する
	}

	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate; // 期限を設定する
	}

	public Integer getTime() {
		return time; // 予想所要時間を取得する
	}

	public void setTime(Integer time) {
		this.time = time; // 予想所要時間を設定する
	}

	public String getMemo() {
		return memo; // メモを取得する
	}

	public void setMemo(String memo) {
		this.memo = memo; // メモを設定する
	}

	public String getUsername() {
		return username; // ★追加：ユーザー名を取得する
	}

	public void setUsername(String username) {
		this.username = username; // ★追加：ユーザー名を設定する
	}

	public LocalDateTime getCreatedAt() {
		return createdAt; // 作成日時を取得する
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt; // 作成日時を設定する
	}

	// --- ここからビジネスロジック（残り時間の計算処理） ---

	public int getRemainingTime() {
		// 進捗が「完了」の場合は、残りの作業時間は一律 0 分とする
		if ("完了".equals(this.progress)) {
			return 0;
		}

		// 進捗が「未着手」または「設定なし」の場合は、まだ全く手を付けていないので初期の予想時間をそのまま返す
		if (this.progress == null || "未着手".equals(this.progress)) {
			return this.time;
		}

		// 進捗が「進行中」の場合の計算
		if (this.date != null) {
			LocalDateTime startDateTime = this.date.atStartOfDay(); // 開始日の 00:00 の時刻を作成
			// もし現在時刻がタスク開始日よりも前（未来のタスク）なら、まだ時間が経過していないので初期の予想時間を返す
			if (LocalDateTime.now().isBefore(startDateTime)) {
				return this.time;
			}
		}

		// 作成日時が入っていない（エラー防止）場合は、初期の予想時間を返す
		if (this.createdAt == null) {
			return this.time;
		}

		// 【進行中の計算】タスク作成時から現在までに「何分経過したか」を計算する
		long elapsedMinutes = Duration.between(this.createdAt, LocalDateTime.now()).toMinutes();

		// 初期予想時間から経過した分数を引き算して、残り時間を割り出す
		int remaining = this.time - (int) elapsedMinutes;

		// 残り時間がマイナスになってしまうのを防ぐ（最低でも 0 分とする）
		return Math.max(remaining, 0);
	}
}