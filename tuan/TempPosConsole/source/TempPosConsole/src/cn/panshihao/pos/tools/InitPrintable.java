package cn.panshihao.pos.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.UnsupportedEncodingException;

import com.swetake.util.Qrcode;

//初始化Printable
public class InitPrintable implements Printable{
	
	public String content = "";
	public String imageContent = "";
	
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		// TODO Auto-generated method stub
		
		double x = pageFormat.getImageableX();
		double y = pageFormat.getImageableY();
		Graphics2D graph = (Graphics2D) graphics;
		graph.setColor(Color.BLACK);
		Font font = new Font("宋体", Font.PLAIN, 9);
		graph.setFont(font); 
		
		/********************************/
		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeErrorCorrect('M');
		qrcode.setQrcodeEncodeMode('B');
		qrcode.setQrcodeVersion(7);
		String QrcodeString = imageContent;
		
		byte[] byteImage = null;
		
		try {
			byteImage = QrcodeString.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			PosLogger.log.error(e.getMessage());
		}

		BufferedImage bufferedIamge = new BufferedImage(98, 98,BufferedImage.TYPE_BYTE_BINARY);
		
		Graphics2D imageGrap = bufferedIamge.createGraphics();
		
		imageGrap.setBackground(Color.WHITE);
		imageGrap.clearRect(0, 0, 98, 98);
		imageGrap.setColor(Color.BLACK);

		// 限制最大字节数为120
		if (byteImage.length > 0 && byteImage.length < 120) {
			boolean[][] s = qrcode.calQrcode(byteImage);
			for (int i = 0; i < s.length; i++) {
				for (int j = 0; j < s.length; j++) {
					if (s[j][i]) {
						imageGrap.fillRect(j * 2 + 3, i * 2 + 3, 2, 2);
					}
				}
			}
		}
		
		imageGrap.dispose();
		bufferedIamge.flush();

		/********************************/
		graph.drawImage(bufferedIamge,null,(int)x + 20,(int)y + 2);
		graph.drawString(content, (int)x + 60, (int)y + 120);
		graph.drawLine((int) x, (int)y + 130, 200, (int)y + 130);
		
		return 0;
	}
	
	
	
}
