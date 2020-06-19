package org.abubusoft.reversi.client;

import com.tinder.scarlet.Scarlet;
import com.tinder.scarlet.messageadapter.jackson.JacksonMessageAdapter;
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory;
import com.tinder.scarlet.websocket.okhttp.OkHttpClientUtils;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import org.abubusoft.reversi.server.model.messages.Greeting;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientTest {

  Logger logger = LoggerFactory.getLogger(ClientTest.class);

  @Test
  public void test() {
    OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
    Scarlet scarletInstance = new Scarlet.Builder()
            .webSocketFactory(OkHttpClientUtils
                    .newWebSocketFactory(okHttpClient, "ws://localhost:8080/messages/websocket/"))
            .addMessageAdapterFactory(new JacksonMessageAdapter.Factory())
            .addStreamAdapterFactory(new RxJava2StreamAdapterFactory())
            .build();

    ReversiService reversiService = scarletInstance.create(ReversiService.class);

    reversiService.observeWebSocketEvent()
            .subscribeOn(Schedulers.io())
            .subscribe(event -> {
              logger.info(event.toString());
            });

    reversiService.sendGreetings(Greeting.of("ciao"));
  }
}
