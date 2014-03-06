package sistema.com.br.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagemUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage imagem = null;
		try {
			imagem = ImageIO.read(new File("D:/IMG_4548.JPG"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		BufferedImage buffered = redimensionar(imagem, 400f);
		
		try {
			ImageIO.write(buffered, "JPG", new File("D:/nweImg5.png")); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static BufferedImage redimensionar(BufferedImage img, float tamanho){
		
		float fator  = 0.5f;
		int largura = img.getWidth();
		int altura = img.getHeight();
		if(largura>=altura){
			fator = tamanho / largura;
		}else{
			fator = tamanho / altura;
		}
		
		//System.out.println("fator = "+fator);
		
		int scaleX = (int) (img.getWidth() * fator);
		int scaleY = (int) (img.getHeight() * fator);
		Image image = img.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage(scaleX, scaleY, BufferedImage.TYPE_INT_RGB);
		buffered.getGraphics().drawImage(image, 0, 0 , null);
	
		return buffered;
		
	}

}
