package app.immobilisation.model;

public class TableauAmortissement {
    private int Annee;
    private double PA;
    private double Anterieur;
    private double Exercice;
    private double Cumul;
    private double VNC;

    public int getAnnee() {
        return Annee;
    }

    public void setAnnee(int annee) {
        Annee = annee;
    }

    public double getPA() {
        return PA;
    }

    public void setPA(double PA) {
        this.PA = PA;
    }

    public double getAnterieur() {
        return Anterieur;
    }

    public void setAnterieur(double anterieur) {
        Anterieur = anterieur;
    }

    public double getExercice() {
        return Exercice;
    }

    public void setExercice(double exercice) {
        Exercice = exercice;
    }

    public double getCumul() {
        return Cumul;
    }

    public void setCumul(double cumul) {
        Cumul = cumul;
    }

    public double getVNC() {
        return VNC;
    }

    public void setVNC(double VNC) {
        this.VNC = VNC;
    }
}
