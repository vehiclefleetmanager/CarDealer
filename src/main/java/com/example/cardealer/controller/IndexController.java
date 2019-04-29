package com.example.cardealer.controller;

import com.example.cardealer.model.Car;
import com.example.cardealer.model.Owner;
import com.example.cardealer.model.dtos.CarDto;
import com.example.cardealer.model.dtos.OwnerDto;
import com.example.cardealer.service.CarService;
import com.example.cardealer.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/*Kontroler którego zadaniem jest porzedstawienie
oferty komisu na stronie głownej*/
@Controller
public class IndexController {

    private final CarService carService;
    private final OwnerService ownerService;

    @Autowired
    public IndexController(CarService carService,
                           OwnerService ownerService) {
        this.carService = carService;
        this.ownerService = ownerService;
    }

    /*Main page methods part*/
    @GetMapping("/")
    public String getMainApplicationPageView(Model model) {
        model.addAttribute("cars", carService.getAvailableCars());
        prepareSearchFormFields(model);
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPageInApplicationView() {
        return "login";
    }


    /*Owner methods part*/
    @GetMapping("/owner")
    public String getOwnerPageView(Model model) {
        model.addAttribute("owner", ownerService.getOwnerById(19));
        return "owner";
    }

    @GetMapping("/owner/{ownerId}/owner-update")
    public String getPageViewToUpdateOwner(@PathVariable Integer ownerId, Model model) {
        model.addAttribute("owner", ownerService.getOwnerById(ownerId));
        return "owner-update";
    }

    @PutMapping("/updateOwner")
    public String doOwnerUpdate(@ModelAttribute("ownerDto") OwnerDto ownerDto) {
        ownerService.updateOwner(ownerDto);
        return "redirect:/owner";
    }

    @GetMapping("/sale")
    public String getPageForRegisterNewOwnerView(Model model) {
        model.addAttribute("person", new OwnerDto());
        return "person-add";
    }

    @GetMapping("/{carId}/more")
    public String getPageWhereDisplayDetailsCar(@PathVariable Integer carId, Model model) {
        model.addAttribute("carDto", carService.getCar(carId));
        return "more";
    }

    @PostMapping("/addPerson")
    public String doAddNewOwner(@ModelAttribute("ownerDto") OwnerDto ownerDto) {
        ownerDto.setStatus(Owner.Status.PRESENT);
        Owner owner = ownerService.addOwner(ownerDto);
        Integer ownerId = owner.getOwnerId();
        return "redirect:/" + ownerId + "/add-car";
    }

    @GetMapping("/{ownerId}/add-car")
    public String getPageForRegisterNewCarView(@PathVariable Integer ownerId, Model model) {
        model.addAttribute("car", new CarDto());
        model.addAttribute("owner", ownerService.getOwnerById(ownerId));
        return "add-car";
    }

    @PostMapping("/{ownerId}/addCar")
    public String doAddNewCar(@ModelAttribute("carDto") CarDto carDto, @PathVariable Integer ownerId) {
        carDto.setStatus(Car.Status.WAIT);
        carDto.setOwnerId(ownerId);
        carDto.setTestDrive(0);
        carService.addCar(carDto);
        return "redirect:/";
    }

    @PostMapping("/search")
    public String filters(@ModelAttribute(value = "carDto") CarDto carDto,
                          @RequestParam(value = "mark", required = false) @PathVariable String carMark,
                          @RequestParam(value = "model", required = false) @PathVariable String carModel,
                          @RequestParam(value = "productionYearFrom", required = false) @PathVariable Integer productionYearFrom,
                          @RequestParam(value = "productionYearTo", required = false) @PathVariable Integer productionYearTo,
                          @RequestParam(value = "priceFrom", required = false) @PathVariable BigDecimal priceFrom,
                          @RequestParam(value = "priceTo", required = false) @PathVariable BigDecimal priceTo,
                          Model model) {
        model.addAttribute("cars",
                carService.getCarsFromSearchButton(carMark, carModel, productionYearFrom, productionYearTo,
                        priceFrom, priceTo, Car.Status.AVAILABLE));
        prepareSearchFormFields(model);
        return "result";
    }

    private void prepareSearchFormFields(Model model) {
        model.addAttribute("markList", carService.findMark());
        model.addAttribute("modelList", carService.findModel());
        model.addAttribute("years", carService.findProductionYear());
    }
    /*?mark="+carMark+"&model="+carModel+"&productionYear="+productionYear+"&productionYear="
            +productionYear+"&price="+price+"&price="+price;*/



    /*@PostMapping("/sale")
    public String addCarToSale(@ModelAttribute CarDto carDto){
        carService.addCar(carDto);
        return "customer/sale";
    }*/


    /*@RequestMapping
    public String showCars(@ModelAttribute("carDto") CarDto carDto, Model model) {
        List<Car> cars = carService.findByRenouncementLike(Transaction.RENOUNCEMENT);
        model.addAttribute("cars", cars);

        List<String> markList = carService.findMark();
        model.addAttribute("markList", markList);

        List<String> modelList = carService.findModel();
        model.addAttribute("modelList", modelList);

        List<Integer> years = carService.findProductionYear();
        model.addAttribute("years", years);

        return "index";
    }


*/
}