package app.immobilisation.controller;

import app.immobilisation.model.Amortissement;
import app.immobilisation.model.Materiel;
import app.immobilisation.model.PDFGenerator;
import app.immobilisation.model.TableauAmortissement;
import app.immobilisation.service.ModelService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
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

    private void instanciation(Materiel materiel,String[] list)
    {
        for (int i = 0; i <list.length ; i++) {
            if (list[i].equals("article="))
            {
                materiel.setArticle(null);
            }
            else if(i==0 && list[i].equals("article=")==false ){
                materiel.setArticle(list[i].replaceAll("article=",""));
            }
            if(list[i].equals("duree="))
            {
                materiel.setDuree(null);
            }
            else if(i==1 && list[i].equals("duree=")==false )
            {
                materiel.setDuree(Integer.parseInt(list[i].replaceAll("duree=","")));
            }
            if(list[i].equals("prixachat="))
            {
                materiel.setPrix_achat(null);
            }
            else if(i==2 && list[i].equals("prixachat=")==false )
            {
                materiel.setPrix_achat(Float.parseFloat(list[i].replaceAll("prixachat=","")));
            }
            if(list[i].equals("idtype="))
            {
                materiel.setId_type(null);
            }
            else if(i==3 && list[i].equals("idtype=")==false ){
                materiel.setId_type(Integer.parseInt(list[i].replaceAll("idtype=","")));
            }
        }
    }

    @RequestMapping(value = "/RechercheMateriel")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public String RechercheMateriel(@ModelAttribute("Materiel") @Validated Materiel materiel,
                                 BindingResult bindingResult, Model model, HttpServletRequest request)
    {

        Materiel Materiel=new Materiel();
        if(request.getParameter("pagin")!=null)
        {
            String critere=request.getParameter("critere");
            String[] list=critere.split(",");

            instanciation(Materiel,list);
        }
        else
        {
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
        }

        Date date1=null;
        Date date2=null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strdate1="";
        String strdate2="";
        if(request.getParameter("pagin")!=null)
        {
            String critere=request.getParameter("critere");
            String[] list=critere.split(",");
            if(list.length<5)
            {

            }
            else
            {
                strdate1=list[4].replaceAll("date1=","");
                strdate2=list[5].replaceAll("date2=","");
            }
        }
        else
        {
            strdate1=request.getParameter("date1");
            strdate2=request.getParameter("date2");
        }
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
            String critera="";
            int debut=1;
            if(request.getParameter("pagin")!=null)
            {
                debut=Integer.parseInt(request.getParameter("debut"));
                critera=request.getParameter("critere");
                if(debut>1)
                {
                    debut = (debut-1)*10 ;
                }
            }
            else
            {
                critera="article="+request.getParameter("article")+",duree="+request.getParameter("duree")+",prixachat="+request.getParameter("prix_achat")+",idtype="+request.getParameter("id_type")+",date1="+strdate1+",date2="+strdate2;
            }
            List val=modelService.finBetween(Materiel,"date_achat",date1,date2,debut,10);
            float somme=0;
            for(int i=0;i<val.size();i++)
            {
                Materiel mat=(Materiel) val.get(i);
                somme+=mat.getPrix_achat();
            }
            model.addAttribute("listMateriaux",val);
            model.addAttribute("somme",somme);
            model.addAttribute("critere",critera);
            model.addAttribute("debut",debut);
            model.addAttribute("count",(int)modelService.countFind(Materiel,"date_achat",date1,date2)/10);
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

    @GetMapping("/export")
    public void generatePdf(HttpServletResponse response,HttpServletRequest request) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        String critere=request.getParameter("critere");
        String[] list=critere.split(",");
        Materiel materiel=new Materiel();

        instanciation(materiel,list);

        Date date1=null;
        Date date2=null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strdate1="";
        String strdate2="";


        strdate1=list[4].replaceAll("date1=","");
        strdate2=list[5].replaceAll("date2=","");


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

        List<Materiel> studentList = null;
        float somme=0;

        try {
            String critera="";
            int debut=1;

            debut=Integer.parseInt(request.getParameter("debut"));
            critera=request.getParameter("critere");
            if(debut>1)
            {
                debut = (debut-1)*10 ;
            }

            studentList=modelService.finBetween(materiel,"date_achat",date1,date2,debut,10);
            for(int i=0;i<studentList.size();i++)
            {
                Materiel mat=(Materiel) studentList.get(i);
                somme+=mat.getPrix_achat();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        PDFGenerator generator = new PDFGenerator();
        generator.setStudentList(studentList);
        generator.setSomme(somme);
        try {
            generator.generate(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
