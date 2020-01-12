package com.roudy.purchase;

import java.time.LocalDateTime;
import com.roudy.user.User;
import com.roudy.good.Good;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Purchase {

    private static final Logger log = LoggerFactory.getLogger(Purchase.class);
    
    private Long ID;
    private HashMap<Good, Integer> basket;
    private User user;
    private double price;

    public Purchase() {
    }

    public Purchase(HashMap<Good, Integer> basket, User user) {
        this.basket = basket;
        this.user = user;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public HashMap<Good, Integer> getBasket() {
        return basket;
    }

    public void setBasket(HashMap<Good, Integer> basket) {
        this.basket = basket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void checkout() throws Exception{
        
        Date cutoffDate = java.sql.Date.valueOf(LocalDate.now().minusDays(730));
        double sum = 0;
        
        switch(user.getType()) {
            case "Employee":
                log.info("Employee");
                
                for (HashMap.Entry<Good, Integer> entry : basket.entrySet()) {
                    Good good = entry.getKey();
                    Integer quantity = entry.getValue();
                    sum += good.getPrice()*quantity;
                }
                sum *= 0.7;
                break;
            case "Affliate":
                log.info("Affliate");
                for (HashMap.Entry<Good, Integer> entry : basket.entrySet()) {
                    Good good = entry.getKey();
                    Integer quantity = entry.getValue();
                    sum += good.getPrice()*quantity;
                }
                sum *= 0.9;
                break;
            case "Customer":
                if(string2date(user.getCreatedDate()).after(cutoffDate)) {
                    log.info("New Customer");
                    for (HashMap.Entry<Good, Integer> entry : basket.entrySet()) {
                        Good good = entry.getKey();
                        Integer quantity = entry.getValue();
                        sum += good.getPrice()*quantity;
                    }
                    if(sum >= 100)
                        sum *= 0.95;
                } else {
                    log.info("Old Customer");
                    for (HashMap.Entry<Good, Integer> entry : basket.entrySet()) {
                        Good good = entry.getKey();
                        Integer quantity = entry.getValue();
                        sum += good.getPrice()*quantity;
                    }
                    sum *= 0.95;
                }
                break;
            default:
                log.info("Error default");
        }
        
        this.price = sum;
    }
    
    Date string2date(String str) throws Exception {
        DateFormat format = new SimpleDateFormat("ddMMyyyy");
        Date date = format.parse(str);
        return date;
    }
}
