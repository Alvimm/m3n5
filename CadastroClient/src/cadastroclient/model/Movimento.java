/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Filipe
 */
@Entity
@Table(name = "movimento")
@NamedQueries({
    @NamedQuery(name = "Movimento.findAll", query = "SELECT m FROM Movimento m"),
    @NamedQuery(name = "Movimento.findByIdmovimento", query = "SELECT m FROM Movimento m WHERE m.idmovimento = :idmovimento"),
    @NamedQuery(name = "Movimento.findByQuantidade", query = "SELECT m FROM Movimento m WHERE m.quantidade = :quantidade"),
    @NamedQuery(name = "Movimento.findByTipo", query = "SELECT m FROM Movimento m WHERE m.tipo = :tipo"),
    @NamedQuery(name = "Movimento.findByValorunitario", query = "SELECT m FROM Movimento m WHERE m.valorunitario = :valorunitario")})
public class Movimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idmovimento")
    private Integer idmovimento;
    @Column(name = "quantidade")
    private String quantidade;
    @Column(name = "tipo")
    private Character tipo;
    @Column(name = "valorunitario")
    private String valorunitario;
    @JoinColumn(name = "idpessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa idpessoa;
    @JoinColumn(name = "idproduto", referencedColumnName = "idproduto")
    @ManyToOne
    private Produto idproduto;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario idusuario;

    public Movimento() {
    }

    public Movimento(Integer idmovimento) {
        this.idmovimento = idmovimento;
    }

    public Integer getIdmovimento() {
        return idmovimento;
    }

    public void setIdmovimento(Integer idmovimento) {
        this.idmovimento = idmovimento;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public String getValorunitario() {
        return valorunitario;
    }

    public void setValorunitario(String valorunitario) {
        this.valorunitario = valorunitario;
    }

    public Pessoa getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Pessoa idpessoa) {
        this.idpessoa = idpessoa;
    }

    public Produto getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(Produto idproduto) {
        this.idproduto = idproduto;
    }

    public Usuario getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmovimento != null ? idmovimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimento)) {
            return false;
        }
        Movimento other = (Movimento) object;
        if ((this.idmovimento == null && other.idmovimento != null) || (this.idmovimento != null && !this.idmovimento.equals(other.idmovimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroserver.model.Movimento[ idmovimento=" + idmovimento + " ]";
    }
    
}
