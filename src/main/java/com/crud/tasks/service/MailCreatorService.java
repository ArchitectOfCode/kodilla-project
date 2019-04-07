package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.config.CompanyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailCreatorService {
    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private CompanyConfig companyConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    private String previewMessage(String message) {
        String previewMessage = "";
        if(message.indexOf(":") > 20 || message.indexOf(":") < 0) {
            String[] splittedMessage = message.split(" ");
            int numOfWordsInPreview = splittedMessage.length > 7 ? 7 : splittedMessage.length;
            for(int i = 0; i < numOfWordsInPreview; i++) {
                previewMessage = previewMessage.concat(splittedMessage[i]);
                previewMessage = i < numOfWordsInPreview - 1 ? previewMessage.concat(" ") : previewMessage.concat(".");
            }
        } else {
            previewMessage = message.substring(0, message.indexOf(":")).concat(" created.");
        }
        return previewMessage;
    }

    public String buildTrelloCardEmail(String message) {
        Context context = new Context();
        context.setVariable("preview_message", previewMessage(message));
        context.setVariable("message", message);
        context.setVariable("goodbye_message", "Please don't reply to this email as the inbox is unattended.");
        context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_name", adminConfig.getAdminName());
        context.setVariable("company_details", companyConfig.getCompanyName()
                .concat(", ".concat(companyConfig.getCompanyGoal())));
        return templateEngine.process("mail/created-trello-card-mail", context);
    }
}
