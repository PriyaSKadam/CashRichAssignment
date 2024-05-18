package in.priya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.priya.service.CoinService;

@RestController
public class CoinController {
	
	@Autowired
	private CoinService coinService;
	
	public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/coins")
    public String getCoinDetails(@RequestParam Integer userId, @RequestParam String symbols) {
        return coinService.getCoinDetails(userId, symbols);
    }

}
