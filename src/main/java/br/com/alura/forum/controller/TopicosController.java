package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Curso;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	@Autowired
	private TopicoRepository dao;
	
	@Autowired
	private CursoRepository cursoDAO;
	
	@GetMapping
	public List<TopicoDTO> listar(String cursoNome){
		return cursoNome == null ? listAll(cursoNome):listByName(cursoNome);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form,UriComponentsBuilder uriBuilder) {
		Topico topico = getTopicoByCurso(form);
		dao.save(topico);
		return ResponseEntity.created(getUri(form,uriBuilder)).body(new TopicoDTO(getTopicoByCurso(form)));
	}
	
	@GetMapping("/{id}")
	public DetalhesTopicoDTO detalhar(@PathVariable Long id) {
		return new DetalhesTopicoDTO(getTopico(id));
	}
	
	@PutMapping("{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id,@RequestBody @Valid AtualizacaoTopicoForm form){
		Optional<Topico> opt = dao.findById(id);
		if(opt.isPresent()) {
			Topico topico = form.atualizar(id,dao);
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		Optional<Topico> opt = dao.findById(id);
		if(opt.isPresent()) {
			dao.deleteById(id);
			return ResponseEntity.ok().build();
			
		}
		return ResponseEntity.notFound().build();
	}
	
	private List<TopicoDTO> listAll(String cursoNome){
			List<Topico> topicos = dao.findAll();
			return TopicoDTO.converter(topicos);			
	}
	
	private List<TopicoDTO> listByName(String cursoNome){
		List<Topico> topicos = dao.findByCursoNome(cursoNome);
		return TopicoDTO.converter(topicos);
	}
	
	private URI getUri(TopicoForm form,UriComponentsBuilder uriBuilder) {
		URI uri = uriBuilder.path("topicos/{id}").buildAndExpand(getTopicoByCurso(form).getId()).toUri();
		return uri;
		
	}
	
	private Topico getTopicoByCurso(TopicoForm form) {
		 return form.convert(cursoDAO);
	}
	
	private Topico getTopico(Long id) {
		Optional<Topico> optional = dao.findById(id);
		Topico topico = optional.orElseThrow();
		return topico;
		
	}
	
	
	
}
