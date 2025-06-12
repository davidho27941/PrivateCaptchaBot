import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface CommandHandler {
    /**
     * 處理從 Telegram 收到的 Update（已知一定是有文字的訊息）
     * @param update 完整的 Update 物件
     * @param telegramClient 用來呼叫 execute(...) 發送回覆
     */
    void handle(Update update, TelegramClient telegramClient) throws TelegramApiException;
}