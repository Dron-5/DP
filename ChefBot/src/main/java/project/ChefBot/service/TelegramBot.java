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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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

    static final String HELP_TEXT = "Данный бот создан для получения рецептов.\uD83C\uDF71 \n" +
            "Вы можете использовать кнопки для поиска рецепта\n" +
            "А также вы можете выполнить команды из главного меню: \n" +
            "/start - Начать \n" +
            "/help - Информация, как использовать этого бота";


    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "Начать"));
        listofCommands.add(new BotCommand("/help", "Информация, как использовать этого бота"));
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
            if (!id.isEmpty()) {
                var recept = sqlite.findRecipes(id);
                sendMessage(chatId, recept);
            } else if (messageText.contains("Рецепт") && id.isEmpty()) {
                sendMessage(chatId, "Введите порядковый номер блюда! \nПример: 15");
            }


            switch (messageText) {
                case "/start" -> {
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
                case "/help" -> sendMessage(chatId, HELP_TEXT);
                case "Салат\uD83E\uDD57" -> sendMessage(chatId, sqlite.findSal());
                case "Суп\uD83C\uDF72" -> sendMessage(chatId, sqlite.findSup());
                case "Пирог\uD83E\uDD67" -> sendMessage(chatId, sqlite.findPir());
                case "Торт\uD83C\uDF70" -> sendMessage(chatId, sqlite.findTor());
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

        String answer = "Здравствуйте, " + name + " \nВас приветствует бот рецептов!\uD83C\uDF71 \n"+
                "Для того, чтобы получить рецепт \nвыберите, какое блюдо желаете, из предложенных";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List <KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Суп\uD83C\uDF72");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Салат\uD83E\uDD57");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Пирог\uD83E\uDD67");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Торт\uD83C\uDF70");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

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