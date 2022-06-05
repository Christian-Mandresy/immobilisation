package app.immobilisation.model;

import javax.persistence.*;

@Entity
public class Amortissement extends BaseModel{

    @Column
    private String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
