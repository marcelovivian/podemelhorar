package sistema.com.br.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import sistema.com.br.dao.DAO;
import sistema.com.br.entity.Cidade;

@FacesConverter(value="cidadeConverter")
public class CidadeConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                Cidade cidade = new DAO<Cidade>(Cidade.class).buscaPorId(new Long(submittedValue));
                return cidade;
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Cidade"));  
            }  
        }  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Cidade) value).getId());  
        }  
    }  
	
}
