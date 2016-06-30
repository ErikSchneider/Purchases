package com.theironyard;

import org.hibernate.engine.internal.JoinSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
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
    public String home(Model model, Integer page, String category) {
        page = (page == null) ? 0 : page;

        PageRequest pr = new PageRequest(page, 10);

        Page<Purchase> purchase;

        if (category != null) {
            purchase = purchases.findByCategory(pr, category);
            }

        else {
            purchase = purchases.findAll(pr);
        }

        model.addAttribute("purchases", purchase);
        model.addAttribute("category", category);

        model.addAttribute("nextPage", page + 1);
        model.addAttribute("showNext", purchase.hasNext());

        model.addAttribute("prevPage", page - 1);
        model.addAttribute("showPrev", purchase.hasPrevious());

        return "home";
    }
}
