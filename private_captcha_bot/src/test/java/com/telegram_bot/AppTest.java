package com.telegram_bot;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.telegram_bot.PrivateCatchaBot;
import com.telegram_bot.handlers.commands.CommandHandler;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

/** Unit test for simple App. */
public class AppTest {
  /** Rigorous Test :-) */
  @Test
  public void shouldAnswerWithTrue() {
    assertTrue(true);
  }

  @Test
  public void shouldContainBanCommandHandler() throws Exception {
    PrivateCatchaBot bot = new PrivateCatchaBot("dummy");
    Field field = PrivateCatchaBot.class.getDeclaredField("commandHandlerMap");
    field.setAccessible(true);

    @SuppressWarnings("unchecked")
    Map<String, CommandHandler> map = (Map<String, CommandHandler>) field.get(bot);

    assertNotNull("/ban handler should be registered", map.get("/ban"));
  }
}
