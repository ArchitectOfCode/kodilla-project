package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.config.CompanyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {
    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private CompanyConfig companyConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    private String CRUD_TASKS = "http://localhost:8888/tasks_frontend/";
    private String TRELLO_BOARDS = "https://trello.com/aoc_kodilla/boards";

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

    public String buildTrelloCardEmail(String message, String link) {
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("preview_message", previewMessage(message));
        context.setVariable("message", message);
        context.setVariable("goodbye_message", "Please don't reply to this email as the inbox is unattended.");
        context.setVariable("tasks_url", link);
        context.setVariable("button", "Show in Trello");
        context.setVariable("admin_config", adminConfig);
        /*context.setVariable("company_details", companyConfig.getCompanyName()
                .concat(", ".concat(companyConfig.getCompanyGoal())));*/
        context.setVariable("company_config", companyConfig);
        context.setVariable("show_button", true);
        context.setVariable("is_friend", true);
        context.setVariable("application_functionality", functionality);
        return templateEngine.process("mail/created-trello-card-mail", context);
    }

    public String buildCurrentlyInDatabaseEmail(String message) {
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("preview_message", previewMessage(message));
        context.setVariable("message", message);
        context.setVariable("goodbye_message", "Please don't reply to this email as the inbox is unattended.");
        context.setVariable("tasks_url", CRUD_TASKS);
        context.setVariable("button", "Show CRUD tasks");
        context.setVariable("admin_config", adminConfig);
        /*context.setVariable("company_details", companyConfig.getCompanyName()
                .concat(", ".concat(companyConfig.getCompanyGoal())));*/
        context.setVariable("company_config", companyConfig);
        context.setVariable("show_button", true);
        context.setVariable("is_friend", false);
        context.setVariable("application_functionality", functionality);
        return templateEngine.process("mail/currently-in-database-mail", context);
    }
}
