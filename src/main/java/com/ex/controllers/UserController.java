package com.ex.controllers;

import com.ex.dao.UserDao;
import com.ex.entity.User;
import com.ex.service.SecurityService;
import com.ex.service.UserService;
import com.ex.task.Numbe;
import com.ex.task.Task;
import com.ex.validator.UserValidator;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


/**

 *
 * @author Eugene Suleimanov
 * @version 1.0
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    String listNumbers = "";
    Task task = new Task(genNumber());
    boolean clear = false;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/rating", method = RequestMethod.GET)
    public String rating(Model model) {
        return "rating";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/game";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Имя пользователя или пароль неверны");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
            return "redirect:/login";
        }

        return "login";
    }

    @RequestMapping(value = {"/", "/game"}, method = RequestMethod.GET)
    public String game(Model model)
    {

        return "game";
    }

    public class JsonResponse {

        private String status = "";
        private String errorMessage = "";

        public JsonResponse(String status, String errorMessage) {
            this.status = status;
            this.errorMessage = errorMessage;
        }
    }

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newAttempt",consumes = "text/plain" )
    @ResponseBody  ResponseEntity<?> newAttempt(@RequestBody String body ) throws IOException {
        Numbe requestBody = objectMapper().readValue( body , Numbe.class);
        if (clear){
            clear = false;
            listNumbers ="";
        }
        String result = result(requestBody.getYouNumber());
        listNumbers = listNumbers + result + "\n";

        System.out.println("GFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "  +requestBody.getYouNumber());
        return ResponseEntity.ok().headers( new HttpHeaders(){{
            add( "Content-Type" , "text/plain; charset=utf-8" );
        }} ).body(listNumbers);
    }

    private String genNumber() {

        final int MAX_NUMBER = 10;
        Integer[] randomNumbers = new Integer[MAX_NUMBER];
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = i;
        }
        Collections.shuffle(Arrays.asList(randomNumbers));
        return String.valueOf(randomNumbers[0]) + randomNumbers[1] + randomNumbers[2] + randomNumbers[3];
    }

    private String result(String str) {
        String number = task.getNumber();
        System.out.println(number);

        ArrayList<Character> numberSymbol = new ArrayList<>();
        ArrayList<Character> strSymbol = new ArrayList<>();
        ArrayList<Character> l3 = new ArrayList<Character>();
        l3 = strSymbol;

        int bull = 0;
        int cow = 0;
        for (int i = 0; i < 4; ++i) {
            numberSymbol.add(number.charAt(i));
            strSymbol.add(str.charAt(i));
            if (number.charAt(i) == str.charAt(i)) {
                bull++;
            }
        }
        l3.retainAll(numberSymbol);
        cow = l3.size() - bull;
        if (bull == 4) {
            task = new Task(genNumber());
            clear = true;
            return str + " - " + bull + "Б" + cow + "K (число угадано) \n---------------------------\nЯ загадал еще...";
        }
        return str + " - " + bull + "Б" + cow + "K";
    }
}
