package ru.omarov.quotes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.omarov.quotes.entity.Position;
import ru.omarov.quotes.services.InitService;
import ru.omarov.quotes.services.PositionService;

import java.time.Duration;

@Component
@Slf4j
public class ResourcesInitializer implements ApplicationRunner {

    private final InitService initService;
    private final PositionService positionService;

    public ResourcesInitializer(InitService initService, PositionService positionService) {
        this.initService = initService;
        this.positionService = positionService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (positionService.getAll().isEmpty()) {
            log.info("Загружка позиций");
            initService.initializePositions();
            log.info("Позиции загружены");
            log.info("Инициализация истории цен");
            int i = 0;
            for (Position p :
                    positionService.getAll()) {
                if (i > 299) {
                    Thread.sleep(Duration.ofMinutes(1).toMillis());
                    i = 0;
                }
                initService.initializePriceHistoryByPositionUid(p.getUid());
                i++;
            }
            log.info("История проинициализирована");
        }
    }
}
