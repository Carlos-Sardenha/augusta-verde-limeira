package br.com.augustaverde.controller;

import br.com.augustaverde.service.ArvoresService;
import br.com.augustaverde.service.CalculadoraCarbono;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SiteController {

    private final ArvoresService arvoresService;
    private final CalculadoraCarbono calculadoraCarbono;

    @GetMapping("/")
    public String inicio(Model model) {
        int arvores = arvoresService.quantidadeAtual();
        model.addAttribute("arvores", arvores);
        model.addAttribute("impacto", calculadoraCarbono.calcular(arvores));
        model.addAttribute("kgPorArvore", CalculadoraCarbono.KG_CO2_POR_ARVORE_EM_20_ANOS);
        return "index";
    }
}
