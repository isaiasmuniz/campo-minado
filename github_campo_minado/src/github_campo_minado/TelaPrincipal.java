package github_campo_minado;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame{
	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(2, 2, 2);
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("campo minado");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
	}

}
