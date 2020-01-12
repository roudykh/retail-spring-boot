package com.roudy;

import com.roudy.user.User;
import com.roudy.user.UserRepository;
import com.roudy.good.Good;
import com.roudy.good.GoodRepository;
import com.roudy.purchase.Purchase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;

@SpringBootApplication
public class StartApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    GoodRepository goodRepository;

    @Bean
    public LobHandler lobHandler() {
        return new DefaultLobHandler();
    }

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception{

        log.info("Starting Application...");
        startTestCases();
        
    }

    // Using H2 database
    void startTestCases() throws Exception{

        jdbcTemplate.execute("DROP TABLE user IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE user(" +
                "id SERIAL, name VARCHAR(100), type VARCHAR(10), created_date date)");

        List<User> list = Arrays.asList(
                new User("User A", "Employee", "01022018"),
                new User("User B", "Affliate", "01042019"),
                new User("User C", "Customer", "01032019"),
                new User("User D", "Customer", "01012017")
        );

        list.forEach(x -> {
            log.info("Saving...{}", x.getName());
            userRepository.save(x);
        });
        
        jdbcTemplate.execute("DROP TABLE good IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE good(" +
                             "id SERIAL, name VARCHAR(100), type VARCHAR(10), price number)");
        
        List<Good> goodList = Arrays.asList(
                new Good("Mango", "Grocery", 1.5),
                new Good("Apple", "Grocery", 3),
                new Good("Knife", "Supply", 5),
                new Good("Plate", "Supply", 6)
        );
        
        HashMap<Good, Integer> basket = new HashMap<Good, Integer>();
        
        goodList.forEach(u -> {
            log.info("Saving...{}", u.getName());
            goodRepository.save(u);
            basket.put(u, 1);
        });
        
        Purchase case1 = new Purchase(basket, userRepository.findByUserId(1L));
        Purchase case2 = new Purchase(basket, userRepository.findByUserId(2L));
        Purchase case3 = new Purchase(basket, userRepository.findByUserId(3L));
        Purchase case4 = new Purchase(basket, userRepository.findByUserId(4L));
        
        case1.checkout();
        case2.checkout();
        case3.checkout();
        case4.checkout();

    }


}
