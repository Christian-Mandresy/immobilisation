/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.immobilisation.model;

import app.immobilisation.model.Materiel;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Brown Dohn
 */
public class PDFGenerator {
    // List to hold all Students
	private List<Materiel> studentList;

	private float somme;

	public float getSomme() {
		return somme;
	}

	public void setSomme(float somme) {
		this.somme = somme;
	}

	public List<Materiel> getStudentList() {
        return studentList;
    }


    public void setStudentList(List<Materiel> studentList) {
        this.studentList = studentList;
    }

	public void generate(HttpServletResponse response) throws DocumentException, IOException {

		// Creating the Object of Document
		Document document = new Document(PageSize.A4);

		// Getting instance of PdfWriter
		PdfWriter.getInstance(document, response.getOutputStream());

		// Opening the created document to modify it
		document.open();

		// Creating font
		// Setting font style and size
		Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTiltle.setSize(20);

		// Creating paragraph
		Paragraph paragraph = new Paragraph("List des trajets", fontTiltle);

		// Aligning the paragraph in document
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		// Adding the created paragraph in document
		document.add(paragraph);

		// Creating a table of 3 columns
		PdfPTable table = new PdfPTable(7);

		// Setting width of table, its columns and spacing
		table.setWidthPercentage(100f);
		table.setWidths(new int[] { 3, 3, 3,3,3,3,3 });
		table.setSpacingBefore(5);

		// Create Table Cells for table header
		PdfPCell cell = new PdfPCell();

		// Setting the background color and padding
		cell.setBackgroundColor(CMYKColor.DARK_GRAY);
		cell.setPadding(5);

		// Creating font
		// Setting font style and size
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.WHITE);

		// Adding headings in the created table cell/ header
		// Adding Cell to table
		cell.setPhrase(new Phrase("Article", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Date de service", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Prix d'achat", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Duree", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Amortissement", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Date d' achat", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Total", font));
		table.addCell(cell);

		// Iterating over the list of students
		for (Materiel student : studentList) {
			// Adding student name
			table.addCell(student.getArticle());
			// Adding student section
			table.addCell(student.getDate_service().toString());
			table.addCell(String.valueOf(student.getPrix_achat()));
			table.addCell(student.getDuree().toString());
			table.addCell(student.getAmortissement().getNom());
			table.addCell(student.getDate_achat().toString());
			table.addCell(String.valueOf(this.getSomme()));
		}
		// Adding the created table to document
		document.add(table);

		// Closing the document
		document.close();

	}
}
