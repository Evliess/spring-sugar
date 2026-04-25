package evliess.io.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbChecker {
    private static final Logger log = LoggerFactory.getLogger(DbChecker.class);

    @Bean
    public CommandLineRunner checkDb(JdbcTemplate jdbcTemplate) {
        return args -> {
            log.info("==============================================");
            try {
                // 打印当前连接的数据库名
                String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
                log.info(">>> 当前连接的数据库名是: " + dbName);

                // 打印该数据库下的所有表
                List<String> tables = jdbcTemplate.queryForList("SHOW TABLES", String.class);
                log.info(">>> 该数据库下的所有表如下：");
                if (tables.isEmpty()) {
                    log.info("    (空空如也，没有任何表！)");
                } else {
                    for (String table : tables) {
                        log.info("    - " + table);
                    }
                }
            } catch (Exception e) {
                log.info(">>> 数据库连接失败: " + e.getMessage());
            }
            log.info("==============================================\n");
        };
    }
}
