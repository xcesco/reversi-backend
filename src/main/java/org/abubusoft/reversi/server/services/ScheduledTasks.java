package org.abubusoft.reversi.server.services;

import org.abubusoft.reversi.server.model.NetworkPlayer1;
import org.abubusoft.reversi.server.model.NetworkPlayer2;
import org.abubusoft.reversi.server.model.User;
import org.abubusoft.reversi.server.model.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

  private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


}