package app.immobilisation.controller;

import app.immobilisation.model.Amortissement;
import app.immobilisation.model.Materiel;
import app.immobilisation.model.TableauAmortissement;
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
import java.lang.reflect.InvocationTargetException;
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

        Materiel Materiel=new Materiel();
        if (request.getParameter("article").equals(""))
        {
            Materiel.setArticle(null);
        }
        else {
            Materiel.setArticle(request.getParameter("article"));
        }
        if(request.getParameter("duree").equals(""))
        {
            Materiel.setDuree(null);
        }
        else
        {
            Materiel.setDuree(Integer.parseInt(request.getParameter("duree")));
        }
        if(request.getParameter("prix_achat").equals(""))
        {
            Materiel.setPrix_achat(null);
        }
        else {
            Materiel.setPrix_achat(Float.parseFloat(request.getParameter("prix_achat")));
        }
        if(request.getParameter("id_type").equals(""))
        {
            Materiel.setId_type(null);
        }
        else{
            Materiel.setId_type(Integer.parseInt(request.getParameter("id_type")));
        }


        Date date1=null;
        Date date2=null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strdate1=request.getParameter("date1");
        String strdate2=request.getParameter("date2");
        if(strdate1.equals("")==true)
        {
            try {
                date1=formatter.parse("1970-01-01");
                date2=formatter.parse("2050-01-01");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(strdate1);
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(strdate2);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {

            List val=modelService.finBetween(Materiel,"date_achat",date1,date2);
            float somme=0;
            for(int i=0;i<val.size();i++)
            {
                Materiel mat=(Materiel) val.get(i);
                somme+=mat.getPrix_achat();
            }
            model.addAttribute("listMateriaux",val);
            model.addAttribute("somme",somme);
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

    @RequestMapping(value = "/ListMateriaux")
    public String ListMateriaux(Map<String, Object> modelMap,HttpServletRequest request)
    {
        Materiel materiel=new Materiel();
        List val=modelService.findall(materiel);
        float somme=0;
        for(int i=0;i<val.size();i++)
        {
            Materiel mat=(Materiel) val.get(i);
            somme+=mat.getPrix_achat();
        }
        modelMap.put("listMateriaux",val);
        modelMap.put("somme",somme);
        return "ListMateriaux";
    }

    @RequestMapping(value = "/TableauAmort")
    public String TableauAmort(Map<String, Object> modelMap,HttpServletRequest request)
    {
        Materiel materiel=new Materiel();
        materiel.setId(Integer.parseInt(request.getParameter("id")));
        List val=null;
        try {
            val=modelService.findById(materiel);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Materiel mat=(Materiel) val.get(0);
        TableauAmortissement[] tableauAmortissements=mat.tableauAmortissements();
        modelMap.put("tableau",tableauAmortissements);
        return "TableauAmort";
    }


}
