package experience.gallery.art;

import javafx.scene.image.Image;

public class ExperienceImage extends Image {
	private String path;
	private String title;
	private String author;
	private String medium;
	private String date;

	public ExperienceImage(String path, double width, double height,
			boolean preserveRatio, boolean smooth) {
		super(path, width, height, preserveRatio, smooth);
	}

	public ExperienceImage(String path, double width, double height,
			boolean preserveRatio, boolean smooth, String title, String author,
			String medium, String date) {
		super(path, width, height, preserveRatio, smooth);
		this.path = path;
		this.title = title;
		this.author = author;
		this.medium = medium;
		this.date = date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
