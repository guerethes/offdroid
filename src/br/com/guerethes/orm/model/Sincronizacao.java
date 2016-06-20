package br.com.guerethes.orm.model;

import br.com.guerethes.orm.annotation.ddl.Column;
import br.com.guerethes.orm.annotation.ddl.Entity;
import br.com.guerethes.orm.annotation.ddl.Id;
import br.com.guerethes.orm.annotation.ddl.Table;
import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.enumeration.validation.TypeColumn;
import br.com.guerethes.synchronization.annotation.OnlyLocalStorage;

@Entity
@OnlyLocalStorage
@Table("sincronizacao")
@SuppressWarnings("serial")
public class Sincronizacao implements PersistDB {
    
	@Id
    private Integer id;
    
    @Column(name = "classe", length = 200, allowNulls = false, typeColumn = TypeColumn.VARCHAR)
    private String classe;

    @Column(name = "id_classe", length = 200, allowNulls = false, typeColumn = TypeColumn.INTEGER)
    private Integer idClasse;

    @Column(name = "json", allowNulls = false, typeColumn = TypeColumn.VARCHAR)
    private String json;

    @Column(name = "operacao", allowNulls = false, typeColumn = TypeColumn.INTEGER)
    private Integer operacao;

	public Sincronizacao() {
	}

	public Sincronizacao(String classe, int idClasse, int operacao, String json) {
		this.classe = classe;
		this.idClasse = idClasse;
		this.json = json;
		this.operacao = operacao;
	}

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

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Integer getOperacao() {
		return operacao;
	}

	public void setOperacao(Integer operacao) {
		this.operacao = operacao;
	}

}