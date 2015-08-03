package org.vvv.chatbotdb.model;

public class SMCondition {
	
	@Override
	public String toString() {
		return "SMCondition [smVariable=" + smVariable
				+ ", value=" + value + "]";
	}

	private Long id;
	
	private SMRule smRule;
	
	private SMVariable smVariable;
	
	private Boolean value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SMRule getSmRule() {
		return smRule;
	}

	public void setSmRule(SMRule smRule) {
		this.smRule = smRule;
	}

	public SMVariable getSmVariable() {
		return smVariable;
	}

	public void setSmVariable(SMVariable smVariable) {
		this.smVariable = smVariable;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

}
