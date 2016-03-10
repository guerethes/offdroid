package br.com.guerethes.orm.model;

import br.com.guerethes.orm.annotation.ddl.Column;
import br.com.guerethes.orm.annotation.ddl.Entity;
import br.com.guerethes.orm.annotation.ddl.Id;
import br.com.guerethes.orm.annotation.ddl.Table;
import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.enumeration.validation.TypeColumn;

@Entity
@Table("sincronizacao")
@SuppressWarnings("serial")
public class Sincronizacao implements PersistDB {

	public Sincronizacao() {
	}

	public Sincronizacao(String classe, int idClasse) {
		this.classe = classe;
		this.idClasse = idClasse;
	}
	
    @Id
    private Integer id;
    
    @Column(name = "classe", length = 200, allowNulls = false, typeColumn = TypeColumn.VARCHAR)
    private String classe;

    @Column(name = "id_classe", length = 200, allowNulls = false, typeColumn = TypeColumn.INTEGER)
    private Integer idClasse;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public Integer getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}
 
}