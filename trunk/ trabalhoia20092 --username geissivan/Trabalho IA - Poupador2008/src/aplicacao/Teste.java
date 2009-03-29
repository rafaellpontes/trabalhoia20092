/**
 * 
 */
package aplicacao;

import br.com.unifor.ia.util.Ambiente;

/**
 * @author Geissivan
 *
 */
public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Ambiente ambiente = new Ambiente();
		
		/*for (int i = 0; i < 30; i++) {			
			for (int j = 0; j < 30; j++) {
				Point p = new Point();
				System.out.print("["+i+","+j+"] ");
			}
			System.out.println();
		}
		
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		
		Point p = new Point(2, 2);		
		for (int x = new Double(p.getX()).intValue() - 2; x <= new Double(p.getX()).intValue()+2; x++) {
			
			for (int y = new Double(p.getY()).intValue() - 2; y <= new Double(p.getY()).intValue()+2; y++) {				
				System.out.print("["+x+","+y+"] ");
			}
			System.out.println();
		}*/
		
		while(true){
			System.out.println((int) (Math.random() * 5));
		}
		
		
		
		
	}

}
