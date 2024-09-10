package online.mayak.energy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import online.mayak.energy.service.ChargerService;
import online.mayak.energy.service.ConnectorService;
import online.mayak.energy.service.OcppTokenService;
import online.mayak.energy.service.OcppTransactionService;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final ChargerService chargerService;
	private final ConnectorService connectorService;
	private final OcppTokenService ocppTokenService;
	private final OcppTransactionService ocppTransactionService;

	@GetMapping({"", "/", "/index", "home"})
	public String home(Model model) {
		model.addAttribute("chargerAllCount", chargerService.getCount());
		model.addAttribute("chargerOnlineCount", chargerService.getOnlineCount());
		model.addAttribute("ocppTokenAllCount", ocppTokenService.getCount());
		model.addAttribute("ocppTransactionActiveCount", ocppTransactionService.getActiveCount());
		model.addAttribute("connectorStatuses", connectorService.getCountByStatus());
		return "home";
	}

}
