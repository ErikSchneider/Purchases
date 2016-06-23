package com.theironyard;

import org.hibernate.engine.internal.JoinSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Erik on 6/22/16.
 */
@Controller
public class PurchasesController {

    @Autowired
    CustomerRepository customers;

    @Autowired
    PurchaseRepository purchases;

    @PostConstruct
    public void init() throws FileNotFoundException {
        if (customers.count()== 0) {
            File customerFile = new File("customers.csv");
            Scanner scanner = new Scanner(customerFile);
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String columns[] = line.split(",");
                Customer customer = new Customer(columns[0], columns[1]);
                customers.save(customer);
            }
        }

        if (purchases.count()== 0) {
            File purchaseFile = new File("purchases.csv");
            Scanner scanner = new Scanner(purchaseFile);
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String columns[] = line.split(",");
                Purchase purchase = new Purchase(columns[1], columns[2], columns[3], columns[4], customers.findOne(Integer.valueOf(columns[0])));
                purchases.save(purchase);
            }
        }
    }


    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, String category) {
        Iterable<Purchase> purchase;
        if (category == null) {
            purchase = purchases.findByCategory(category);
            model.addAttribute("purchases", purchase);
            }
        else {
            purchase = purchases.findAll();
            model.addAttribute("purchases", purchase);
        }
        return "home";
    }
}
