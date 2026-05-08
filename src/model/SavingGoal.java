package model;
public class SavingGoal {
    private double targetAmount;
    private double currentAmount;
    private int goalid;
    private String name;
    private String deadline;
    private String userId;
    //constructor
    public SavingGoal(int goalid,String name,String deadline,double targetAmount,String userId){
        this.goalid=goalid;
        this.name=name;
        this.deadline=deadline;
        this.targetAmount=targetAmount;
        this.currentAmount=0.0;
        this.userId=userId;
    }
    public double trackprogress(){
        if(targetAmount<=0)return 0.0;
        return (currentAmount/targetAmount)*100.0;
    }
    //getters
    public double getTargetAmount(){return targetAmount;}
    public String getName(){return name;}
    public double getCurrentAmount(){return currentAmount;}
    public int getGoalID(){return goalid;}
    public String getDeadline(){return deadline;}
    public String getUserId(){return userId;}

    // setters
    public void setTargetAmount(double TargetAmount){targetAmount=TargetAmount;}
    public void setName(String Name){name=Name;}
    public void setCurrentAmount(double CurrentAmount){currentAmount=CurrentAmount;}
    public void setGoalID(int GoalID){goalid=GoalID;}
    public void setDeadline(String Deadline){deadline=Deadline;}
    public void SetuserId(String userId){this.userId=userId;}


}
