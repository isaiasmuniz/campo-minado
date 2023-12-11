package github_campo_minado;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	private final int linhas;
	private final int colunas;
	private boolean aberto;
	private boolean marcado;
	private boolean minado;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();

	public Campo(int linhas, int colunas) {
		super();
		this.linhas = linhas;
		this.colunas = colunas;
	}
	
	public void registrarObservadores(CampoObservador obsevador) {
		observadores.add(obsevador);
	}
	
	private void notificarObservador(CampoEventos evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	boolean addVizinhos(Campo vizinho) {
		boolean linhaDiferente = linhas != vizinho.linhas;
		boolean colunaDiferente = colunas != vizinho.colunas;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int linha = Math.abs(linhas - vizinho.linhas);
		int coluna = Math.abs(colunas - vizinho.colunas);
		int geral = linha + coluna;
		
		if(geral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		}else if(geral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		}else {
			return false;
		}
		
	}

	public void alterarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
			if(marcado) {
				notificarObservador(CampoEventos.MARCAR);
			}else {
				notificarObservador(CampoEventos.DESMARCAR);
			}
		}
	}

	boolean abrir() {
		if(!aberto && !marcado) {
			aberto = true;
			if(minado) {
				notificarObservador(CampoEventos.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			if(vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		}else {
			return false;
		}
	}

	boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	public boolean isMarcado() {
		return marcado;
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFecahdo() {
		return !aberto;
	}

	public boolean isMinado() {
		return minado;
	}
	
 	void minar(){
		minado = true;
	}

 	void setAberto(boolean aberto) {
 		this.aberto = aberto;
 		
 		if(aberto) {
 			notificarObservador(CampoEventos.ABRIR);
 		}
 	}

 	public int getLinha() {
 		return linhas;
 	}
 	
 	public int getColunas() {
 		return colunas;
 	}

 	boolean objetivoAlcancado() {
 		boolean desvendado = !minado && aberto;
 		boolean protegido = minado && marcado;
 		return desvendado || protegido;
 	}

 	public int minasNasVizinhanca() {
 		return (int)vizinhos.stream().filter(v -> v.minado).count();
 	}

 	void reiniciar() {
 		aberto = false;
 		marcado = false;
 		minado = false;
 		notificarObservador(CampoEventos.REINICIAR);
 	}
}

