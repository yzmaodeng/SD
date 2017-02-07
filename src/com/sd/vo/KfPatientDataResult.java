package com.sd.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@Table(name="tkfpatientResult")
@SuppressWarnings("serial")
@DynamicInsert(true)
@DynamicUpdate(true)
@SelectBeforeUpdate(true)
public class KfPatientDataResult extends BaseVo {

	@Column()
	private String id;
	@Id
	@Column()
	private String gid ;
	@Column()
	private String teamGid;
	@Column()
	private String date;
	@Column()
	private String motionGid;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Column()
	private String leg;
	@Column()
	private String type;
	@Column()
	private Integer maxNum;
	@Column()private Integer minNum;
	@Column()private double avgNum;
	@Column()private Integer sumNum;
	@Column()private Integer maxScore;
	@Column()private Integer minScore;
	@Column()private double avgScore;
	@Column()private Integer sumScore;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getTeamGid() {
		return teamGid;
	}
	public void setTeamGid(String teamGid) {
		this.teamGid = teamGid;
	}

	public String getMotionGid() {
		return motionGid;
	}
	public void setMotionGid(String motionGid) {
		this.motionGid = motionGid;
	}
	
	public String getType() {
		return type;
	}
	public String getLeg() {
		return leg;
	}
	public void setLeg(String leg) {
		this.leg = leg;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public Integer getMinNum() {
		return minNum;
	}
	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	
	public double getAvgNum() {
		return avgNum;
	}
	public void setAvgNum(double avgNum) {
		this.avgNum = avgNum;
	}
	public Integer getSumNum() {
		return sumNum;
	}
	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}
	public Integer getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}
	public Integer getMinScore() {
		return minScore;
	}
	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}
	
	public double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}
	public Integer getSumScore() {
		return sumScore;
	}
	public void setSumScore(Integer sumScore) {
		this.sumScore = sumScore;
	}
	
	          
	 
	       
	          
	     
	           
	          
	        
	        
	        
	        
	      
	      
	      
	      

}
