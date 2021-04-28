package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap; //creo una identity map, la passo al dao così lui evita ogni volta di fare una new rischiando di fare errori, gli passiamo la struttura dati da riempire
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
	}

	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		//Aggiungere vertici
		//1. -> recupero tutti gli ArtObjects dal db
		//2. -> li inserisco come vertici
		
		//List<ArtObject> vertici = dao.listObjects();
		dao.listObjects(idMap);
		//Graphs.addAllVertices(grafo, vertici);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiungere gli archi
		
		
		/*
		//APPROCCIO 1 
		//faccio fare meno lavoro al db ma più lavoro al pc
		//-> Doppio ciclo for sui vertici
		//Dati due vertici, controllo se sono collegati
		
		for(ArtObject a1: this.grafo.vertexSet()) {
			for(ArtObject a2: this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {
					//devo collegare a1 ad a2? chiedo al dao
					int peso = dao.getPeso(a1,a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}
		
		System.out.println("GRAFO CREATO!!!");
		System.out.println("#VERTICI: "+ grafo.vertexSet().size()); 
		System.out.println("#ARCHI: "+grafo.edgeSet().size());  //NON CI STAMPERà NULLA PERCHé L'APPROCCIO 1 è MOLTO LENTO
		*/
		
		
		 /* APPROCCIO 2
		  String sql="SELECT e2.object_id, COUNT(*) AS peso "
		  		+ "FROM exhibition_objects AS e1, exhibition_objects AS e2 "
		  		+ "WHERE e1.exhibition_id=e2.exhibition_id AND e1.object_id=8485 AND e1.object_id != e2.object_id "
		  		+ "GROUP BY e2.object_id";
		  	
		  		ANCORA TROPPO LENTO
		  		*/
		 
		//APPROCCIO 3
		
		for(Adiacenza a: dao.getAdiacenze()) {
			
			Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
			
		}
		System.out.println("GRAFO CREATO!!!");
		System.out.println("#VERTICI: "+ grafo.vertexSet().size()); 
		System.out.println("#ARCHI: "+grafo.edgeSet().size());  //NON CI STAMPERà NULLA PERCHé L'APPROCCIO 1 è MOLTO LENTO
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
}
