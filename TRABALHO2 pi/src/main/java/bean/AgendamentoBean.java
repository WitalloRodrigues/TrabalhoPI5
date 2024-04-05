package bean;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import entidades.Agenda;
import util.JPAUtil;
import dao.AgendaDao;

@ManagedBean
@ApplicationScoped
public class AgendamentoBean {
	
	private Agenda agenda;
    private AgendaDao AgendaDao;
    private List<Agenda> agendas;
    private int quantidadeAgendamentos;
    
    
    public String listagem() {
    	limparCampos();
    	contarAgendamentos();
    	return "agendamentos";
    }
	
    
    public void limparCampos() {
    	agenda = new Agenda();
    }
    
    private void atualizarListaagendas() {
    	agendas = AgendaDao.listar();
    }
    
    public AgendamentoBean() {
    	agenda = new Agenda();
    	AgendaDao = new AgendaDao(JPAUtil.getEntityManager());
        atualizarListaagendas();
        contarAgendamentos();
    }
    
    private boolean agendamentoJaExiste() {
        List<Agenda> agendamentosExistentes = AgendaDao.buscarPorDataHoraEMedico(agenda.getId(), agenda.getDataHoraConsulta(), agenda.getMedico());
        return !agendamentosExistentes.isEmpty();
    }
    
    public void salvar() {
    	
    	if (agendamentoJaExiste()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar o agendamento. "
                    		+ "Já existe um agendamento para a mesma data, hora e médico.",
                            ""));
            return;
        }
    	
        try {
        	
        	
        	
        	if(agenda.getId() == null ) {
        		
        		FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("agenda salvo com sucesso."));
        		AgendaDao.salvar(agenda);
        		limparCampos();
        		
        	}else {
        		
        		FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("agenda editada com sucesso."));
        		AgendaDao.salvar(agenda);
        	}
            
        	
        	
            
            atualizarListaagendas();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao salvar o agenda.", null));
        }
    }
    
    public void excluir(Agenda agenda) {
        try {
        	AgendaDao.excluir(agenda);
        	atualizarListaagendas();
        	contarAgendamentos();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("agenda excluído com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao excluir o agenda.", null));
        }
    }
    
    
    public String editar(Agenda agenda) {
    	this.agenda = agenda;
		return "itens_enviados";
	}


    
	public void exibir(Agenda agenda) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = sdf.format(agenda.getDataHoraConsulta());
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Lançamento selecionado =>    "
                		+ "ID : " + agenda.getId() + " |  "
        				+ "Paciente : " + agenda.getPaciente() + " |  "
						+ "Medico : "+ agenda.getMedico() + " | "
						+ "Data : " +dataFormatada));
        
        
    }
    
    public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public AgendaDao getAgendaDao() {
		return AgendaDao;
	}

	public void setAgendaDao(AgendaDao agendaDao) {
		AgendaDao = agendaDao;
	}

	public List<Agenda> getAgendas() {
        List<Agenda> agendas = AgendaDao.listar();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (Agenda agenda : agendas) {
            String dataFormatada = sdf.format(agenda.getDataHoraConsulta());
            agenda.setDataFormatada(dataFormatada);
        }

        return agendas;
    }

	public void setAgendas(List<Agenda> agendas) {
		this.agendas = agendas;
	}
	
	public void contarAgendamentos() {
        quantidadeAgendamentos = AgendaDao.contarAgendamentos();
    }
	
	public void mostrarContarAgendamentos() {
		quantidadeAgendamentos = AgendaDao.contarAgendamentos();
		FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Quantidade de agendamentos => "+ quantidadeAgendamentos));
	}


	public int getQuantidadeAgendamentos() {
		return quantidadeAgendamentos;
	}


	public void setQuantidadeAgendamentos(int quantidadeAgendamentos) {
		this.quantidadeAgendamentos = quantidadeAgendamentos;
	}

	
    
    
    
	

}
