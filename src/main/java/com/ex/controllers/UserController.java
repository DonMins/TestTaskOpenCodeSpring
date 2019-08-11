package com.ex.controllers;

import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.entity.History;
import com.ex.entity.Rating;
import com.ex.entity.User;
import com.ex.service.SecurityService;
import com.ex.service.UserService;
import com.ex.task.youNumberOfjson;
import com.ex.validator.UserValidator;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Zdornov Maxim
 * @version 1.0
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RatingsDao ratingsDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    /**
     * Method displays user rating
     *
     * @return Return rating.jsp page
     */

    @RequestMapping(value = "/rating", method = RequestMethod.GET)
    public String rating(Model model) {
        List<Rating> ratingList = ratingsDao.findAll();
        // Создаем map с именем пользователя и его рейтингом и сразу отсортировываем по убыванию
        Map<Double, String> map = new TreeMap<>(new Comparator<Double>() {
            @Override
            public int compare(Double a, Double b) {
                if (a >= b) {
                    return 1;
                } else if (a < b)
                    return -1;
                else
                    return 0;
            }
        });
        // Пользователи с 0  getCountgame() не будут отображаться в списке
        for (Rating rating : ratingList) {
            map.put((double) rating.getAllAttempt() / rating.getCountgame(), rating.getUsername());
        }

        model.addAttribute("rating", map);
        return "rating";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) throws UnsupportedEncodingException {
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
            model.addAttribute("message", "Вышли успешно");
            return "redirect:/login";
        }
        return "login";
    }

    /**
     * Game Start Page
     *
     * @return Return game.jsp page
     */

    @RequestMapping(value = {"/", "/game"}, method = RequestMethod.GET)
    public String game() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Rating tempRating = ratingsDao.findByUsername(auth.getName());
        tempRating.setYouGenNumber(genNumber()); // Как только пользователь вошел на страницу game, сразу сгенерим
        // ему число для угадывания
        ratingsDao.save(tempRating);
        return "game";
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * This method allows you to get the history of previous user games
     *
     * @return Return the history to game.jsp to textareaHistory
     */
    @RequestMapping(method = RequestMethod.POST, value = "/history", consumes = "text/plain")
    @ResponseBody
    ResponseEntity<?> history(@RequestBody String string) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JSONObject obj = new JSONObject(string);
        String isClear = obj.getString("clear"); //was there a request to clear history in the database
        if (isClear.equals("yes")) {
            History history = historyDao.findByUsername(auth.getName());
            history.setData("");
            historyDao.save(history);
        }
        History history = historyDao.findByUsername(auth.getName());

        return ResponseEntity.ok().headers(new HttpHeaders() {{
            add("Content-Type", "text/plain; charset=utf-8");
        }}).body(history.getData());
    }

    /**
     * This method gets the number passed by the user to guess the intended number
     *
     * @return Return the result to game.jsp
     */
    @RequestMapping(method = RequestMethod.POST, value = "/newAttempt", consumes = "text/plain")
    @ResponseBody
    ResponseEntity<?> newAttempt(@RequestBody String body) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        youNumberOfjson requestBody = objectMapper().readValue(body, youNumberOfjson.class);

        String result = result(requestBody.getYouNumber(), auth);

        // After each departure of the number we save the whole history
        History history = historyDao.findByUsername(auth.getName());
        history.setData(history.getData() + result + "\n");
        historyDao.save(history);

        return ResponseEntity.ok().headers(new HttpHeaders() {{
            add("Content-Type", "text/plain; charset=utf-8");
        }}).body(result);
    }

    /**
     * @return Number generated by computer for guessing.
     */
    private String genNumber() {
        final int MAX_NUMBER = 10;
        Integer[] randomNumbers = new Integer[MAX_NUMBER];
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = i;
        }
        Collections.shuffle(Arrays.asList(randomNumbers));
        return String.valueOf(randomNumbers[0]) + randomNumbers[1] + randomNumbers[2] + randomNumbers[3];
    }

    /**
     * @param stringOfYouEnteredNumber: Your entered number to computer
     * @param auth:                     authentication data
     * @return Line with the number of "bulls" and "cows"
     */
    private String result(String stringOfYouEnteredNumber, Authentication auth) {

        Rating tempRating = ratingsDao.findByUsername(auth.getName());
        tempRating.setAllAttempt(tempRating.getAllAttempt() + 1); // increase the number of attempts
        // to guess in the database

        ratingsDao.save(tempRating);
        String number = tempRating.getYouGenNumber(); //Get the number to guess

        ArrayList<Character> numberSymbol = new ArrayList<>();
        ArrayList<Character> strSymbol = new ArrayList<>();
        ArrayList<Character> l3;
        l3 = strSymbol;
        int bull = 0;
        int cow = 0;
        for (int i = 0; i < 4; ++i) {
            numberSymbol.add(number.charAt(i));
            strSymbol.add(stringOfYouEnteredNumber.charAt(i));
            if (number.charAt(i) == stringOfYouEnteredNumber.charAt(i)) {
                bull++;
            }
        }
        l3.retainAll(numberSymbol);
        cow = l3.size() - bull;
        if (bull == 4) {
            tempRating.setCountgame(tempRating.getCountgame() + 1);
            tempRating.setYouGenNumber(genNumber());
            ratingsDao.save(tempRating);
            return stringOfYouEnteredNumber + " - " + bull + "Б" + cow + "K (число угадано) \n---------------------------\nЯ загадал еще...";
        }
        return stringOfYouEnteredNumber + " - " + bull + "Б" + cow + "K";
    }
}
