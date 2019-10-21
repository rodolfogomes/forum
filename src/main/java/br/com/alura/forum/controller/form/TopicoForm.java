package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotEmpty;

import org.springframework.lang.NonNull;

import br.com.alura.forum.model.Curso;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;

public class TopicoForm {
	@NonNull @NotEmpty
	private String titulo;
	@NonNull @NotEmpty
	private String mensagem;
	@NonNull @NotEmpty
	private String nomeCurso;
	
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
	public String getNomeCurso() {
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	
	public Topico convert(CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(nomeCurso);
		return new Topico(titulo,mensagem,curso);
	}
	
	

}
