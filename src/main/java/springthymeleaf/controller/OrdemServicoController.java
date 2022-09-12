package springthymeleaf.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import springthymeleaf.dto.RequisicaoOrdemServico;
import springthymeleaf.entities.Cliente;
import springthymeleaf.entities.OrdemServico;
import springthymeleaf.entities.Produto;
import springthymeleaf.entities.Servico;
import springthymeleaf.entities.StatusOrdemServico;
import springthymeleaf.repositories.ClienteRepository;
import springthymeleaf.repositories.OrdemServicoRepository;
import springthymeleaf.repositories.ProdutoRepository;
import springthymeleaf.repositories.ServicoRepository;

@Controller
@RequestMapping("/ordemservico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @GetMapping("")
    public ModelAndView paginaInicialOS() {
        ModelAndView mv = new ModelAndView("ordemservico/index");
        return mv;
    }

    @GetMapping("/new")
    public ModelAndView paginaCadastro(RequisicaoOrdemServico requisicao) {
        Iterable<Cliente> clientes = clienteRepository.findAll();
        Iterable<Produto> produtos = produtoRepository.findAll();
        Iterable<Servico> servicos = servicoRepository.findAll();
        ModelAndView mv = new ModelAndView("ordemservico/new");
        mv.addObject("statusOrdemServico", StatusOrdemServico.values());
        mv.addObject("clientes", clientes);
        mv.addObject("produtos", produtos);
        mv.addObject("servicos", servicos);
        return mv;

    }

    @PostMapping("")
    public ModelAndView cadastro(@Valid RequisicaoOrdemServico requisicao, BindingResult erro) {
        
        OrdemServico ordemServico = requisicao.toOS();

        if (erro.hasErrors()) {
            ModelAndView mv = new ModelAndView("ordemservico/new");
            System.out.println(erro);
            return mv;

        } else {
            ordemServicoRepository.save(ordemServico);
            return new ModelAndView("redirect:/ordemservico");
        }

    }

    //LIKE for Kaue Java
    //@GetMapping("/Clientes/{qual}")
    // @Query("SELECT c FROM Cliente c WHERE c.nome LIKE ")
    //public List<Cliente> listClienteParecido(@PathVariable String qual) {
    //   List<Cliente> clientes = clienteRepository.findAll();
    //   List<Cliente> listResulPesquisa = new ArrayList<>();
    //   clientes.forEach(x ->{
    //        if (x.getNome().contains("qual")) {
    //            listResulPesquisa.add(x);           
    //        }
    //    });
    //    return listResulPesquisa;
    //}
}
