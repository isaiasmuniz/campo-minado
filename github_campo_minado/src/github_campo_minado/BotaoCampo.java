package github_campo_minado;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener{
	
	private final Color BG_PADRAO = new Color(36, 142, 150);
	private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		setOpaque(true);
		addMouseListener(this);
		this.campo = campo;
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		campo.registrarObservadores(this);
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEventos evento) {
		switch(evento) {
		case ABRIR:
			aplicarEstilorAbrir();
			break;
		case MARCAR:
			aplicarEstilorMarcar();
			break;
		case EXPLODIR:
			aplicarEstilorExplodir();
			break;
			default:
			setBorder(BorderFactory.createBevelBorder(0));
			aplicarEstilorPadrao();
			break;
		}
		
	}

	private void aplicarEstilorPadrao() {
		setBackground(BG_PADRAO);
		setText("");
		
	}

	private void aplicarEstilorExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");
	}

	private void aplicarEstilorMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicarEstilorAbrir() {
		
		setBorder(BorderFactory.createLineBorder(Color.black));
		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		
		setBackground(Color.GRAY);
		
		switch(campo.minasNasVizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
			default:
				setForeground(Color.PINK);
		}
		
		String valor = !campo.vizinhancaSegura() ? campo.minasNasVizinhanca() + "" : "";
		setText(valor);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			campo.abrir();
		}else {
			campo.alterarMarcacao();
		}
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
