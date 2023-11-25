/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.model;

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
@Table(name = "pessoajuridica")
@NamedQueries({
    @NamedQuery(name = "Pessoajuridica.findAll", query = "SELECT p FROM Pessoajuridica p"),
    @NamedQuery(name = "Pessoajuridica.findByIdpessoa", query = "SELECT p FROM Pessoajuridica p WHERE p.idpessoa = :idpessoa"),
    @NamedQuery(name = "Pessoajuridica.findByCnpj", query = "SELECT p FROM Pessoajuridica p WHERE p.cnpj = :cnpj")})
public class Pessoajuridica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idpessoa")
    private Integer idpessoa;
    @Column(name = "cnpj")
    private String cnpj;
    @JoinColumn(name = "idpessoa", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pessoa pessoa;

    public Pessoajuridica() {
    }

    public Pessoajuridica(Integer idpessoa) {
        this.idpessoa = idpessoa;
    }

    public Integer getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Integer idpessoa) {
        this.idpessoa = idpessoa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
        if (!(object instanceof Pessoajuridica)) {
            return false;
        }
        Pessoajuridica other = (Pessoajuridica) object;
        if ((this.idpessoa == null && other.idpessoa != null) || (this.idpessoa != null && !this.idpessoa.equals(other.idpessoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroserver.model.Pessoajuridica[ idpessoa=" + idpessoa + " ]";
    }
    
}
