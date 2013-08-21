package com.win.knowledge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.win.tools.easy.platform.entity.BaseEntity;

/**
 * 项目对象
 * 
 * @author 袁晓冬
 * 
 */
@Entity
@Table(name = "T_KNOWLEDGE_PROJECT")
public class ProjectEntity extends BaseEntity {

	/** 项目ID */
	private String id;

	/** 父节点ID */
	private String pid;
	/** 是否为末级项目 */
	private boolean leaf;

	/** 项目名称 */
	private String name;

	/** 描述 */
	private String remark;


	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid2")
	@GeneratedValue(generator = "idGenerator")
	@Column(length = 40)
	public String getId() {
		return id;
	}

	@Column(length = 40)
	public String getName() {
		return name;
	}


	public String getPid() {
		return pid;
	}

	@Column(length = 1024)
	public String getRemark() {
		return remark;
	}


	public boolean isLeaf() {
		return leaf;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
