package github_campo_minado;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampo();
		associarVizinhas();
		sortearMinas();
	}
	
	public void paraCada(Consumer<Campo>funcao) {
		campos.forEach(funcao);
	}
	
	public void registrarObservador(Consumer<ResultadoEvento>observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}
	
	public void abrir(int linhas, int colunas) {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linhas && c.getColunas() == colunas)
			.findFirst()
			.ifPresent(c -> c.abrir());
	}

	public void alterarMarcacao(int linhas, int colunas) {
		campos.parallelStream()
		.filter(c -> c.getLinha() == linhas && c.getColunas() == colunas)
		.findFirst()
		.ifPresent(c -> c.alterarMarcacao());
	}
	
	private void mostrarMinas() {
		campos.stream()
		.filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
	}

	private void sortearMinas() {
		long minasArmasdas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmasdas = campos.stream().filter(minado).count();
		}while(minasArmasdas < minas);
	}

	private void associarVizinhas() {
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.addVizinhos(c2);
			}
		}
	}
	
	private void gerarCampo() {
		for(int l = 0; l < linhas; l++) {
			for(int c = 0; c < colunas; c++) {
				Campo campo = new Campo(l, c);
				campo.registrarObservadores(this);
				campos.add(campo);
			}
		}
	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
		
	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEventos evento) {
		if(evento == CampoEventos.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		}else if(objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}
	
}
