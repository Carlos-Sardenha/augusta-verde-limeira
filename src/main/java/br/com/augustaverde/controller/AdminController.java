package br.com.augustaverde.controller;

import br.com.augustaverde.service.ArvoresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ArvoresService arvoresService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping
    public String painel(Model model) {
        model.addAttribute("form", new AtualizacaoArvoresForm(arvoresService.quantidadeAtual()));
        return "admin/painel";
    }

    @PostMapping("/arvores")
    public String atualizar(@Valid @ModelAttribute("form") AtualizacaoArvoresForm form, BindingResult erros) {
        if (erros.hasErrors()) {
            return "admin/painel";
        }
        arvoresService.atualizar(form.getQuantidade());
        return "redirect:/admin?salvo";
    }
}
