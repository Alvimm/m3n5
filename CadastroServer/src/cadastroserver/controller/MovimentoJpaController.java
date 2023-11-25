/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.controller;

import cadastroserver.controller.exceptions.NonexistentEntityException;
import cadastroserver.controller.exceptions.PreexistingEntityException;
import cadastroserver.model.Movimento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cadastroserver.model.Pessoa;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Filipe
 */
public class MovimentoJpaController implements Serializable {

    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa idpessoa = movimento.getIdpessoa();
            if (idpessoa != null) {
                idpessoa = em.getReference(idpessoa.getClass(), idpessoa.getId());
                movimento.setIdpessoa(idpessoa);
            }
            Produto idproduto = movimento.getIdproduto();
            if (idproduto != null) {
                idproduto = em.getReference(idproduto.getClass(), idproduto.getIdproduto());
                movimento.setIdproduto(idproduto);
            }
            Usuario idusuario = movimento.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                movimento.setIdusuario(idusuario);
            }
            em.persist(movimento);
            if (idpessoa != null) {
                idpessoa.getMovimentoCollection().add(movimento);
                idpessoa = em.merge(idpessoa);
            }
            if (idproduto != null) {
                idproduto.getMovimentoCollection().add(movimento);
                idproduto = em.merge(idproduto);
            }
            if (idusuario != null) {
                idusuario.getMovimentoCollection().add(movimento);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimento(movimento.getIdmovimento()) != null) {
                throw new PreexistingEntityException("Movimento " + movimento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimento movimento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimento persistentMovimento = em.find(Movimento.class, movimento.getIdmovimento());
            Pessoa idpessoaOld = persistentMovimento.getIdpessoa();
            Pessoa idpessoaNew = movimento.getIdpessoa();
            Produto idprodutoOld = persistentMovimento.getIdproduto();
            Produto idprodutoNew = movimento.getIdproduto();
            Usuario idusuarioOld = persistentMovimento.getIdusuario();
            Usuario idusuarioNew = movimento.getIdusuario();
            if (idpessoaNew != null) {
                idpessoaNew = em.getReference(idpessoaNew.getClass(), idpessoaNew.getId());
                movimento.setIdpessoa(idpessoaNew);
            }
            if (idprodutoNew != null) {
                idprodutoNew = em.getReference(idprodutoNew.getClass(), idprodutoNew.getIdproduto());
                movimento.setIdproduto(idprodutoNew);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                movimento.setIdusuario(idusuarioNew);
            }
            movimento = em.merge(movimento);
            if (idpessoaOld != null && !idpessoaOld.equals(idpessoaNew)) {
                idpessoaOld.getMovimentoCollection().remove(movimento);
                idpessoaOld = em.merge(idpessoaOld);
            }
            if (idpessoaNew != null && !idpessoaNew.equals(idpessoaOld)) {
                idpessoaNew.getMovimentoCollection().add(movimento);
                idpessoaNew = em.merge(idpessoaNew);
            }
            if (idprodutoOld != null && !idprodutoOld.equals(idprodutoNew)) {
                idprodutoOld.getMovimentoCollection().remove(movimento);
                idprodutoOld = em.merge(idprodutoOld);
            }
            if (idprodutoNew != null && !idprodutoNew.equals(idprodutoOld)) {
                idprodutoNew.getMovimentoCollection().add(movimento);
                idprodutoNew = em.merge(idprodutoNew);
            }
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getMovimentoCollection().remove(movimento);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getMovimentoCollection().add(movimento);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimento.getIdmovimento();
                if (findMovimento(id) == null) {
                    throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimento movimento;
            try {
                movimento = em.getReference(Movimento.class, id);
                movimento.getIdmovimento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.", enfe);
            }
            Pessoa idpessoa = movimento.getIdpessoa();
            if (idpessoa != null) {
                idpessoa.getMovimentoCollection().remove(movimento);
                idpessoa = em.merge(idpessoa);
            }
            Produto idproduto = movimento.getIdproduto();
            if (idproduto != null) {
                idproduto.getMovimentoCollection().remove(movimento);
                idproduto = em.merge(idproduto);
            }
            Usuario idusuario = movimento.getIdusuario();
            if (idusuario != null) {
                idusuario.getMovimentoCollection().remove(movimento);
                idusuario = em.merge(idusuario);
            }
            em.remove(movimento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movimento> findMovimentoEntities() {
        return findMovimentoEntities(true, -1, -1);
    }

    public List<Movimento> findMovimentoEntities(int maxResults, int firstResult) {
        return findMovimentoEntities(false, maxResults, firstResult);
    }

    private List<Movimento> findMovimentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimento.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Movimento findMovimento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimento> rt = cq.from(Movimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
