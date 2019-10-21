package br.com.alura.forum.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.springframework.lang.NonNull;

import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class AtualizacaoTopicoForm {
	
	@NonNull @NotEmpty
	private String titulo;
	@NonNull @NotEmpty
	private String mensagem;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public Topico atualizar(Long id, TopicoRepository dao) {
		Optional<Topico> opt = dao.findById(id);
		Topico topico = null;
		if(opt.isPresent()) {
			topico = opt.get();
			topico.setTitulo(this.titulo);
			topico.setMensagem(this.mensagem);	
		}
		return topico;	
	}
	
	

}
