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
public class TelegramBot extends TelegramLongPollingBot
{


    @Autowired
    private UserRepository userRepository;
    private Rc rc;

    final BotConfig config;

    static final String HELP_TEXT = "Данный бот создан для получения рецептов. \n" +
                "Вы можете выполнить команды из главного меню: \n" +
                "/start - Начать \n" +
                "/help - Информация, как использовать этого бота ";
    static final String Rec = "11. 101 ккал   15 мин   Салат с редькой и морковью Легкий, простой, освежающий, всего за 15 минут! \n" +
            "https://static.1000.menu/res/640/img/content-v2/64/bf/72350/salat-s-redkoi-i-morkovu_1676959302_5_max.jpg  \n";



    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start","Начать"));
        listofCommands.add(new BotCommand("/help", "Информация, как использовать этого бота"));
        try{
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {

        }

    }


    @Override
    public String getBotUsername() { return config.getBotName(); }

    @Override
    public String getBotToken() { return config.getToken(); }


    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();



            switch (messageText){
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;

                case "Салат":
                    sendMessage(chatId, Rec);
                    break;


                default:

                    sendMessage(chatId, "Извините, рецепт не найден");

            }

        }

    }



    private void registerUser(Message msg) {

        if(userRepository.findById(msg.getChatId()).isEmpty()){

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setLogin(chat.getFirstName());

            userRepository.save(user);
        }
    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Здравствуйте, " + name + " \nВас приветствует бот рецептов! \nЕсли вы хотите получить рецепт выбирите одну цифру от 1 до 56!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);





        try{
            execute(message);
        }
        catch (TelegramApiException e){

        }
    }

}