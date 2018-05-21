package com.practise.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Event model(POJO), set采用了链式调用                                                                                                                                                                                                                                                                                                                                                                       
 * @ClassName:
 * @Description:
 * @author Yao
 * 
 * @date 2018年5月12日 下午3:33:31
 */
public class EventModel {
	private EventType type;
	private int actorId; //事件出发者id
	private int entityType;
	private int entityId;
	private int entityOwnerId; //该实体拥有者，比如actorid点赞，给entityOwnerId发站内信。
	
	//构造函数
	public EventModel() {
	}
	
	public EventModel(EventType type) {
        this.type = type;
    }
	
	private Map<String, String> exts = new HashMap<String, String>();
	
	
    public String getExt(String key) {
        return exts.get(key);
    }
    
	public EventModel setExt(String key, String value){
		exts.put(key, value);
		return this;
	}
	
	
	public EventType getType() {
		return type;
	}

	public EventModel setType(EventType type) {
		this.type = type;
		return this;
	}

	public int getActorId() {
		return actorId;
	}

	public EventModel setActorId(int actorId) {
		this.actorId = actorId;
		return this;

	}

	public int getEntityType() {
		return entityType;
	}

	public EventModel setEntityType(int entityType) {
		this.entityType = entityType;
		return this;

	}

	public int getEntityId() {
		return entityId;
	}

	public EventModel setEntityId(int entityId) {
		this.entityId = entityId;
		return this;

	}

	public int getEntityOwnerId() {
		return entityOwnerId;
	}

	public EventModel setEntityOwnerId(int entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
		return this;

	}

	public Map<String, String> getExts() {
		return exts;
	}

	public EventModel setExts(Map<String, String> exts) {
		this.exts = exts;
		return this;

	}
	
}
