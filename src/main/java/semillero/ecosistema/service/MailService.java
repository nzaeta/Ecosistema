package semillero.ecosistema.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.entity.UserEntity;
import semillero.ecosistema.repository.ProviderRepository;
import semillero.ecosistema.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProviderRepository providerRepository;

    public void sendEmail (String[] to, String subject, String content){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("nahuel9567@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //Descomentar para probar en el momento
   /* @PostConstruct
    public void initialize() {

        List<UserEntity> usuarios = userRepository.findAll();
        List<String> emails = new ArrayList<>();
        for (UserEntity usuario : usuarios) {
            if (usuario != null && usuario.getEmail() != null) {
                emails.add(usuario.getEmail());
            }
        }
        List<ProviderEntity> providers = providerRepository.findAll();
        List<String> listProviders = new ArrayList<>();
        for(ProviderEntity provider : providers){
            if(provider != null && provider.getIsNew() == true){
                listProviders.add(provider.getName());
                provider.setIsNew(false);
                providerRepository.save(provider);
                System.out.print(provider.getIsNew());
            }
        }
        String[] to = {"nahuel9567@gmail.com"};//emails.toArray(new String[0]);
        String subject = "Correo de Prueba 4";
        String content = "<h1>Este mail es de prueba</h1>" +
                "<p>Por favor no responder</p>" +
                "<h1>" + listProviders + "<h1>";
        sendEmail(to, subject, content);
    }*/

}
