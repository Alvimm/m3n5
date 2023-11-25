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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Filipe
 */
@Entity
@Table(name = "pessoafisica")
@NamedQueries({
    @NamedQuery(name = "Pessoafisica.findAll", query = "SELECT p FROM Pessoafisica p"),
    @NamedQuery(name = "Pessoafisica.findByIdpessoa", query = "SELECT p FROM Pessoafisica p WHERE p.idpessoa = :idpessoa"),
    @NamedQuery(name = "Pessoafisica.findByCpf", query = "SELECT p FROM Pessoafisica p WHERE p.cpf = :cpf")})
public class Pessoafisica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idpessoa")
    private Integer idpessoa;
    @Column(name = "cpf")
    private String cpf;
    @JoinColumn(name = "idpessoa", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pessoa pessoa;

    public Pessoafisica() {
    }

    public Pessoafisica(Integer idpessoa) {
        this.idpessoa = idpessoa;
    }

    public Integer getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Integer idpessoa) {
        this.idpessoa = idpessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpessoa != null ? idpessoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pessoafisica)) {
            return false;
        }
        Pessoafisica other = (Pessoafisica) object;
        if ((this.idpessoa == null && other.idpessoa != null) || (this.idpessoa != null && !this.idpessoa.equals(other.idpessoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroserver.model.Pessoafisica[ idpessoa=" + idpessoa + " ]";
    }
    
}
