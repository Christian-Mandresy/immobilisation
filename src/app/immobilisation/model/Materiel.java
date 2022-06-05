package app.immobilisation.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
public class Materiel extends BaseModel {

    @Column
    @NotNull
    private float prix_achat;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_achat;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_service;

    @Column
    private int id_type;

    @Column
    private int duree;

    @Column
    @NotBlank
    private String article;

    @ManyToOne
    @JoinColumn(name="id", insertable = false,updatable = false)
    private Amortissement amortissement;

    public Amortissement getAmortissement() {
        return amortissement;
    }

    public void setAmortissement(Amortissement amortissement) {
        this.amortissement = amortissement;
    }

    public float getPrix_achat() {
        return prix_achat;
    }

    public void setPrix_achat(float prix_achat) {
        this.prix_achat = prix_achat;
    }

    public Date getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(Date date_achat) {
        this.date_achat = date_achat;
    }

    public Date getDate_service() {
        return date_service;
    }

    public void setDate_service(Date date_service) {
        this.date_service = date_service;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int yearsDiff(String join_date, String leave_date)
    {
        // Create an instance of the SimpleDateFormat class
        SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        int year=0;
        // In the try block, we will try to find the difference
        try {

            // Use parse method to get date object of both dates
            Date date1 = obj.parse(join_date);
            Date date2 = obj.parse(leave_date);

            // Calucalte time difference in milliseconds
            long time_difference = date2.getTime() - date1.getTime();

            // Calculate time difference in years using TimeUnit class
            long years_difference = TimeUnit.MILLISECONDS.toDays(time_difference) / 365l;

            return (int)years_difference;

        }
        // Catch parse exception
        catch (ParseException excep) {
            excep.printStackTrace();
        }
        return year;
    }

    public float pourcentage()
    {
        int years=yearsDiff(this.date_service.toString(), LocalDate.now().toString());
        if(years>duree)
        {
            return 100;
        }
        else
        {
            return years*100/this.getDuree();
        }
    }
}
