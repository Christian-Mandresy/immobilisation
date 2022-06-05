package app.immobilisation.controller;

import app.immobilisation.model.Amortissement;
import app.immobilisation.model.Materiel;
import app.immobilisation.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
public class MaterielController {

    private ModelService modelService;

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @RequestMapping(value = "/formAjout")
    public String FormAjout(Map<String, Object> modelMap)
    {
        modelMap.put("Materiel",new Materiel());
        modelMap.put("type",modelService.findall(new Amortissement()));
        return "Saisie";
    }

    @RequestMapping(value = "/formRecherche")
    public String FormRecherche(Map<String, Object> modelMap)
    {
        modelMap.put("Materiel",new Materiel());
        modelMap.put("type",modelService.findall(new Amortissement()));
        return "Recherche";
    }

    @RequestMapping(value = "/AjoutMateriel",method = RequestMethod.POST)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public String InsertMateriel(@ModelAttribute("Materiel") @Validated Materiel materiel,
                                 BindingResult bindingResult, Model model, HttpServletRequest request)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("Materiel",new Materiel());
            model.addAttribute("type",modelService.findall(new Amortissement()));
            return "Saisie";
        }
        else
        {
            Materiel Materiel=new Materiel();
            Materiel.setArticle(request.getParameter("article"));
            Materiel.setDuree(Integer.parseInt(request.getParameter("duree")));
            Materiel.setPrix_achat(Float.parseFloat(request.getParameter("prix_achat")));
            Materiel.setId_type(Integer.parseInt(request.getParameter("id_type")));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                Materiel.setDate_achat(new SimpleDateFormat("yyyy-MM-dd")
                        .parse(request.getParameter("date_achat")));
                Materiel.setDate_service(new SimpleDateFormat("yyyy-MM-dd")
                        .parse(request.getParameter("date_service")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                modelService.save(Materiel);
                model.addAttribute("success","Materiel ajouter avec success");
                model.addAttribute("Materiel",new Materiel());
                model.addAttribute("type",modelService.findall(new Amortissement()));
                return "Saisie";
            }catch (Exception e)
            {
                e.printStackTrace();
                model.addAttribute("error","une erreur est survenue");
                model.addAttribute("Materiel",new Materiel());
                model.addAttribute("type",modelService.findall(new Amortissement()));
                return "Saisie";
            }
        }
    }

    @RequestMapping(value = "/RechercheMateriel",method = RequestMethod.POST)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public String RechercheMateriel(@ModelAttribute("Materiel") @Validated Materiel materiel,
                                 BindingResult bindingResult, Model model, HttpServletRequest request)
    {
        if(bindingResult.hasErrors()){
            model.addAttribute("Materiel",new Materiel());
            model.addAttribute("type",modelService.findall(new Amortissement()));
            return "Recherche";
        }
        else
        {
            Materiel Materiel=new Materiel();
            Materiel.setArticle(request.getParameter("article"));
            Materiel.setDuree(Integer.parseInt(request.getParameter("duree")));
            Materiel.setPrix_achat(Float.parseFloat(request.getParameter("prix_achat")));
            Materiel.setId_type(Integer.parseInt(request.getParameter("id_type")));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Date date1=new Date();
            Date date2=new Date();
            try {
                date1=new SimpleDateFormat("yyyy-MM-dd")
                        .parse(request.getParameter("date1"));
                date2=new SimpleDateFormat("yyyy-MM-dd")
                        .parse(request.getParameter("date2"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                List val=modelService.finBetween(Materiel,"date_achat",date1,date2);
                model.addAttribute("listMateriaux",val);
                return "ListMateriaux";
            }catch (Exception e)
            {
                e.printStackTrace();
                model.addAttribute("error","une erreur est survenue");
                model.addAttribute("Materiel",new Materiel());
                model.addAttribute("type",modelService.findall(new Amortissement()));
                return "Recherche";
            }
        }
    }
}
