package semillero.ecosistema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.repository.ImageRepository;
import semillero.ecosistema.repository.ProviderRepository;
import semillero.ecosistema.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailWeeklyService {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Scheduled(cron = "0 0 12 ? * MON")
    public void enviarEmailSemanal() {
        List<UserEntity> usuarios = userRepository.findAll();
        List<String> emails = new ArrayList<>();
        for (UserEntity usuario : usuarios) {
            if (usuario != null && usuario.getEmail() != null) {
                emails.add(usuario.getEmail());
            }
        }
        List<ProviderEntity> providers = providerRepository.findAll();
        List<ProviderEntity> listProviders = new ArrayList<>();
        for(ProviderEntity provider : providers){
            if(provider != null && provider.getIsNew() == true){
                listProviders.add(provider);
                provider.setIsNew(false);
                providerRepository.save(provider);
            }
        }
        String provURL = "http://localhost:5173/proveedores"; //Cambiar por el url cuando el Front este deployado
        StringBuilder sb = new StringBuilder();
        for (ProviderEntity provider : providers) {
            sb.append("<div style=\" width: 152px;height: 236px; overflow: visible; background: #fafafa; cursor: pointer;\"");
            sb.append("onclick=\"window.location.href='"+ provURL +"'\">");
            sb.append("<div style=\"padding: 0px; padding-top: 8px\"> <img src=\"" + imageRepository.findByProviderId(provider.getId()).get(0).getImagenUrl() + "\" alt=\"image proveedor\" ");
            sb.append("style=\" width: 136px; height: 136px; margin: 0px 8px; border-radius: 8px; \" />");
            sb.append("<div style=\"max-width: 90px; border: 1px solid #2196f3; display: flex; justify-content: center; align-items: center; padding: 2px 8px; border-radius: 4px; box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25); background: #fafafa; position: relative; bottom: 155px; left: 47px;\">");
            sb.append("<span style=\" color: #2196f3; font-size: 13px; padding: 0px 8px\">"+ provider.getCategory().getNombre() +"</span></div>");
            sb.append(" <div style=\" position: relative; bottom: 22px; height: 60px; margin-top: 6px;\">");
            sb.append("<span style=\"white-space: nowrap; text-overflow: ellipsis; overflow: hidden; display: block; font-size: 16px; margin: 0px 8px; font-weight: 600\">"+provider.getName()+"</span>");
            sb.append("<span style=\"display: block; font-size: 13px; margin: 0px 8px; font-weight: 400; color: #222; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;\">"+provider.getDescription()+"</span> </div>");
            sb.append("<div style=\"display: flex; align-items: center; position: relative; bottom: 24px; left: 6px; gap: 4px;\">");
            sb.append("<img src=\"https://icones.pro/wp-content/uploads/2021/02/icone-de-localisation-violette.png\" alt=\"Location\" ");
            sb.append("style=\"width: 24px; height: 24px\"/><span style=\"font-size: 13px; font-weight: 400\">"+provider.getCity()+"</span></div></div></div>");
        }

        String repeatedHtml = sb.toString();
        String firstPart = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> <html dir=\"ltr\" lang=\"en\"> <head> <link rel=\"preload\" as=\"image\" href=\"https://cdn.pixabay.com/photo/2020/08/05/13/29/eco-5465486_1280.png\" /> <meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\" /> <style> @font-face { font-family: \"Inter\"; font-style: normal; font-weight: 400; mso-font-alt: \"sans-serif\"; src: url(https://rsms.me/inter/font-files/Inter-Regular.woff2?v=3.19) format(\"woff2\"); } * { font-family: \"Inter\", sans-serif; } </style> <style> blockquote, h1, h2, h3, img, li, ol, p, ul { margin-top: 0; margin-bottom: 0; } </style> </head> <body> <div style=\" display: none; overflow: hidden; line-height: 1px; opacity: 0; max-height: 0; max-width: 0; \" id=\"__react-email-preview\" > Actualización Semanal </div> <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\" max-width: 600px; min-width: 300px; width: 100%; margin-left: auto; margin-right: auto; padding: 0.5rem; \" > <tbody> <tr style=\"width: 100%\"> <td> <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"margin-top: 0px; margin-bottom: 0px\" > <tbody style=\"width: 100%\"> <tr style=\"width: 100%\"> <td align=\"center\" data-id=\"__react-email-column\"> <img title=\"Logo\" alt=\"Logo\" src=\"https://cdn.pixabay.com/photo/2020/08/05/13/29/eco-5465486_1280.png\" style=\" display: block; outline: none; border: none; text-decoration: none; width: 64px; height: 64px; \" /> </td> </tr> </tbody> </table> <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"max-width: 37.5em; height: 32px\" > <tbody> <tr style=\"width: 100%\"> <td></td> </tr> </tbody> </table> <h2 style=\" text-align: left; color: rgb(17, 24, 39); margin-bottom: 12px; margin-top: 0px; font-size: 30px; line-height: 36px; font-weight: 700; \" > <strong>Actualización Semanal</strong> </h2> <p style=\" font-size: 15px; line-height: 24px; margin: 16px 0; text-align: left; margin-bottom: 20px; margin-top: 0px; color: rgb(55, 65, 81); -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; \" > </p> <p style=\" font-size: 15px; line-height: 24px; margin: 16px 0; text-align: left; margin-bottom: 20px; margin-top: 0px; color: rgb(55, 65, 81); -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; \" > En la semana hemos recibido nuevos proveedores a la famila de Ecosistema Red de Impacto. Te invitamos a que los explores, seguro te van a encantar. </p> <div style=\"display: flex; justify-content: center; padding: 20px\">";
        String secondPart =  "</div> <p style=\" font-size: 15px; line-height: 24px; margin: 16px 0; text-align: left; margin-bottom: 20px; margin-top: 0px; color: rgb(55, 65, 81); -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; \" > </p> <p style=\" font-size: 15px; line-height: 24px; margin: 16px 0; text-align: left; margin-bottom: 20px; margin-top: 0px; color: rgb(55, 65, 81); -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; \" > ¿Querés formar parte de la Red de impacto ECO como Proveedor? </p> <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"max-width: 100%; text-align: center; margin-bottom: 0px\" > <tbody> <tr style=\"width: 100%\"> <td> <a href=\"http://localhost:5173/\" style=\" color: #ffffff; background-color: #4e169d; border-color: #4e169d; padding: 12px 34px 12px 34px; border-width: 2px; border-style: solid; text-decoration: none; font-size: 14px; font-weight: 500; border-radius: 9999px; line-height: 100%; display: inline-block; max-width: 100%; \" target=\"_blank\" ><span ><!--[if mso ]><i style=\" letter-spacing: 34px; mso-font-width: -100%; mso-text-raise: 18; \" hidden >&nbsp;</i ><! [endif]--></span ><span style=\" max-width: 100%; display: inline-block; line-height: 120%; mso-padding-alt: 0px; mso-text-raise: 9px; \" >Registrate →</span ><span ><!--[if mso ]><i style=\"letter-spacing: 34px; mso-font-width: -100%\" hidden >&nbsp;</i ><! [endif]--></span ></a > </td> </tr> </tbody> </table> <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"max-width: 37.5em; height: 32px\" > <tbody> <tr style=\"width: 100%\"> <td></td> </tr> </tbody> </table> <p style=\" font-size: 15px; line-height: 24px; margin: 16px 0; text-align: left; margin-bottom: 20px; margin-top: 0px; color: rgb(55, 65, 81); -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; \" > Unete a nuestra comunidad de proveedores y personas conscientes con el impacto y el consumo consciente. </p> <p style=\" font-size: 15px; line-height: 24px; margin: 16px 0; text-align: left; margin-bottom: 20px; margin-top: 0px; color: rgb(55, 65, 81); -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; \" > Equipo de Ecosistema Red de Impacto<br /> </p> </td> </tr> </tbody> </table> </body> </html>";

        String[] to = emails.toArray(new String[0]);
        String subject = "Novedades en Ecos Red de Impacto";
        String content = firstPart + repeatedHtml + secondPart;
        //Descomentar cuando este listo el HTML del Front
        mailService.sendEmail(to,subject,content);
    }


}
