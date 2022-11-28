package springthymeleaf.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import springthymeleaf.dto.RequisicaoOrdemServico;
import springthymeleaf.dto.RequisicaoProdutoOrdem;
import springthymeleaf.dto.RequisicaoServicoOrdem;
import springthymeleaf.entities.Cliente;
import springthymeleaf.entities.OrdemServico;
import springthymeleaf.entities.Produto;
import springthymeleaf.entities.ProdutoOrdem;
import springthymeleaf.entities.Servico;
import springthymeleaf.entities.ServicoOrdem;
import springthymeleaf.entities.StatusOrdemServico;
import springthymeleaf.entities.Tecnico;
import springthymeleaf.repositories.ClienteRepository;
import springthymeleaf.repositories.OrdemServicoRepository;
import springthymeleaf.repositories.ProdutoOrdemRepository;
import springthymeleaf.repositories.ProdutoRepository;
import springthymeleaf.repositories.ServicoOrdemRepository;
import springthymeleaf.repositories.ServicoRepository;
import springthymeleaf.repositories.StatusOrdemServicoRepository;
import springthymeleaf.repositories.TecnicoRepository;
import springthymeleaf.services.ClienteService;
import springthymeleaf.services.OrdemServicoService;
import springthymeleaf.services.ProdutoService;
import springthymeleaf.services.ServicoService;
import springthymeleaf.services.StatusOrdemServicoService;
import springthymeleaf.services.TecnicoService;

@Controller
@RequestMapping("/ordemservico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired ServicoService servicoService;

    @Autowired
    private StatusOrdemServicoService statusOrdemServicoService;

    @Autowired
    private StatusOrdemServicoRepository statusOrdemServicoRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ProdutoOrdemRepository produtoOrdemRepository;

    @Autowired
    private ServicoOrdemRepository servicoOrdemRepository;

    @GetMapping("")
    public ModelAndView paginaInicialOS() {
        List<OrdemServico> ordemServico = this.ordemServicoService.findAllOrdemServico();

        ModelAndView mv = new ModelAndView("ordemservico/index");
        mv.addObject("ordemServico", ordemServico);

        return mv;
    }

    @GetMapping("/new")
    public ModelAndView paginaCadastro(RequisicaoOrdemServico requisicao) {

        List<Cliente> clientes = this.clienteService.findAllClientes();
        List<Tecnico> tecnicos = this.tecnicoService.findAllTecnicos();
        List<StatusOrdemServico> status = this.statusOrdemServicoService.findAllStatusOrdemServico();

        ModelAndView mv = new ModelAndView("ordemservico/new");
        mv.addObject("status", status);
        mv.addObject("clientes", clientes);
        mv.addObject("tecnicos", tecnicos);
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
            this.ordemServicoService.saveOrdemServico(ordemServico);
            return new ModelAndView("redirect:/ordemservico");
        }

    }

    @GetMapping("/{id}/edit")
    public ModelAndView editar(@PathVariable Long id, RequisicaoOrdemServico requisicao) {
        Optional<OrdemServico> optional = this.ordemServicoService.findOrdemServicoById(id);
        List<StatusOrdemServico> status = this.statusOrdemServicoService.findAllStatusOrdemServico();
        List<Servico> servico = this.servicoService.findAllServicos();
        List<Produto> produto = this.produtoService.findAllProdutos();

        if (optional.isPresent()) {
            OrdemServico ordemServico = optional.get();
            requisicao.fromOS(ordemServico);
            ModelAndView mv = new ModelAndView("ordemservico/edit");
            mv.addObject("ordemServicoId", ordemServico.getId());
            mv.addObject("status", status);
            mv.addObject("statusSelecionado", requisicao.getStatusOrdemServico());
            mv.addObject("servico", servico);
            mv.addObject("produto", produto);
            mv.addObject("produtoSelecionado", requisicao.getProduto());

            return mv;
        } else {
            System.out.println("Ordem de Serviço Não Encontrada");
            return new ModelAndView("redirect:/ordemservico");
        }

    }

    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoOrdemServico requisicao, BindingResult erro) {

        if (erro.hasErrors()) {
            ModelAndView mv = new ModelAndView("ordemservico/edit");
            mv.addObject("ordemServicoId", id);
            System.out.println(erro);
            return mv;
        } else {
            Optional<OrdemServico> optional = this.ordemServicoService.findOrdemServicoById(id);
            if (optional.isPresent()) {
                OrdemServico ordemServico = requisicao.toOrdemServico(optional.get());
                this.ordemServicoService.saveOrdemServico(ordemServico);
                return new ModelAndView("redirect:/ordemservico");
            } else {
                System.out.println("OS Não Encontrada!");
                return new ModelAndView("redirect:/ordemservico");
            }
        }

    }

    @PostMapping("/{id}/produtoadd")
    public ModelAndView adicionarProduto(@PathVariable Long id, @Valid RequisicaoProdutoOrdem requisicaoProdutoOrdem, BindingResult erro, RequisicaoOrdemServico requisicaoOrdemServico) {

        ProdutoOrdem produtoOrdem = requisicaoProdutoOrdem.toProdutoOrdem();

        if (erro.hasErrors()) {
            ModelAndView mv = new ModelAndView("ordemservico/new");
            System.out.println(erro);
            return mv;

        } else {
            Optional<OrdemServico> optional = this.ordemServicoService.findOrdemServicoById(id);
            OrdemServico ordemServico = requisicaoOrdemServico.fromOSProdutoAdd(optional.get());
            produtoOrdem.setOrdemServico(ordemServico);
            produtoOrdemRepository.save(produtoOrdem);
            return new ModelAndView("redirect:/ordemservico/{id}/edit");
        }

    }

    @PostMapping("/{id}/servicoadd")
    public ModelAndView adicionarServico(@PathVariable Long id, @Valid RequisicaoServicoOrdem requisicaoServicoOrdem, BindingResult erro, RequisicaoOrdemServico requisicaoOrdemServico){
        ServicoOrdem servicoOrdem = requisicaoServicoOrdem.toServicoOrdem();

        if (erro.hasErrors()) {
            ModelAndView mv = new ModelAndView("ordemservico/new");
            System.out.println(erro);
            return mv;
        }else{
            Optional<OrdemServico> optional = this.ordemServicoService.findOrdemServicoById(id);
            OrdemServico ordemServico = requisicaoOrdemServico.fromOSServicoAdd(optional.get());
            servicoOrdem.setOrdemServico(ordemServico);
            servicoOrdemRepository.save(servicoOrdem);
            return new ModelAndView("redirect:/ordemservico/{id}/edit");
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        try {
            this.ordemServicoService.deleteOrdemServico(id);
            return "redirect:/ordemservico";
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Os Nao Encontrada");
            return "redirect:/ordemservico";

        }
    }

}
