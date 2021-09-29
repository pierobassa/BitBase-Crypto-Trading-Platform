package application.export;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import application.ClientView;
import application.model.ClientLogic;
import application.net.common.SupportedAssets;

@SuppressWarnings("deprecation")
public class PortfolioPDF {
	
	private static PortfolioPDF instance = null;

	public static PortfolioPDF getInstance(){
		if(instance == null)
			instance  = new PortfolioPDF();
		
		return instance ;
	}
	
	public boolean exportPortfolioPDF(){ //Creates a pdf with the holding amounts for each asset of the user and saves the pdf in the same source folder
		PdfWriter pdfWriter;
		try {
			pdfWriter = new PdfWriter(new File("Portfolio " + ClientLogic.getInstance().getUsername()+ ".pdf"));
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			
			File f = new File(this.getClass().getResource("/images/portfolioExportLogo.png").getFile());
			java.awt.Image image = ImageIO.read(f);
			ImageData imageData = ImageDataFactory.create(image, null);
		    Image pdfImg = new Image(imageData);
		    pdfImg.scaleToFit(200, 200);
		    pdfImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
			//System.out.println(f.getAbsolutePath());
			
			float col = 280f;
			float columnWidth[] = {col, col};
			Table table = new Table(columnWidth);
			
			table.setBackgroundColor(new DeviceRgb(149, 128, 255)).setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)));
			table.addCell(new Cell()
				.add(new Paragraph("Personal Portfolio"))
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setMarginTop(30f)
				.setMarginBottom(30f)
				.setFontSize(30f)
				.setBorder(Border.NO_BORDER)
			);
			
			table.addCell(new Cell()
				.add(new Paragraph("Username: " + ClientLogic.getInstance().getUsername()+"\n"+java.util.Calendar.getInstance().getTime()))
				.setTextAlignment(TextAlignment.RIGHT)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setMarginTop(30f)
				.setMarginBottom(30f)
				.setBorder(Border.NO_BORDER)
				.setMarginRight(10f)
			);
			
			float colWidth[] = {280, 280};
			Table portfolioAssets = new Table(colWidth);
			
			portfolioAssets.addCell(new Cell().add(new Paragraph("Asset name"))
					.setBackgroundColor(new DeviceRgb(0, 156, 246))
					.setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)))
					
			);
			portfolioAssets.addCell(new Cell().add(new Paragraph("Holdings"))
					.setBackgroundColor(new DeviceRgb(0, 156, 246))
					.setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)))
			);
			
			Map<String, String> assets = SupportedAssets.getInstance().getAssets();
			int cont = 0;
			for(var key : assets.keySet()) {
				if(cont % 2 == 0) {
					portfolioAssets.addCell(new Cell().add(new Paragraph(SupportedAssets.getInstance().getName(key)))
							.setBackgroundColor(new DeviceRgb(242, 254, 255)));
					portfolioAssets.addCell(new Cell().add(new Paragraph(ClientView.getInstance().getAmount(key)))
							.setBackgroundColor(new DeviceRgb(242, 254, 255)));
				}
				else {
					portfolioAssets.addCell(new Cell().add(new Paragraph(SupportedAssets.getInstance().getName(key)))
							.setBackgroundColor(new DeviceRgb(181, 246, 255)));
					portfolioAssets.addCell(new Cell().add(new Paragraph(ClientView.getInstance().getAmount(key)))
							.setBackgroundColor(new DeviceRgb(181, 246, 255)));
				}
				cont++;
			}
			
			SolidLine line = new SolidLine(1f);
			line.setColor(Color.convertRgbToCmyk(new DeviceRgb(0, 0, 0)));
			
			PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_BOLDITALIC);
			Cell infoCell = new Cell().add(new Paragraph("For real time portfolio value and prices, please log in to your Bitbase App.")
					.setFontColor(Color.convertRgbToCmyk(new DeviceRgb(0, 3, 88)))
					.setFontSize(15f)
					.setFont(font)
					.setHorizontalAlignment(HorizontalAlignment.CENTER)
					.setTextAlignment(TextAlignment.CENTER)
					.setMarginTop(20f)
			);

			LineSeparator separator = new LineSeparator(line);
	        separator.setBold();
	        separator.setWidth(500);
	        separator.setHorizontalAlignment(HorizontalAlignment.CENTER);
	        separator.setMarginTop(40f);
	        
	        Paragraph rights = new Paragraph("All rights reserved. Piero Bassa - " + java.util.Calendar.getInstance().getTime()).setFontSize(10f).setMarginTop(5f);
	        
			document.add(pdfImg);
			document.add(table);
			document.add(portfolioAssets);
			document.add(infoCell);
			document.add(separator);
			document.add(rights);
			document.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
}
