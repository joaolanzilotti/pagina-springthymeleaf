
package springthymeleaf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class OrdemServico implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dataInicial = new Date();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataFinal;

    //@Enumerated -> vai ser enumerada e com o EnumType.STRING vai fazer ele vir em forma de Texto no banco de Dados!
    @Enumerated(EnumType.STRING)
    private StatusOrdemServico statusOrdemServico;

    //Muitos para UM - Muitas Listas de Compras para um Cliente -  e la na classe Cliente tem que usar o @OneToMany(mappedby = "cliente")
    //@JoinColumn(name = "") -> Especifica qual nome da foreign key
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    //Muitos para UM - Muitas Listas de Compras para um Cliente - e la na classe Servicos tem que usar o @OneToMany(mappedby = "servicos")
    //@JoinColumn(name = "") -> Especifica qual nome da foreign key
    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public OrdemServico() {
    }

    public OrdemServico(Cliente cliente, Date dataFinal, Long id, Produto produto, Servico servico, StatusOrdemServico statusOrdemServico) {
        this.cliente = cliente;
        this.dataFinal = dataFinal;
        this.id = id;
        this.produto = produto;
        this.servico = servico;
        this.statusOrdemServico = statusOrdemServico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public StatusOrdemServico getStatusOrdemServico() {
        return statusOrdemServico;
    }

    public void setStatusOrdemServico(StatusOrdemServico statusOrdemServico) {
        this.statusOrdemServico = statusOrdemServico;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrdemServico other = (OrdemServico) obj;
        return Objects.equals(this.id, other.id);
    }

    

    
    
    
}
