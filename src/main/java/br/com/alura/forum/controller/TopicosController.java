package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
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
	@Cacheable(value = "listaTopicos")
	public Page<TopicoDTO> listar(@RequestParam(required = false) String cursoNome,
										Pageable pagina){
		
		return cursoNome == null ? listAll(cursoNome,pagina):listByName(cursoNome,pagina);
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaTopicos", allEntries = true)
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
	@CacheEvict(value = "listaTopicos", allEntries = true)
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
	@CacheEvict(value = "listaTopicos", allEntries = true)
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		Optional<Topico> opt = dao.findById(id);
		if(opt.isPresent()) {
			dao.deleteById(id);
			return ResponseEntity.ok().build();
			
		}
		return ResponseEntity.notFound().build();
	}
	
	private Page<TopicoDTO> listAll(@RequestParam String cursoNome,@RequestParam Pageable paginacao){
			Page<Topico> topicos = dao.findAll(paginacao);
			return TopicoDTO.converter(topicos);			
	}
	
	private Page<TopicoDTO> listByName(String cursoNome,@RequestParam Pageable paginacao){
		Page<Topico> topicos = dao.findByCursoNome(cursoNome,paginacao);
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
		return dao.findById(id).orElseThrow();
	}
	
	
	
}
