package app.immobilisation.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
public class Materiel extends BaseModel {

    @Column
    @NotNull
    private Float prix_achat;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_achat;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_service;

    @Column
    private Integer id_type;

    @Column
    private Integer duree;

    @Column
    @NotBlank
    private String article;

    @ManyToOne
    @JoinColumn(name="id_type", insertable = false,updatable = false)
    private Amortissement amortissement;

    public Amortissement getAmortissement() {
        return amortissement;
    }

    public void setAmortissement(Amortissement amortissement) {
        this.amortissement = amortissement;
    }

    public Float getPrix_achat() {
        return prix_achat;
    }

    public void setPrix_achat(Float prix_achat) {
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

    public Integer getId_type() {
        return id_type;
    }

    public void setId_type(Integer id_type) {
        this.id_type = id_type;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public long yearsDiff(String join_date, String leave_date)
    {
        // Create an instance of the SimpleDateFormat class
        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");

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

            return years_difference;

        }
        // Catch parse exception
        catch (ParseException excep) {
            excep.printStackTrace();
        }
        return year;
    }

    public float pourcentage()
    {
        long years=yearsDiff(this.date_service.toString(), LocalDate.now().toString());
        if(years>duree)
        {
            return 100;
        }
        else
        {
            return years*100/this.getDuree();
        }
    }

    public TableauAmortissement[] tableauAmortissements()
    {
        int taille =duree;
        if(this.getDate_service().getMonth()!=0)
        {
            taille++;
        }
        TableauAmortissement[] val=new TableauAmortissement[taille];
        for (int i = 0; i <val.length ; i++) {
            val[i]=new TableauAmortissement();
        }
        double exercice= this.prix_achat/this.duree;
        double taux=100/(double)this.duree/100.0d;
        double prorata1=0;
        double prorata2=0;
        boolean prorat=false;
        if(this.getDate_service().getMonth()!=0)
        {
            prorat=true;
            int nombremois=12+1-(this.getDate_service().getMonth()+1);
            int mois=12-nombremois;
            double n =(double)(nombremois/12.0d);
            prorata1= (double) ((double)this.prix_achat * (double)taux * (double)n);
            prorata2= (double) ((double)this.prix_achat *  (double)taux * (double)(mois/12.0d) );
        }



        val[0].setAnnee(this.getDate_service().getYear()+1900);
        val[0].setPA(this.getPrix_achat());
        val[0].setAnterieur(0);
        if(this.getDate_service().getMonth()!=0)
        {
            BigDecimal salary = new BigDecimal(prorata1);
            BigDecimal salary2 = salary.setScale(2, RoundingMode.HALF_EVEN);
            val[0].setExercice(salary2.doubleValue());
        }
        else
        {
            val[0].setExercice(exercice);
        }
        BigDecimal cumul = new BigDecimal(val[0].getExercice());
        BigDecimal cumul2 = cumul.setScale(2, RoundingMode.HALF_EVEN);
        val[0].setCumul(cumul2.doubleValue());

        BigDecimal vnc = new BigDecimal(val[0].getPA()-val[0].getCumul());
        BigDecimal vnc2 = vnc.setScale(2, RoundingMode.HALF_EVEN);
        val[0].setVNC(vnc2.doubleValue());

        val[0].setArticle(this.article);
        val[0].setDate_service(this.date_service);

        for (int i = 1; i <val.length ; i++) {
            val[i].setAnnee(val[i-1].getAnnee()+1);
            val[i].setPA(this.getPrix_achat());

            BigDecimal ant = new BigDecimal(val[i-1].getCumul());
            BigDecimal ant2 = ant.setScale(2, RoundingMode.HALF_EVEN);
            val[i].setAnterieur(ant2.doubleValue() );

            if(i==val.length-1 && prorat==true)
            {

                BigDecimal salary = new BigDecimal(prorata2);
                BigDecimal salary2 = salary.setScale(2, RoundingMode.HALF_UP);
                val[i].setExercice(salary2.doubleValue());
            }
            else if(i==val.length-1 && prorat==false)
            {
                val[i].setExercice(exercice);
            }
            else
            {
                val[i].setExercice(exercice);
            }

            BigDecimal cumul3 = new BigDecimal(val[i].getAnterieur()+val[i].getExercice());
            BigDecimal cumul4 = null;
            if(i==val.length-1 && prorat==true)
            {
                val[i].setCumul(this.prix_achat);
                val[i].setVNC(0);
            }
            else
            {
                cumul4 = cumul3.setScale(2, RoundingMode.HALF_EVEN);
                val[i].setCumul(cumul4.doubleValue());
                BigDecimal vnc3 = new BigDecimal(this.prix_achat-val[i].getCumul());
                BigDecimal vnc4 = vnc3.setScale(2, RoundingMode.HALF_UP);
                val[i].setVNC(vnc4.doubleValue());
            }

            val[i].setArticle(this.article);
            val[i].setDate_service(this.date_service);


        }
        return val;
    }

    public TableauAmortissement Amortissement(int annee)
    {
        TableauAmortissement[] table=tableauAmortissements();
        for (int i = 0; i <table.length ; i++) {
            if(table[i].getAnnee()==annee)
            {
                return table[i];
            }
        }
        return new TableauAmortissement();
    }
}
