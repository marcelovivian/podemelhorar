package sistema.com.br.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class GalleriaBean {

	private List<String> img;

	@PostConstruct
	public void init() {
		img = new ArrayList<String>();

		for (int i = 1; i <= 12; i++) {
			img.add("/resources/img/galleria/" + i + ".png");
		}

	}

	public List<String> getImages() {
		return img;
	}

	public List<String> getImg() {
		return img;
	}

	public void setImg(List<String> img) {
		this.img = img;
	}
	
	

}
