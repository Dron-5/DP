package project.ChefBot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.ChefBot.Sqlite.*;
import project.ChefBot.config.BotConfig;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    private UserRepository userRepository;
    private Rc rc;
    @Autowired
    Sqlite sqlite;


    final BotConfig config;

    static final String HELP_TEXT = "Данный бот создан для получения рецептов. \n" +
            "Вы можете выполнить команды из главного меню: \n" +
            "/start - Начать \n" +
            "/help - Информация, как использовать этого бота \n" +
            "/recipe - Выбор рецепта";


    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "Начать"));
        listofCommands.add(new BotCommand("/help", "Информация, как использовать этого бота"));
        listofCommands.add(new BotCommand("/recipe", "Выбор рецепта"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println("Ошибка: " + e);
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            var id = extractDigits(messageText);
            if (messageText.contains("Рецепт") && !id.isEmpty()) {
                var recept = sqlite.findRecipes(id);
                sendMessage(chatId, recept);
            } else if (messageText.contains("Рецепт") && id.isEmpty()) {
                sendMessage(chatId, "Введите номер блюда! \nПример: Рецепт 2");
            }


            switch (messageText) {
                case "/start" -> {
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
                case "/help" -> sendMessage(chatId, HELP_TEXT);
                case "Пирог" -> sendMessage(chatId, sqlite.findSal());
                case "Суп"-> sendMessage(chatId, sqlite.findSup());
            }

        }

    }

    public String extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }


    private void registerUser(Message msg) {

        if (userRepository.findById(msg.getChatId()).isEmpty()) {

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setLogin(chat.getFirstName());

            userRepository.save(user);
        }
    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Здравствуйте, " + name + " \nВас приветствует бот рецептов! \nДля того, чтобы получить рецепт напишите, какое блюдо желаете, например 'Пирог'";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendMessage(long chatId, List<String> textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        for (String text : textToSend) {
            message.setText(text);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }


    }

}