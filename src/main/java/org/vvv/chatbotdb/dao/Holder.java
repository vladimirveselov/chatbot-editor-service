package org.vvv.chatbotdb.dao;


public class Holder {
    
    private DBHelper dbHelper;
    
    private TopicDBHelper topicDBHelper;
    
    private RuleDBHelper ruleDBHelper;
    
    private InputDBHelper inputDBHelper;
    
    private SplitInputDBHelper splitInputDBHelper;
    
    private OutputDBHelper outputDBHelper;
    
    private QueryDBHelper queryDBHelper;
    
    private SplitQueryDBHelper splitQueryDBHelper;
    
    private SearchAnswerDBHelper searchAnswer;

    private ChatbotDBHelper chatbotDBHelper;
    
    private StateMachineDBHelper stateMachineDBHelper;
    
    private ActionDBHelper actionDBHelper;
    
    public ActionDBHelper getActionDBHelper() {
        return actionDBHelper;
    }

    public void setActionDBHelper(ActionDBHelper actionDBHelper) {
        this.actionDBHelper = actionDBHelper;
    }

    public StateMachineDBHelper getStateMachineDBHelper() {
		return stateMachineDBHelper;
	}

	public void setStateMachineDBHelper(StateMachineDBHelper stateMachineDBHelper) {
		this.stateMachineDBHelper = stateMachineDBHelper;
	}

	public ChatbotDBHelper getChatbotDBHelper() {
        return chatbotDBHelper;
    }

    public void setChatbotDBHelper(ChatbotDBHelper chatbotDBHelper) {
        this.chatbotDBHelper = chatbotDBHelper;
    }

    public SearchAnswerDBHelper getSearchAnswer() {
        return searchAnswer;
    }

    public void setSearchAnswer(SearchAnswerDBHelper searchAnswer) {
        this.searchAnswer = searchAnswer;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public TopicDBHelper getTopicDBHelper() {
        return topicDBHelper;
    }

    public void setTopicDBHelper(TopicDBHelper topicDBHelper) {
        this.topicDBHelper = topicDBHelper;
    }

    public RuleDBHelper getRuleDBHelper() {
        return ruleDBHelper;
    }

    public void setRuleDBHelper(RuleDBHelper ruleDBHelper) {
        this.ruleDBHelper = ruleDBHelper;
    }

    public InputDBHelper getInputDBHelper() {
        return inputDBHelper;
    }

    public void setInputDBHelper(InputDBHelper inputDBHelper) {
        this.inputDBHelper = inputDBHelper;
    }

    public SplitInputDBHelper getSplitInputDBHelper() {
        return splitInputDBHelper;
    }

    public void setSplitInputDBHelper(SplitInputDBHelper splitInputDBHelper) {
        this.splitInputDBHelper = splitInputDBHelper;
    }

    public OutputDBHelper getOutputDBHelper() {
        return outputDBHelper;
    }

    public void setOutputDBHelper(OutputDBHelper outputDBHelper) {
        this.outputDBHelper = outputDBHelper;
    }

    public QueryDBHelper getQueryDBHelper() {
        return queryDBHelper;
    }

    public void setQueryDBHelper(QueryDBHelper queryDBHelper) {
        this.queryDBHelper = queryDBHelper;
    }

    public SplitQueryDBHelper getSplitQueryDBHelper() {
        return splitQueryDBHelper;
    }

    public void setSplitQueryDBHelper(SplitQueryDBHelper splitQueryDBHelper) {
        this.splitQueryDBHelper = splitQueryDBHelper;
    }
    
    
    public void init() {
        this.dbHelper = new DBHelper();
        this.topicDBHelper = new TopicDBHelper();
        this.topicDBHelper.setDbHelper(this.dbHelper);
        this.topicDBHelper.setHolder(this);
        
        this.ruleDBHelper = new RuleDBHelper();
        this.ruleDBHelper.setDbHelper(this.dbHelper);
        this.ruleDBHelper.setHolder(this);
        
        this.inputDBHelper = new InputDBHelper();
        this.inputDBHelper.setDbHelper(this.dbHelper);
        this.inputDBHelper.setHolder(this);
        
        this.splitInputDBHelper = new SplitInputDBHelper();
        this.splitInputDBHelper.setDbHelper(this.dbHelper);
        this.splitInputDBHelper.setHolder(this);
        
        this.outputDBHelper = new OutputDBHelper();
        this.outputDBHelper.setDbHelper(this.dbHelper);
        this.outputDBHelper.setHolder(this);
        
        this.queryDBHelper = new QueryDBHelper();
        this.queryDBHelper.setDbHelper(this.dbHelper);
        this.queryDBHelper.setHolder(this);
        
        this.splitQueryDBHelper = new SplitQueryDBHelper();
        this.splitQueryDBHelper.setDbHelper(this.dbHelper);        
        this.splitQueryDBHelper.setHolder(this);
        
        this.searchAnswer = new SearchAnswerDBHelper();
        this.searchAnswer.setDbHelper(dbHelper);
        this.searchAnswer.setHolder(this);

        this.chatbotDBHelper = new ChatbotDBHelper();
        this.chatbotDBHelper.setDbHelper(dbHelper);
        this.chatbotDBHelper.setHolder(this);
        
        this.stateMachineDBHelper = new StateMachineDBHelper();
        this.stateMachineDBHelper.setDbHelper(dbHelper);
        this.stateMachineDBHelper.setHolder(this);

        this.actionDBHelper = new ActionDBHelper();
        this.actionDBHelper.setDbHelper(dbHelper);
        this.actionDBHelper.setHolder(this);

    }

}
