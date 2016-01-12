

public class FavInfo {

	private String title;
	private String url;

	public FavInfo(String t, String u) {
		title = t;
		url = u;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String toString() {
		return title;
	}
}