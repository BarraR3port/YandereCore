package com.podcrash.commissions.yandere.core.common.data.user.props;

import com.podcrash.commissions.yandere.core.common.data.reward.RewardType;

import java.util.Date;

public class Reward {
    private final String id;
    private final Date createDate;
    private final String name;
    private final RewardType type;
    
    public Reward(String name, RewardType type, String id){
        this.name = name;
        this.id = id;
        this.createDate = new Date();
        this.type = type;
    }
    
    public String getName(){
        return name;
    }
    
    public String getID(){
        return id;
    }
    
    public Date getCreateDate(){
        return createDate;
    }
    
    public RewardType getType(){
        return type;
    }
}
