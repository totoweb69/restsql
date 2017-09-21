package org.restsql.core.sqlresource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlaceHolder")
public class PlaceHolder {
    
    @XmlAttribute(name = "name", required = true)
    protected String  name;
    
    @XmlAttribute(name = "idx", required = true)
    protected Integer index;
    
    @XmlAttribute(name = "type", required = true)
    protected String  type;
        

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObjectValue(String strVal){        
        switch(type.toUpperCase()){
            case "NUMBER" :
                try{
                    Long retour = Long.parseLong(strVal);
                    return retour;
                }catch(Exception e){
                    Double retour = Double.parseDouble(strVal);
                    return retour;
                }
            default:
                return strVal;
        }
    }
    
}
