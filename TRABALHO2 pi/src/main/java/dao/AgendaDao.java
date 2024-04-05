package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import entidades.Agenda;

public class AgendaDao {
	
	private EntityManager entityManager;
	

    public AgendaDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	public void salvar(Agenda agenda) {
		try {
            entityManager.getTransaction().begin();
            entityManager.persist(agenda);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
	}
	
	public void excluir(Agenda agenda) {
        try {
            entityManager.getTransaction().begin();
            agenda = entityManager.merge(agenda);
            entityManager.remove(agenda);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
	
	public List<Agenda> listar() {
        try {
            TypedQuery<Agenda> query = entityManager.createQuery("SELECT l FROM Agenda l order by dataHoraConsulta", Agenda.class);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
	
	public List<Agenda> buscarPorDataHoraEMedico(Integer id, Date dataHoraConsulta, String medico) {
        TypedQuery<Agenda> query = entityManager.createQuery(
                "SELECT a FROM Agenda a WHERE a.id != :id AND a.dataHoraConsulta = :dataHoraConsulta AND a.medico = :medico",
                Agenda.class);
        query.setParameter("id", id = (id == null) ? 0 : id);
        query.setParameter("dataHoraConsulta", dataHoraConsulta);
        query.setParameter("medico", medico);
        System.out.println("SELECT a FROM Agenda a WHERE a.id !="+id+" AND a.dataHoraConsulta = "+dataHoraConsulta+""
        		+ "AND a.medico = "+medico);
        return query.getResultList();
    }
	
	public int contarAgendamentos() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(a) FROM Agenda a", Long.class);
        Long resultado = query.getSingleResult();
        return resultado != null ? resultado.intValue() : 0;
    }


}
