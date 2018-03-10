package org.restsql.core.sqlresource;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlaceHolder")
public class PlaceHolder {
    
    @XmlAttribute(name = "name", required = true)
    protected String  name;
    
    @XmlAttribute(name = "label")
    protected String  label;    
    
    
    @XmlAttribute(name = "index" , required = true)
    protected Integer index;
    
    @XmlAttribute(name = "type", required = true)
    protected String  type;
    
    @XmlAttribute(name = "valeurParDefaut")
    protected String  valeurParDefaut;    
        
    @XmlAttribute(name = "format")
    protected String  format;    

    @XmlElement(name = "choix")
    protected List<Choix> choix;    
    

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getValeurParDefaut() {
        return valeurParDefaut;
    }

    public void setValeurParDefaut(String valeurParDefaut) {
        this.valeurParDefaut = valeurParDefaut;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Choix> getChoix() {
        if(choix == null){
            choix = new ArrayList<>();
        }
        return choix;
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
