/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.controller;

import cadastroserver.controller.exceptions.IllegalOrphanException;
import cadastroserver.controller.exceptions.NonexistentEntityException;
import cadastroserver.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cadastroserver.model.Pessoajuridica;
import cadastroserver.model.Pessoafisica;
import cadastroserver.model.Movimento;
import cadastroserver.model.Pessoa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Filipe
 */
public class PessoaJpaController implements Serializable {

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) throws PreexistingEntityException, Exception {
        if (pessoa.getMovimentoCollection() == null) {
            pessoa.setMovimentoCollection(new ArrayList<Movimento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoajuridica pessoajuridica = pessoa.getPessoajuridica();
            if (pessoajuridica != null) {
                pessoajuridica = em.getReference(pessoajuridica.getClass(), pessoajuridica.getIdpessoa());
                pessoa.setPessoajuridica(pessoajuridica);
            }
            Pessoafisica pessoafisica = pessoa.getPessoafisica();
            if (pessoafisica != null) {
                pessoafisica = em.getReference(pessoafisica.getClass(), pessoafisica.getIdpessoa());
                pessoa.setPessoafisica(pessoafisica);
            }
            Collection<Movimento> attachedMovimentoCollection = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionMovimentoToAttach : pessoa.getMovimentoCollection()) {
                movimentoCollectionMovimentoToAttach = em.getReference(movimentoCollectionMovimentoToAttach.getClass(), movimentoCollectionMovimentoToAttach.getIdmovimento());
                attachedMovimentoCollection.add(movimentoCollectionMovimentoToAttach);
            }
            pessoa.setMovimentoCollection(attachedMovimentoCollection);
            em.persist(pessoa);
            if (pessoajuridica != null) {
                Pessoa oldPessoaOfPessoajuridica = pessoajuridica.getPessoa();
                if (oldPessoaOfPessoajuridica != null) {
                    oldPessoaOfPessoajuridica.setPessoajuridica(null);
                    oldPessoaOfPessoajuridica = em.merge(oldPessoaOfPessoajuridica);
                }
                pessoajuridica.setPessoa(pessoa);
                pessoajuridica = em.merge(pessoajuridica);
            }
            if (pessoafisica != null) {
                Pessoa oldPessoaOfPessoafisica = pessoafisica.getPessoa();
                if (oldPessoaOfPessoafisica != null) {
                    oldPessoaOfPessoafisica.setPessoafisica(null);
                    oldPessoaOfPessoafisica = em.merge(oldPessoaOfPessoafisica);
                }
                pessoafisica.setPessoa(pessoa);
                pessoafisica = em.merge(pessoafisica);
            }
            for (Movimento movimentoCollectionMovimento : pessoa.getMovimentoCollection()) {
                Pessoa oldIdpessoaOfMovimentoCollectionMovimento = movimentoCollectionMovimento.getIdpessoa();
                movimentoCollectionMovimento.setIdpessoa(pessoa);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
                if (oldIdpessoaOfMovimentoCollectionMovimento != null) {
                    oldIdpessoaOfMovimentoCollectionMovimento.getMovimentoCollection().remove(movimentoCollectionMovimento);
                    oldIdpessoaOfMovimentoCollectionMovimento = em.merge(oldIdpessoaOfMovimentoCollectionMovimento);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoa(pessoa.getId()) != null) {
                throw new PreexistingEntityException("Pessoa " + pessoa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getId());
            Pessoajuridica pessoajuridicaOld = persistentPessoa.getPessoajuridica();
            Pessoajuridica pessoajuridicaNew = pessoa.getPessoajuridica();
            Pessoafisica pessoafisicaOld = persistentPessoa.getPessoafisica();
            Pessoafisica pessoafisicaNew = pessoa.getPessoafisica();
            Collection<Movimento> movimentoCollectionOld = persistentPessoa.getMovimentoCollection();
            Collection<Movimento> movimentoCollectionNew = pessoa.getMovimentoCollection();
            List<String> illegalOrphanMessages = null;
            if (pessoajuridicaOld != null && !pessoajuridicaOld.equals(pessoajuridicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pessoajuridica " + pessoajuridicaOld + " since its pessoa field is not nullable.");
            }
            if (pessoafisicaOld != null && !pessoafisicaOld.equals(pessoafisicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pessoafisica " + pessoafisicaOld + " since its pessoa field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pessoajuridicaNew != null) {
                pessoajuridicaNew = em.getReference(pessoajuridicaNew.getClass(), pessoajuridicaNew.getIdpessoa());
                pessoa.setPessoajuridica(pessoajuridicaNew);
            }
            if (pessoafisicaNew != null) {
                pessoafisicaNew = em.getReference(pessoafisicaNew.getClass(), pessoafisicaNew.getIdpessoa());
                pessoa.setPessoafisica(pessoafisicaNew);
            }
            Collection<Movimento> attachedMovimentoCollectionNew = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionNewMovimentoToAttach : movimentoCollectionNew) {
                movimentoCollectionNewMovimentoToAttach = em.getReference(movimentoCollectionNewMovimentoToAttach.getClass(), movimentoCollectionNewMovimentoToAttach.getIdmovimento());
                attachedMovimentoCollectionNew.add(movimentoCollectionNewMovimentoToAttach);
            }
            movimentoCollectionNew = attachedMovimentoCollectionNew;
            pessoa.setMovimentoCollection(movimentoCollectionNew);
            pessoa = em.merge(pessoa);
            if (pessoajuridicaNew != null && !pessoajuridicaNew.equals(pessoajuridicaOld)) {
                Pessoa oldPessoaOfPessoajuridica = pessoajuridicaNew.getPessoa();
                if (oldPessoaOfPessoajuridica != null) {
                    oldPessoaOfPessoajuridica.setPessoajuridica(null);
                    oldPessoaOfPessoajuridica = em.merge(oldPessoaOfPessoajuridica);
                }
                pessoajuridicaNew.setPessoa(pessoa);
                pessoajuridicaNew = em.merge(pessoajuridicaNew);
            }
            if (pessoafisicaNew != null && !pessoafisicaNew.equals(pessoafisicaOld)) {
                Pessoa oldPessoaOfPessoafisica = pessoafisicaNew.getPessoa();
                if (oldPessoaOfPessoafisica != null) {
                    oldPessoaOfPessoafisica.setPessoafisica(null);
                    oldPessoaOfPessoafisica = em.merge(oldPessoaOfPessoafisica);
                }
                pessoafisicaNew.setPessoa(pessoa);
                pessoafisicaNew = em.merge(pessoafisicaNew);
            }
            for (Movimento movimentoCollectionOldMovimento : movimentoCollectionOld) {
                if (!movimentoCollectionNew.contains(movimentoCollectionOldMovimento)) {
                    movimentoCollectionOldMovimento.setIdpessoa(null);
                    movimentoCollectionOldMovimento = em.merge(movimentoCollectionOldMovimento);
                }
            }
            for (Movimento movimentoCollectionNewMovimento : movimentoCollectionNew) {
                if (!movimentoCollectionOld.contains(movimentoCollectionNewMovimento)) {
                    Pessoa oldIdpessoaOfMovimentoCollectionNewMovimento = movimentoCollectionNewMovimento.getIdpessoa();
                    movimentoCollectionNewMovimento.setIdpessoa(pessoa);
                    movimentoCollectionNewMovimento = em.merge(movimentoCollectionNewMovimento);
                    if (oldIdpessoaOfMovimentoCollectionNewMovimento != null && !oldIdpessoaOfMovimentoCollectionNewMovimento.equals(pessoa)) {
                        oldIdpessoaOfMovimentoCollectionNewMovimento.getMovimentoCollection().remove(movimentoCollectionNewMovimento);
                        oldIdpessoaOfMovimentoCollectionNewMovimento = em.merge(oldIdpessoaOfMovimentoCollectionNewMovimento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoa.getId();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pessoajuridica pessoajuridicaOrphanCheck = pessoa.getPessoajuridica();
            if (pessoajuridicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Pessoajuridica " + pessoajuridicaOrphanCheck + " in its pessoajuridica field has a non-nullable pessoa field.");
            }
            Pessoafisica pessoafisicaOrphanCheck = pessoa.getPessoafisica();
            if (pessoafisicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Pessoafisica " + pessoafisicaOrphanCheck + " in its pessoafisica field has a non-nullable pessoa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Movimento> movimentoCollection = pessoa.getMovimentoCollection();
            for (Movimento movimentoCollectionMovimento : movimentoCollection) {
                movimentoCollectionMovimento.setIdpessoa(null);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
