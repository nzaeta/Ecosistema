package semillero.ecosistema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import semillero.ecosistema.entity.ProviderEntity;
import semillero.ecosistema.entity.UserEntity;
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
        List<String> listProviders = new ArrayList<>();
        for(ProviderEntity provider : providers){
            if(provider != null && provider.getIsNew() == true){
                listProviders.add(provider.getName());
                provider.setIsNew(false);
                providerRepository.save(provider);
            }
        }
        String[] to = {"nahuel9567@gmail.com"};//emails.toArray(new String[0]);
        String subject = "Correo de Prueba 2";
        String content = "<h1>Este mail es de prueba</h1>" +
                "<p>Por favor no responder</p>" +
                "<h1>" + listProviders + "<h1>";
        //Descomentar cuando este listo el HTML del Front
        //mailService.sendEmail(to,subject,content);
    }


}
