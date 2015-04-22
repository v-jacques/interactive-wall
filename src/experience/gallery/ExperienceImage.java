package experience.gallery;

import java.util.Date;

import javafx.scene.image.Image;

public class ExperienceImage extends Image {
	String title;
	String author;
	String medium;
	Date date;

	public ExperienceImage(String path, double width, double height,
			boolean preserveRatio, boolean smooth) {
		super(path, width, height, preserveRatio, smooth);
	}
	
	public ExperienceImage(String path, double width, double height,
			boolean preserveRatio, boolean smooth, String title, String author, String medium, Date date) {
		super(path, width, height, preserveRatio, smooth);
		this.title = title;
		this.author = author;
		this.medium = medium;
		this.date = date;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
